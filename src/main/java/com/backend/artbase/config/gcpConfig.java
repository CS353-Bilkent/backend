// package com.backend.artbase.config;

// import java.io.FileInputStream;
// import java.io.IOException;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// import com.google.auth.oauth2.GoogleCredentials;
// import com.google.cloud.spring.core.CredentialsSupplier;
// import com.google.cloud.spring.core.DefaultCredentialsProvider;

// @Configuration
// public class gcpConfig {

// @Value("${gcp.config.db.file}")
// private String keyFile;

// @Bean
// public GoogleCredentials getCredentials() throws IOException {
// return GoogleCredentials.fromStream(new FileInputStream(keyFile));

// }

// }
