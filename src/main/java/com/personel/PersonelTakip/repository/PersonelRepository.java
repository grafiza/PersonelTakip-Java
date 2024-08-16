package com.personel.PersonelTakip.repository;

import com.personel.PersonelTakip.entity.Personel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonelRepository extends JpaRepository<Personel,Long> {
    Optional<Personel> findByTc(String idNumber);
}
