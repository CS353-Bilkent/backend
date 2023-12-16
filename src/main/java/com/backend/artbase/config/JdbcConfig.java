// package com.backend.artbase.config;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.jdbc.datasource.DriverManagerDataSource;

// @Configuration
// public class JdbcConfig {

// @Value("${gcp.db.url}")
// private String url;

// @Value("${gcp.db.driver}")
// private String driverClassName;

// @Value("${gcp.db.username}")
// private String username;

// @Value("${gcp.db.password}")
// private String password;

// @Bean
// public JdbcTemplate jdbcTemplate() {
// return new JdbcTemplate(dataSource());
// }

// @Bean
// public DriverManagerDataSource dataSource() {
// DriverManagerDataSource dataSource = new DriverManagerDataSource();
// dataSource.setDriverClassName(driverClassName);
// dataSource.setUrl(url);
// dataSource.setUsername(username);
// dataSource.setPassword(password);
// return dataSource;
// }
// }
