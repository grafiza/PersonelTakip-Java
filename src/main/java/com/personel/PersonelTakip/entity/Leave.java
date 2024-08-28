package com.personel.PersonelTakip.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
    public class Leave extends BaseEntity{
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

        public int getLeaveDays() {
            if (leaveStartDate != null && leaveEndDate != null && leaveType.equals(LeaveType.YILLIK)) {
                return (int) ChronoUnit.DAYS.between(leaveStartDate, leaveEndDate.plusDays(1));
            }
            return 0;
        }
    }
