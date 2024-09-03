package com.personel.PersonelTakip.service;
import com.personel.PersonelTakip.entity.Employee;
import com.personel.PersonelTakip.entity.Leave;
import com.personel.PersonelTakip.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class EmployeeLeaveScheduler {


    private final EmployeeRepository employeeRepository;

    public EmployeeLeaveScheduler(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Her gün gece 12:00'de çalışacak
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateEmployeeLeaveEntitlements() {
        List<Employee> employees = employeeRepository.findAll();

        for (Employee employee : employees) {
            if (employee.getStartDate() != null) {
                long yearsWorked = ChronoUnit.YEARS.between(employee.getStartDate(), LocalDate.now());

                // Yıllık izin günlerini hesapla
                Long leaveDays = calculateLeaveDays(yearsWorked);

                // Kalan izin günlerini güncelle
                employee.setLeaveDays(leaveDays);
                employee.setRemainingLeaveDays(leaveDays - calculateUsedLeave(employee));

                // Veritabanına kaydet
                employeeRepository.save(employee);
            }
        }
    }

    private Long calculateLeaveDays(long yearsWorked) {
        if (yearsWorked == 0) return 0L;
        if (yearsWorked == 1) return 14L;
        if (yearsWorked >= 2 && yearsWorked <= 5) return 28L;
        if (yearsWorked == 6) return 34L;
        if (yearsWorked >= 7 && yearsWorked <= 15) return 40L;
        if (yearsWorked == 16) return 46L;
        return 52L;  // 17 yıl ve üstü
    }

    private double calculateUsedLeave(Employee employee) {


        LocalDate twoYearsAgo = LocalDate.now().minusYears(2);
        return employee.getLeaves().stream()
                .filter(leave -> leave.getLeaveEndDate().isAfter(twoYearsAgo))
                .mapToDouble(Leave::getLeaveDays)
                .sum();

    }
}
