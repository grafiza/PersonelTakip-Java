package com.personel.PersonelTakip.repository;

import com.personel.PersonelTakip.entity.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRepository extends JpaRepository<Leave,Long> {

    List<Leave> findByEmployeeId(Long id);
}
