package com.nam;

import com.nam.service.FilesStorageService;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DAA implements CommandLineRunner {
    @Resource
    FilesStorageService storageService;
    public static void main(String[] args) {
        SpringApplication.run(DAA.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        storageService.init();
    }
}
