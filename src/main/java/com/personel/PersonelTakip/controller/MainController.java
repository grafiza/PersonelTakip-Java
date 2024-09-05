package com.personel.PersonelTakip.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
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
            // JSON dosyasını kaynak dizininden okur
            ClassPathResource resource = new ClassPathResource("LeaveDays.json");
            Path path = resource.getFile().toPath();
            String jsonContent = new String(Files.readAllBytes(path));

            // JSON içeriğini döner
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jsonContent);

        } catch (IOException e) {
            // Hata durumunda 500 Internal Server Error döner
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error reading JSON file");
        }
    }
}
