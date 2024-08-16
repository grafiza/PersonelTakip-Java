package com.personel.PersonelTakip.service;

import com.personel.PersonelTakip.entity.Izin;
import com.personel.PersonelTakip.entity.Personel;
import com.personel.PersonelTakip.helpers.mappers.IzinMapper;
import com.personel.PersonelTakip.helpers.requests.IzinRequest;
import com.personel.PersonelTakip.helpers.responses.IzinResponse;
import com.personel.PersonelTakip.repository.IzinRepository;
import com.personel.PersonelTakip.repository.PersonelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class IzinService {

    private final IzinRepository izinRepository;
    private final PersonelRepository personelRepository;
    private final IzinMapper izinMapper;



    public List<IzinResponse> getAllIzin() {
        return izinRepository.findAll().stream()
                .map(izinMapper::toResponse)
                .collect(Collectors.toList());
    }

    public IzinResponse saveIzin(IzinRequest izinRequest) {
        Personel personel = personelRepository.findById(izinRequest.getPersonelId())
                .orElseThrow(() -> new RuntimeException("Personel not found"));

        // İzin tarihlerini kontrol et
        LocalDate startDate = izinRequest.getStartDate();
        LocalDate endDate = izinRequest.getEndDate();

        // İzin süresini hesapla (gün cinsinden)
        long daysOfLeave = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;

        // Kalan izin hakkını güncelle
        if (personel.getLeaveRights() < daysOfLeave) {
            throw new RuntimeException("Yeterli izin hakkı yok.");
        }

        personel.setLeaveRights(personel.getLeaveRights() - (int) daysOfLeave);
        personelRepository.save(personel);

        Izin izin = izinMapper.toEntity(izinRequest, personel);
        izin = izinRepository.save(izin);
        return izinMapper.toResponse(izin);
    }

    public void deleteIzin(Long id) {
        Izin izin = izinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Izin not found"));
        Personel personel = izin.getPersonel();

        // İzin silindiğinde personelin izin hakkını geri yükle
        long daysOfLeave = java.time.temporal.ChronoUnit.DAYS.between(izin.getStartDate(), izin.getEndDate()) + 1;
        personel.setLeaveRights(personel.getLeaveRights() + (int) daysOfLeave);
        personelRepository.save(personel);

        izinRepository.deleteById(id);
    }

    public IzinResponse updateIzin(Long id, IzinRequest izinRequest) {
        Izin existingIzin = izinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Izin not found"));
        Personel personel = personelRepository.findById(izinRequest.getPersonelId())
                .orElseThrow(() -> new RuntimeException("Personel not found"));

        // Önce eski izin süresini geri yükle
        long oldDaysOfLeave = java.time.temporal.ChronoUnit.DAYS.between(existingIzin.getStartDate(), existingIzin.getEndDate()) + 1;
        personel.setLeaveRights(personel.getLeaveRights() + (int) oldDaysOfLeave);

        // Yeni izin süresini hesapla
        LocalDate startDate = izinRequest.getStartDate();
        LocalDate endDate = izinRequest.getEndDate();
        long newDaysOfLeave = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;

        if (personel.getLeaveRights() < newDaysOfLeave) {
            throw new RuntimeException("Yeterli izin hakkı yok.");
        }

        personel.setLeaveRights(personel.getLeaveRights() - (int) newDaysOfLeave);
        personelRepository.save(personel);

        Izin updatedIzin = izinMapper.toEntity(izinRequest, personel);
        updatedIzin.setId(existingIzin.getId());
        updatedIzin = izinRepository.save(updatedIzin);
        return izinMapper.toResponse(updatedIzin);
    }
}
