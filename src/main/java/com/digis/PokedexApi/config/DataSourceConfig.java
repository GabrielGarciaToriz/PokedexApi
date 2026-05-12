package com.digis.PokedexApi.config;

import javax.sql.DataSource;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.client.RestTemplate;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.caffeine.CaffeineCacheManager;


@Configuration
@EnableCaching
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:oracle:thin:@192.167.0.196:1521:orcl");
        dataSource.setUsername("DAraizaPokeApi");
        dataSource.setPassword("password1");
        return dataSource;

    }

    @Bean
    public RestTemplate pokemonRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
                "pokemon-name",
                "pokemon-id",
                "pokemon-todos",
                "pokemon-paginado"
        );
        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .maximumSize(500)
                        .expireAfterWrite(1, TimeUnit.HOURS
                        )
        );
        return cacheManager;
    }
}
