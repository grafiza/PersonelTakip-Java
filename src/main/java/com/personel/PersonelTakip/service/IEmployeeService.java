package com.personel.PersonelTakip.service;

import com.personel.PersonelTakip.entity.Employee;
import com.personel.PersonelTakip.entity.Duty;

import java.util.List;

public interface IEmployeeService extends IService<Employee> {
    List<Employee> getUserByDuty(Duty duty);

}
