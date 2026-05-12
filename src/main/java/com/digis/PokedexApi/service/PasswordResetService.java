package com.digis.PokedexApi.service;

import com.digis.PokedexApi.dto.Result;
import com.digis.PokedexApi.entity.PasswordResetToken;
import com.digis.PokedexApi.entity.UsuarioPokemon;
import com.digis.PokedexApi.exception.ErrorCode;
import com.digis.PokedexApi.repository.PasswordResetTokenRepository;
import com.digis.PokedexApi.repository.UsuarioPokemonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService extends BaseService {

    @Autowired
    private UsuarioPokemonRepository usuarioRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.token.expiracion-minutos}")
    private int expiracionMinutos;

    @Transactional
    public Result solicitarRecuperacion(String correo) {
        try {
            Optional<UsuarioPokemon> optUsuario = usuarioRepository.findByCorreo(correo);

            if (optUsuario.isEmpty() || !optUsuario.get().getActivo()) {
                return Result.ok("Si el correo existe y la cuenta está activa, recibirás un enlace en breve.");
            }

            UsuarioPokemon usuario = optUsuario.get();

            tokenRepository.deleteByUsuariioId(usuario.getIdUsuarioPokemon());

            String tokenValor = UUID.randomUUID().toString();

            PasswordResetToken token = new PasswordResetToken();
            token.setToken(tokenValor);
            token.setUsuario(usuario);
            token.setExpiration(LocalDateTime.now().plusMinutes(expiracionMinutos));
            token.setUsado(false);

            tokenRepository.save(token);

            emailService.enviarCorreoRecuperacion(usuario.getCorreo(), tokenValor);

            return Result.ok("Si el correo existe y la cuenta está activa, recibirás un enlace en breve.");

        } catch (Exception e) {
            return Result.error(ErrorCode.INTERNAL_ERROR, "Error al procesar la solicitud", e);
        }
    }

    public Result validarToken(String tokenValor) {
        Optional<PasswordResetToken> optToken = tokenRepository.findByToken(tokenValor);

        if (optToken.isEmpty() || !optToken.get().esValido()) {
            return Result.error(ErrorCode.UNAUTHORIZED, "El enlace es inválido o ya expiró.");
        }

        return Result.ok("Token válido.");
    }

    @Transactional
    public Result cambiarPassword(String tokenValor, String nuevaPassword) {
        try {
            Optional<PasswordResetToken> optToken = tokenRepository.findByToken(tokenValor);

            if (optToken.isEmpty() || !optToken.get().esValido()) {
                return Result.error(ErrorCode.UNAUTHORIZED, "El enlace es inválido o ya expiró.");
            }

            PasswordResetToken token = optToken.get();
            UsuarioPokemon usuario = token.getUsuario();

            usuario.setPassword(passwordEncoder.encode(nuevaPassword));
            usuarioRepository.save(usuario);

            token.setUsado(true);
            tokenRepository.save(token);

            return Result.ok("Contraseña actualizada correctamente.");

        } catch (Exception e) {
            return Result.error(ErrorCode.INTERNAL_ERROR, "Error al cambiar la contraseña", e);
        }
    }
}