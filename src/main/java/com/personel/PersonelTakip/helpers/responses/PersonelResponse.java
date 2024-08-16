package com.personel.PersonelTakip.helpers.responses;

import com.personel.PersonelTakip.entity.Department;
import com.personel.PersonelTakip.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class PersonelResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String tc;
    private LocalDate startDate;
    private LocalDate endDate;
    private Department department;
    private Role role;
    private int leaveRights;
}
