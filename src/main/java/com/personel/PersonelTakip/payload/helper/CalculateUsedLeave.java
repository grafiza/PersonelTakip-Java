package com.personel.PersonelTakip.payload.helper;


import com.personel.PersonelTakip.entity.Leave;

import java.time.LocalDate;
import java.util.List;

public class CalculateUsedLeave {
    private Long calculateUsedLeave(List<Leave> leaveList) {
        LocalDate twoYearsAgo = LocalDate.now().minusYears(2);
        return leaveList.stream()
                .filter(leave -> leave.getLeaveEndDate().isAfter(twoYearsAgo))
                .mapToLong(Leave::getLeaveDays)
                .sum();
    }
}
