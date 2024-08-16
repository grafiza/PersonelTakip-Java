package com.personel.PersonelTakip.helpers.requests;


import com.personel.PersonelTakip.entity.Department;
import com.personel.PersonelTakip.entity.Role;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonelRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String tc;
    private LocalDate startDate;
    private LocalDate endDate;
    private Department department;
    private Role role;
}
