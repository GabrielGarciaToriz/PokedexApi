package com.digis.PokedexApi.config;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DataSourceConfig {
    
    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:oracle:thin:@192.167.0.100:1521:orcl");
        dataSource.setUsername("DAraizaPokeApi");
        dataSource.setPassword("password1");
        return dataSource;
        
    }
    
    
}
