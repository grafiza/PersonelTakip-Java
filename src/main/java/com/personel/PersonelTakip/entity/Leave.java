package com.personel.PersonelTakip.entity;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.personel.PersonelTakip.payload.helper.HolidayLoader;
import com.personel.PersonelTakip.payload.helper.LeaveDaysCalculator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;

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

    @NotNull
    private LocalDate leaveStartDate;
    @NotNull

        private LocalDate leaveEndDate;

    private String description;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;



    public double getLeaveDays() {
        LeaveDaysCalculator calculator = new LeaveDaysCalculator();
        return calculator.calculateLeaveDays(this);
    }


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
}
