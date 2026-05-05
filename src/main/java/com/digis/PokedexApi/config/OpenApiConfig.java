package com.digis.PokedexApi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI pokedexOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("PokedexApi")
                        .description("""
                                     API REST PARA GESTIONARUSUARIOS POKEMON, FAVORITOS Y CONSULTAR DATOS DESDE LA PokeAPI EXTERNA
                                     """)
                        .version("1.0.0"))
                .tags(List.of(
                        new Tag().name("Catálogo").description("Tipos de Pokémon y roles de usuario"),
                        new Tag().name("PokéAPI").description("Consulta de pokémon desde la PokéAPI (con caché)"),
                        new Tag().name("Favoritos").description("Gestión de pokémon favoritos por usuario"),
                        new Tag().name("Usuarios").description("CRUD de usuarios Pokémon"),
                        new Tag().name("Caché").description("Administración del caché de la aplicación")));
    }

}
