package com.personel.PersonelTakip.payload.helper;

import com.personel.PersonelTakip.entity.Leave;
import com.personel.PersonelTakip.entity.LeaveType;
import com.personel.PersonelTakip.payload.helper.HolidayLoader;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

public class LeaveDaysCalculator {

    public double calculateLeaveDays(Leave leave) {
        if (leave.getLeaveStartDate() != null && leave.getLeaveEndDate() != null && leave.getLeaveType().equals(LeaveType.YILLIK)) {
            double days = 0;

            try {
                HolidayLoader holidayLoader = new HolidayLoader("src/main/resources/data/LeaveDays.json", leave.getLeaveStartDate().getYear());
                Set<LocalDate> holidays = holidayLoader.getHolidays();

                LocalDate date = leave.getLeaveStartDate();
                while (!date.isAfter(leave.getLeaveEndDate())) {
                    if (date.getDayOfWeek() != DayOfWeek.SUNDAY && !holidays.contains(date)) {
                        days++;
                    }
                    date = date.plusDays(1);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // "YARIM GÜN" veya "yarım gün" ifadesini kontrol edin
            if (leave.getDescription() != null && (leave.getDescription().contains("YARIM GÜN") || leave.getDescription().contains("yarım gün"))) {
                days -= 0.5; // Eğer varsa, 0,5 gün çıkar
            }

            return days;
        }
        return 0;
    }
}