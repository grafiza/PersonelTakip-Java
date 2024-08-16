package com.personel.PersonelTakip.controller;

import com.personel.PersonelTakip.helpers.requests.IzinRequest;
import com.personel.PersonelTakip.helpers.responses.IzinResponse;
import com.personel.PersonelTakip.service.IzinService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/izin")
@AllArgsConstructor
public class IzinController {

    private final IzinService izinService;



    @GetMapping
    public ResponseEntity<List<IzinResponse>> getAllIzin() {
        List<IzinResponse> izinList = izinService.getAllIzin();
        return ResponseEntity.ok(izinList);
    }

    @PostMapping
    public ResponseEntity<IzinResponse> createIzin(@RequestBody IzinRequest izinRequest) {
        IzinResponse createdIzin = izinService.saveIzin(izinRequest);
        return new ResponseEntity<>(createdIzin, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IzinResponse> updateIzin(@PathVariable Long id, @RequestBody IzinRequest izinRequest) {
        IzinResponse updatedIzin = izinService.updateIzin(id, izinRequest);
        return ResponseEntity.ok(updatedIzin);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIzin(@PathVariable Long id) {
        izinService.deleteIzin(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
