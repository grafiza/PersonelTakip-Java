package com.personel.PersonelTakip.controller;

import com.personel.PersonelTakip.helpers.requests.PersonelRequest;
import com.personel.PersonelTakip.helpers.responses.PersonelResponse;
import com.personel.PersonelTakip.service.PersonelService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personel")
@AllArgsConstructor
public class PersonelController {

    private final PersonelService personelService;



    @GetMapping
    public ResponseEntity<List<PersonelResponse>> getAllPersonel() {
        List<PersonelResponse> personelList = personelService.getAllPersonel();
        return ResponseEntity.ok(personelList);
    }

    @PostMapping
    public ResponseEntity<PersonelResponse> createPersonel(@RequestBody PersonelRequest personelRequest) {
        PersonelResponse createdPersonel = personelService.savePersonel(personelRequest);
        return new ResponseEntity<>(createdPersonel, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonelResponse> updatePersonel(@PathVariable Long id, @RequestBody PersonelRequest personelRequest) {
        PersonelResponse response = personelService.updatePersonel(id, personelRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonel(@PathVariable Long id) {
        personelService.deletePersonel(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
