package com.personel.PersonelTakip.controller;

import com.personel.PersonelTakip.entity.Employee;
import com.personel.PersonelTakip.entity.Duty;
import com.personel.PersonelTakip.service.Impl.EmployeeService;
import com.personel.PersonelTakip.entity.Duty;
import com.personel.PersonelTakip.entity.Employee;
import com.personel.PersonelTakip.service.Impl.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    ResponseEntity<Page<Employee>> getEmployees(@RequestParam(defaultValue = "0") Integer page,
                                                @RequestParam(defaultValue = "50") Integer pageSize) {
        return ResponseEntity.ok(employeeService.getAll(PageRequest.of(page, pageSize, Sort.by("id"))));
    }

    @GetMapping("/duty")
    ResponseEntity<List<Employee>> getEmployees(@RequestParam Duty duty) {
        return ResponseEntity.ok(employeeService.getUserByDuty(duty));
    }

    @GetMapping("/{id}")
    ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getById(id));
    }

    @PostMapping
    ResponseEntity<Employee> createEmployee(@RequestBody Employee employee){
        return ResponseEntity.ok(employeeService.save(employee));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteById(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/edit/{id}")
    ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee){
        return ResponseEntity.ok(employeeService.update(employee));
    }

}
