package com.backend.artbase.utils;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.backend.artbase.config.DatabaseConnectionChecker;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    private final DatabaseConnectionChecker connectionChecker;

    public ApplicationStartup(DatabaseConnectionChecker connectionChecker) {
        this.connectionChecker = connectionChecker;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("Executing Database Connection Check after application startup...");
        connectionChecker.checkDatabaseConnection();
    }
}
