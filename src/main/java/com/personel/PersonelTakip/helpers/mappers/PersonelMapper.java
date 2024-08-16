package com.personel.PersonelTakip.helpers.mappers;


import com.personel.PersonelTakip.helpers.requests.PersonelRequest;
import com.personel.PersonelTakip.helpers.responses.PersonelResponse;
import com.personel.PersonelTakip.entity.Personel;
import org.springframework.stereotype.Component;

@Component
public class PersonelMapper {

    public Personel toEntity(PersonelRequest personelRequest) {
        return Personel.builder()
                .firstName(personelRequest.getFirstName())
                .lastName(personelRequest.getLastName())
                .email(personelRequest.getEmail())
                .phoneNumber(personelRequest.getPhoneNumber())
                .tc(personelRequest.getTc())
                .startDate(personelRequest.getStartDate())
                .endDate(personelRequest.getEndDate())
                .department(personelRequest.getDepartment())
                .role(personelRequest.getRole())
                .build();
    }

    public PersonelResponse toResponse(Personel personel) {
        return PersonelResponse.builder()
                .id(personel.getId())
                .firstName(personel.getFirstName())
                .lastName(personel.getLastName())
                .email(personel.getEmail())
                .phoneNumber(personel.getPhoneNumber())
                .tc(personel.getTc())
                .startDate(personel.getStartDate())
                .endDate(personel.getEndDate())
                .department(personel.getDepartment())
                .role(personel.getRole())
                .leaveRights(personel.getLeaveRights()) // Yeni alan map'leniyor
                .build();
    }
}
