package com.personel.PersonelTakip.helpers.requests;


import com.personel.PersonelTakip.entity.LeaveType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class IzinRequest {
    private Long personelId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LeaveType leaveType;
    private String description;
}
