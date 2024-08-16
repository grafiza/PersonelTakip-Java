package com.personel.PersonelTakip.helpers.responses;

import com.personel.PersonelTakip.entity.LeaveType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class IzinResponse {
    private Long id;
    private Long personelId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LeaveType leaveType;
    private String description;
}
