package com.personel.PersonelTakip.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/holidays")
public class MainController {
    @GetMapping
    public ResponseEntity<String> getHolidays() {
        try {
            ClassPathResource resource = new ClassPathResource("data/LeaveDays.json");
            Path path = resource.getFile().toPath();
            String jsonContent = new String(Files.readAllBytes(path));

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jsonContent);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error reading JSON file");
        }
    }
}
