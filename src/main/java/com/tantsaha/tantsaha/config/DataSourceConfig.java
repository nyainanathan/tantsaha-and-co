package com.tantsaha.tantsaha.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    private final Dotenv dotenv = Dotenv.load();

    @Bean
    public DataSource dataSource() {

        String url = dotenv.get("DB_URL");
        String user = dotenv.get("DB_USER");
        String pass = dotenv.get("DB_PASS");

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(pass);
        dataSource.setDriverClassName("org.postgresql.Driver");

        return dataSource;
    }
}