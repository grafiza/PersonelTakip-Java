package com.personel.PersonelTakip.entity;
import java.time.DayOfWeek;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class Leave extends BaseEntity {
    @NotNull

    @Enumerated(EnumType.STRING)
    @NotNull
    private LeaveType leaveType;

    @Override
    public String toString() {
        return "Leave{" +
                "leaveType=" + leaveType +
                ", leaveStartDate=" + leaveStartDate +
                ", description='" + description + '\'' +
                ", leaveEndDate=" + leaveEndDate +
                ", employee=" + employee +
                '}';
    }

    @NotNull
    private LocalDate leaveStartDate;
    @NotNull

    private String description;

    private LocalDate leaveEndDate;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;



    public double getLeaveDays() {
        if (leaveStartDate != null && leaveEndDate != null && leaveType.equals(LeaveType.YILLIK)) {
            double days = 0;


            LocalDate date = leaveStartDate;
            while (!date.isAfter(leaveEndDate)) {
                if (date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                    days++;
                }
                date = date.plusDays(1);
            }

            // "YARIM GÜN" veya "yarım gün" ifadesini kontrol edin
            if (description != null && (description.contains("YARIM GÜN") || description.contains("yarım gün"))) {
                days -= 0.5; // Eğer varsa, 0,5 gün çıkar
            }
            return days;
        }
        return 0;
    }

}
