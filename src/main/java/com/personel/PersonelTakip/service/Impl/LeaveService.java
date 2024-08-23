package com.personel.PersonelTakip.service.Impl;

import com.personel.PersonelTakip.common.GeneralException;
import com.personel.PersonelTakip.entity.Employee;
import com.personel.PersonelTakip.entity.Leave;
import com.personel.PersonelTakip.repository.LeaveRepository;
import com.personel.PersonelTakip.service.ILeaveService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
@Service
public class LeaveService implements ILeaveService {


    private final LeaveRepository leaveRepository;
    private final EmployeeService employeeService;

    public LeaveService(LeaveRepository leaveRepository, EmployeeService employeeService) {
        this.leaveRepository = leaveRepository;
        this.employeeService = employeeService;
    }
    @Transactional
    @Override
    public Leave save(Leave leave) {
        Employee employee = employeeService.getById(leave.getEmployee().getId());
        if (employee == null || leave.getEmployee().getId() == null) {
            throw new GeneralException("Personel bilgisi eksik olamaz!");
        }




        leave.setEmployee(employee); // Employee'yi leave'e set et

        Leave savedLeave = leaveRepository.save(leave);
        employee.calculateRemainingLeaveDays();
        employeeService.save(employee); // Güncellenmiş employee'yi kaydet

        return savedLeave;
    }

    @Override
    public Leave getById(Long id) {
        return leaveRepository.findById(id).orElseThrow(()->new GeneralException("Izin Kaydı Bulunamadı"));
    }

    @Override
    public List<Leave> getAll() {
        return leaveRepository.findAll();
    }

    @Override
    public Page<Leave> getAll(Pageable pageable) {
        return leaveRepository.findAll(pageable);

    }
    @Transactional
    @Override
    public void delete(Long id) {
        if (!leaveRepository.existsById(id)) {
            throw new GeneralException("Izin Kaydı Bulunamadı");
        }
        Leave leave = leaveRepository.findById(id).orElseThrow();
        Employee employee = leave.getEmployee();
        leaveRepository.deleteById(id);
        employee.calculateRemainingLeaveDays();
        employeeService.save(employee);
    }

    // Yıllık izin hakkını hesaplar
    public int calculateLeaveEntitlement(LocalDate startDate, LocalDate endDate, List<Leave> pastLeaves) {
        long yearsWorked = ChronoUnit.YEARS.between(startDate, endDate);

        int totalLeaveDays = getLeaveDaysByYearsWorked(yearsWorked);

        int usedLeaveLastTwoYears = calculateUsedLeaveInLastTwoYears(pastLeaves, endDate);

        return totalLeaveDays - usedLeaveLastTwoYears;
    }

    // Çalışılan yıla göre izin günlerini döndürür
    private int getLeaveDaysByYearsWorked(long yearsWorked) {
        if (yearsWorked == 0) return 0;
        if (yearsWorked == 1) return 14;
        if (yearsWorked >= 2 && yearsWorked <= 5) return 28;
        if (yearsWorked == 6) return 34;
        if (yearsWorked >= 7 && yearsWorked <= 15) return 40;
        if (yearsWorked == 16) return 46;
        return 52;  // 17 yıl ve üstü
    }

    // Son iki yılda kullanılan izinleri hesaplar
    private int calculateUsedLeaveInLastTwoYears(List<Leave> pastLeaves, LocalDate endDate) {
        LocalDate twoYearsAgo = endDate.minusYears(2);
        return pastLeaves.stream()
                .filter(leave -> leave.getLeaveEndDate().isAfter(twoYearsAgo))
                .mapToInt(Leave::getLeaveDays)
                .sum();
    }
}
