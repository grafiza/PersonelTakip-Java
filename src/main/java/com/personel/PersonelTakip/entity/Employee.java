package com.personel.PersonelTakip.entity;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class Employee extends BaseEntity {

    @NotNull
    @Column(length = 11, unique = true)
    private String ssnNumber;
    @NotNull

    private String firstName;
    @NotNull
    private String lastName;
    private String phone;

    @Enumerated(EnumType.STRING)
    private Duty duty;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;
    @NotNull
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;

    // Yıllık izin hakları
    private double leaveDays; // Hak edilen izin günleri
    private double remainingLeaveDays; // Kullanılabilir kalan izin günleri

    // İzin geçmişi, son iki yılın izni burada tutulacak
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Leave> leaves;

    // Metotlar ve davranışlar
    @PostPersist
    @PostUpdate
    @PostRemove
    public void calculateRemainingLeaveDays() {
        this.remainingLeaveDays = this.leaveDays - calculateUsedLeave();
    }

    @PrePersist
    @PreUpdate
    public void calculateLeaveEntitlement() {
        long yearsWorked = ChronoUnit.YEARS.between(this.startDate, this.endDate != null ? this.endDate : LocalDate.now());

        this.leaveDays = calculateLeaveDays(yearsWorked);
        this.remainingLeaveDays = this.leaveDays - calculateUsedLeave(); // Kullanılan izinler düşülür
    }

    private double calculateLeaveDays(double yearsWorked) {
        if (yearsWorked == 0) return 0;
        if (yearsWorked == 1) return 14;
        if (yearsWorked >= 2 && yearsWorked <= 5) return 28;
        if (yearsWorked == 6) return 34;
        if (yearsWorked >= 7 && yearsWorked <= 15) return 40;
        if (yearsWorked == 16) return 46;
        return 52;  // 17 yıl ve üstü
    }

    private double calculateUsedLeave() {
        if (leaves == null || leaves.isEmpty()) {
            return 0;
        }

        LocalDate twoYearsAgo = LocalDate.now().minusYears(2);
        return leaves.stream()
                .filter(leave -> leave.getLeaveEndDate().isAfter(twoYearsAgo))
                .mapToDouble(Leave::getLeaveDays)
                .sum();
    }
}