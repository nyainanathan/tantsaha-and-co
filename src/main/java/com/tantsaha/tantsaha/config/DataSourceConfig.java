package com.tantsaha.tantsaha.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class DataSourceConfig {
    private final Dotenv dotenv = Dotenv.load();

    public Connection getConnection() {
        try {
            String dbUrl = dotenv.get("DB_URL");
            String user = dotenv.get("DB_USER");
            String password = dotenv.get("DB_PASS");

            return DriverManager.getConnection(dbUrl, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}