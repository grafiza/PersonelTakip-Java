package com.personel.PersonelTakip.service;


import com.personel.PersonelTakip.entity.Personel;
import com.personel.PersonelTakip.helpers.mappers.PersonelMapper;
import com.personel.PersonelTakip.helpers.requests.PersonelRequest;
import com.personel.PersonelTakip.helpers.responses.PersonelResponse;
import com.personel.PersonelTakip.repository.PersonelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PersonelService {

    private final PersonelRepository personelRepository;
    private final PersonelMapper personelMapper;


    public List<PersonelResponse> getAllPersonel() {
        return personelRepository.findAll().stream()
                .map(personelMapper::toResponse)
                .collect(Collectors.toList());
    }

    public PersonelResponse savePersonel(PersonelRequest personelRequest) {
        // idNumber'ın benzersizliğini kontrol et
        Optional<Personel> existingPersonel = personelRepository.findByTc(personelRequest.getTc());
        if (existingPersonel.isPresent()) {
            throw new RuntimeException("Bu ID numarasına sahip bir personel zaten mevcut.");
        }
        Personel personel = personelMapper.toEntity(personelRequest);
        calculateLeaveRights(personel);
        personel = personelRepository.save(personel);
        return personelMapper.toResponse(personel);
    }

    public void deletePersonel(Long id) {
        personelRepository.deleteById(id);
    }

    public PersonelResponse updatePersonel(Long id, PersonelRequest personelRequest) {

        // Mevcut personeli bul
        Personel existingPersonel = personelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Personel not found"));

        // Yeni ID numarasını al
        String newIdNumber = personelRequest.getTc();

        // Yeni ID numarası varsa, başka bir personelde olup olmadığını kontrol et
        if (newIdNumber != null && !newIdNumber.equals(existingPersonel.getTc())) {
            Optional<Personel> personelWithSameId = personelRepository.findByTc(newIdNumber);
            if (personelWithSameId.isPresent()) {
                throw new RuntimeException("Bu ID numarasına sahip bir personel zaten mevcut.");
            }
        }

        // Personel bilgilerini güncelle
        existingPersonel.setTc(personelRequest.getTc());
        existingPersonel.setFirstName(personelRequest.getFirstName());
        existingPersonel.setLastName(personelRequest.getLastName());
        existingPersonel.setEmail(personelRequest.getEmail());
        existingPersonel.setPhoneNumber(personelRequest.getPhoneNumber());
        existingPersonel.setStartDate(personelRequest.getStartDate());
        existingPersonel.setEndDate(personelRequest.getEndDate());
        existingPersonel.setDepartment(personelRequest.getDepartment());
        existingPersonel.setRole(personelRequest.getRole());

        // İzin haklarını yeniden hesapla
        calculateLeaveRights(existingPersonel);

        // Güncellenmiş personeli kaydet
        existingPersonel = personelRepository.save(existingPersonel);
        return personelMapper.toResponse(existingPersonel);
    }

    private void calculateLeaveRights(Personel personel) {
        LocalDate startDate = personel.getStartDate();
        LocalDate endDate = personel.getEndDate() != null ? personel.getEndDate() : LocalDate.now();
        long yearsWorked = ChronoUnit.YEARS.between(startDate, endDate);

        // İzin hakkını yıllara göre belirle
        int leaveRights;
        if (yearsWorked <= 1) {
            leaveRights = 0;
        } else if (yearsWorked <= 5) {
            leaveRights = 14;
        } else if (yearsWorked <= 15) {
            leaveRights = 20;
        } else {
            leaveRights = 24;
        }

        personel.setLeaveRights(leaveRights);
    }

}
