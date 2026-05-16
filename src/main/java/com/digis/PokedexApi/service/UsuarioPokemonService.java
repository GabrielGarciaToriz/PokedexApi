package com.digis.PokedexApi.service;

import com.digis.PokedexApi.dto.Login.LoginRequestDTO;
import com.digis.PokedexApi.dto.Login.LoginResponseDTO;
import com.digis.PokedexApi.dto.Result;
import com.digis.PokedexApi.entity.TokenActivacion;
import com.digis.PokedexApi.entity.UsuarioPokemon;
import com.digis.PokedexApi.exception.ErrorCode;
import com.digis.PokedexApi.repository.TokenActivacionRepository;
import com.digis.PokedexApi.repository.UsuarioPokemonRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class UsuarioPokemonService extends BaseService {

    @Autowired
    private UsuarioPokemonRepository usuarioRepository;

    @Autowired
    private TokenActivacionRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    
   // Mapa en memoria para mantener las conexiones SSE activas por correo 
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    // Permite al controlador suscribir el navegador del usuario 
    public SseEmitter suscribirCliente(String email) {
        SseEmitter emitter = new SseEmitter(180_000L); // 3 minutos de tiempo de espera (Timeout)
        
        this.emitters.put(email, emitter);

        // Limpieza de memoria si el cliente cierra la pestaña o expira el tiempo
        emitter.onCompletion(() -> this.emitters.remove(email));
        emitter.onTimeout(() -> this.emitters.remove(email));
        
        return emitter;
    }

   
    public Result getAll() {
        return ejecutarLista(() -> usuarioRepository.getAllWithDetails());
    }

    public Result getAllByid(int idUsuarioPokemon) {
        return ejecutarLista(() -> usuarioRepository.getAllByIdWithDetails(idUsuarioPokemon));
    }

    public Result getAllByUsername(String username) {
        return ejecutarLista(() -> usuarioRepository.getAllByUserNameWithDetails(username));
    }

    public Result getAllByCorreo(String correo) {
        return ejecutar(() -> usuarioRepository.findByCorreo(correo));
    }

    public Result buscarUsuario(String nombre, String apellidoPaterno, String apellidoMaterno) {
        return ejecutarLista(() -> usuarioRepository.buscarPorFiltros(nombre, apellidoPaterno, apellidoMaterno));
    }

    // ── REGISTRO con envío de email ───────────────────────
    @Transactional
    public Result agregarUsuario(UsuarioPokemon usuario) {
        try {
            // 1. Encriptar contraseña antes de guardar
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

            // 2. Marcar como inactivo hasta confirmar email
            usuario.setActivo(false);

            // 3. Guardar usuario
            UsuarioPokemon guardado = usuarioRepository.save(usuario);

            // 4. Generar token único
            String token = UUID.randomUUID().toString().replace("-", "");

            // 5. Guardar token con expiración de 24 horas
            TokenActivacion tokenActivacion = new TokenActivacion(
                    token,
                    guardado,
                    LocalDateTime.now().plusHours(24)
            );
            tokenRepository.save(tokenActivacion);

            // 6. Enviar email (si falla, lanzamos excepción y se hace rollback)
            emailService.enviarEmailActivacion(
                    guardado.getCorreo(),
                    guardado.getNombre(),
                    token
            );

            return Result.ok("Usuario registrado. Revisa tu correo para activar tu cuenta.");

        } catch (Exception e) {
            return Result.error(ErrorCode.INTERNAL_ERROR, "Error al registrar usuario: " + e.getMessage());
        }
    }
    // ── LOGIN ─────────────────────────────────────────────

    public Result login(LoginRequestDTO request) {
        try {
            UsuarioPokemon usuario = usuarioRepository
                    .findByCorreo(request.getCorreo())
                    .orElseThrow(() -> new RuntimeException("Correo o contraseña incorrectos"));

            if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
                return Result.error(ErrorCode.INVALID_INPUT, "Correo o contraseña incorrectos");
            }

            if (!usuario.getActivo()) {
                return Result.error(ErrorCode.INVALID_INPUT,
                        "Cuenta no activada. Revisa tu correo o solicita un nuevo enlace.");
            }

            String rol = usuario.getRol() != null ? usuario.getRol().getRol() : "USER";

            // ✅ El token se genera con el CORREO como identificador
            String token = jwtService.generarToken(usuario.getCorreo(), rol);

            return Result.ok(new LoginResponseDTO(token, usuario.getCorreo(), usuario.getNombre(), rol));

        } catch (RuntimeException e) {
            return Result.error(ErrorCode.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return Result.error(ErrorCode.INTERNAL_ERROR, "Error en login.");
        }
    }

    // ── ACTIVACIÓN de cuenta ──────────────────────────────
    @Transactional
    public Result activarCuenta(String token) {
        try {
            // 1. Buscar token
            TokenActivacion tokenActivacion = tokenRepository.findByToken(token)
                    .orElseThrow(() -> new RuntimeException("Token inválido o inexistente"));

            // 2. Verificar que no fue usado
            if (tokenActivacion.isUsado()) {
                return Result.error(ErrorCode.INVALID_INPUT, "Este enlace ya fue utilizado.");
            }

            // 3. Verificar que no expiró
            if (LocalDateTime.now().isAfter(tokenActivacion.getFechaExpiracion())) {
                return Result.error(ErrorCode.INVALID_INPUT, "El enlace de activación ha expirado. Solicita uno nuevo.");
            }

            // 4. Activar usuario
            UsuarioPokemon usuario = tokenActivacion.getUsuarioPokemon();
            usuario.setActivo(true);
            usuarioRepository.save(usuario);

            // 5. Marcar token como usado
            tokenActivacion.setUsado(true);
            tokenRepository.save(tokenActivacion);
            
            SseEmitter emitter = emitters.get(usuario.getCorreo());
            if (emitter != null) {
                try {
                    // Generamos el JWT de inmediato para que el frontend inicie sesión directo
                    String rol = usuario.getRol() != null ? usuario.getRol().getRol() : "USER";
                    String jwtToken = jwtService.generarToken(usuario.getCorreo(), rol);

                    // Formateamos un JSON string de forma manual o con Jackson para enviar al cliente
                    String jsonPayload = String.format(
                        "{\"token\":\"%s\",\"correo\":\"%s\",\"nombre\":\"%s\",\"rol\":\"%s\"}",
                        jwtToken, usuario.getCorreo(), usuario.getNombre(), rol
                    );

                    // Disparamos el evento hacia el cliente conectado
                    emitter.send(SseEmitter.event()
                            .name("CUENTA_ACTIVADA")
                            .data(jsonPayload));
                    
                    // Finalizamos la conexión por haber cumplido exitosamente su propósito
                    emitter.complete();
                } catch (IOException e) {
                    // Si el cliente se desconectó abruptamente, lo removemos del mapa
                    emitters.remove(usuario.getCorreo());
                }
            }

            return Result.ok("¡Cuenta activada exitosamente! Ya puedes iniciar sesión.");

        } catch (RuntimeException e) {
            return Result.error(ErrorCode.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return Result.error(ErrorCode.INTERNAL_ERROR, "Error al activar cuenta.");
        }
    }

    // ── REENVIAR email de activación ──────────────────────
    @Transactional
    public Result reenviarActivacion(String email) {
        try {
            UsuarioPokemon usuario = usuarioRepository.findByCorreo(email)
                    .orElseThrow(() -> new RuntimeException("No existe un usuario con ese correo"));

            if (usuario.getActivo()) {
                return Result.error(ErrorCode.INVALID_INPUT, "Esta cuenta ya está activada.");
            }

            // Eliminar tokens anteriores
            tokenRepository.deleteByUsuarioId(usuario.getIdUsuarioPokemon());

            // Generar y guardar nuevo token
            String nuevoToken = UUID.randomUUID().toString().replace("-", "");
            tokenRepository.save(new TokenActivacion(
                    nuevoToken,
                    usuario,
                    LocalDateTime.now().plusHours(24)
            ));

            emailService.enviarEmailActivacion(email, usuario.getNombre(), nuevoToken);

            return Result.ok("Correo de activación reenviado. Revisa tu bandeja.");

        } catch (RuntimeException e) {
            return Result.error(ErrorCode.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return Result.error(ErrorCode.INTERNAL_ERROR, "Error al reenviar activación.");
        }
    }

    // ── ELIMINAR usuario ──────────────────────────────────
    @Transactional
    public Result eliminarUsuario(int idUsuario) {
        return ejecutarVoid(() -> {
            tokenRepository.deleteByUsuarioId(idUsuario); // primero los tokens (FK)
            usuarioRepository.deleteById(idUsuario);
        });
    }
}
