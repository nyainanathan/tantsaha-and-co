package com.tantsaha.tantsaha.repository;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;

@Configuration
public class DataSourceConfig {

  @Bean
  public Connection getConnection(){
    try{
      String dbUrl = System.getenv("DB_URL");
      String user = System.getenv("DB_USER");
      String password = System.getenv("DB_PASS");

      return DriverManager.getConnection(dbUrl, user, password);

    } catch (Exception e){
      throw new RuntimeException(e);
    }
  }
}