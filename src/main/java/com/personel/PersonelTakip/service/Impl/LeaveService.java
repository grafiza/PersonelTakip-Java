package com.personel.PersonelTakip.service.Impl;

import com.personel.PersonelTakip.common.GeneralException;
import com.personel.PersonelTakip.entity.Employee;
import com.personel.PersonelTakip.entity.Leave;
import com.personel.PersonelTakip.repository.EmployeeRepository;
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
    private final EmployeeRepository employeeRepository;

    public LeaveService(LeaveRepository leaveRepository, EmployeeService employeeService, EmployeeRepository employeeRepository) {
        this.leaveRepository = leaveRepository;
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
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
        return leaveRepository.findById(id).orElseThrow(() -> new GeneralException("Izin Kaydı Bulunamadı"));
    }

    @Override
    public List<Leave> getAll() {
        return leaveRepository.findAll();
    }

    @Override
    public Page<Leave> getAll(Pageable pageable) {
        return leaveRepository.findAll(pageable);

    }

    @Override
    public Leave update(Leave leave) {
        return null;
    }

    @Override
    public Leave update(Long id, Leave leave) {
        // Veritabanından mevcut Leave nesnesini bulun
        Leave foundLeave = getById(id);

        // Elde edilen Leave nesnesini güncelleyin
        updateLeaveDetails(foundLeave, leave);

        // Güncellenmiş Leave nesnesini kaydedin
        Leave updatedLeave = leaveRepository.save(foundLeave);

        // İlişkili Employee nesnesini güncelleyin
        if (leave.getEmployee() != null && leave.getEmployee().getId() != null) {
            updateEmployeeDetails(leave.getEmployee());
        }

        return updatedLeave;
    }

    private void updateLeaveDetails(Leave foundLeave, Leave leave) {
        foundLeave.setLeaveType(leave.getLeaveType());
        foundLeave.setLeaveStartDate(leave.getLeaveStartDate());
        foundLeave.setLeaveEndDate(leave.getLeaveEndDate());
        foundLeave.setDescription(leave.getDescription());
    }

    private void updateEmployeeDetails(Employee leaveEmployee) {
        Employee employee = employeeRepository.findById(leaveEmployee.getId())
                .orElseThrow(() -> new GeneralException("Employee not found"));
        employee.calculateRemainingLeaveDays();
        employeeService.save(employee); // Güncellenmiş employee'yi kaydet
    }


    @Transactional
    @Override
    public void delete(Long id) {
        if (!leaveRepository.existsById(id)) {
            throw new GeneralException("Izin Kaydı Bulunamadı");
        }
        Leave leave = leaveRepository.findById(id).orElseThrow();
        Employee employee = leave.getEmployee();

        // Leave entity'sini silmeden önce Employee'yi güncelle
        employee.calculateRemainingLeaveDays();
        employeeService.save(employee);

        // Leave entity'sini sil
        leaveRepository.deleteById(id);
    }

    // Yıllık izin hakkını hesaplar
    public double calculateLeaveEntitlement(LocalDate startDate, LocalDate endDate, List<Leave> pastLeaves) {
        long yearsWorked = ChronoUnit.YEARS.between(startDate, endDate);

        double totalLeaveDays = getLeaveDaysByYearsWorked(yearsWorked);

        double usedLeaveLastTwoYears = calculateUsedLeaveInLastTwoYears(pastLeaves, endDate);

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
    private double calculateUsedLeaveInLastTwoYears(List<Leave> pastLeaves, LocalDate endDate) {
        LocalDate twoYearsAgo = endDate.minusYears(2);
        return pastLeaves.stream()
                .filter(leave -> leave.getLeaveEndDate().isAfter(twoYearsAgo))
                .mapToDouble(Leave::getLeaveDays)
                .sum();
    }
}
