package com.personel.PersonelTakip.repository;

import com.personel.PersonelTakip.entity.Employee;
import com.personel.PersonelTakip.entity.Duty;
import com.personel.PersonelTakip.entity.Duty;
import com.personel.PersonelTakip.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    boolean existsBySsnNumber (String ssnNumber);
    List<Employee> findAllByDuty(Duty duty);

}
