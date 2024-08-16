package com.personel.PersonelTakip.helpers.mappers;


import com.personel.PersonelTakip.entity.Izin;
import com.personel.PersonelTakip.entity.Personel;
import com.personel.PersonelTakip.helpers.requests.IzinRequest;
import com.personel.PersonelTakip.helpers.responses.IzinResponse;
import org.springframework.stereotype.Component;

@Component
public class IzinMapper {

    public IzinResponse toResponse(Izin izin) {
        if (izin == null) {
            return null;
        }

        return IzinResponse.builder()
                .id(izin.getId())
                .personelId(izin.getPersonel().getId())
                .startDate(izin.getStartDate())
                .endDate(izin.getEndDate())
                .leaveType(izin.getLeaveType())
                .description(izin.getDescription())
                .build();
    }

    public Izin toEntity(IzinRequest request, Personel personel) {
        if (request == null || personel == null) {
            return null;
        }

        return Izin.builder()
                .personel(personel)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .leaveType(request.getLeaveType())
                .description(request.getDescription())
                .build();
    }
}
