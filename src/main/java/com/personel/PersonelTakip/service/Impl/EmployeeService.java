package com.personel.PersonelTakip.service.Impl;

import com.personel.PersonelTakip.common.GeneralException;
import com.personel.PersonelTakip.entity.*;
import com.personel.PersonelTakip.repository.EmployeeRepository;
import com.personel.PersonelTakip.repository.LeaveRepository;
import com.personel.PersonelTakip.service.IEmployeeService;
import com.personel.PersonelTakip.entity.Duty;
import com.personel.PersonelTakip.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService implements IEmployeeService {
    private final EmployeeRepository employeeRepository;
    private final LeaveRepository leaveRepository;

    public EmployeeService(EmployeeRepository employeeRepository, LeaveRepository leaveRepository, LeaveRepository le, LeaveRepository leaveRepository1) {
        this.employeeRepository = employeeRepository;


        this.leaveRepository = leaveRepository1;
    }

    @Override
    public List<Employee> getUserByDuty(Duty duty) {
        return employeeRepository.findAllByDuty(duty);
    }

    @Override
    public List<Employee> getAllActive() {

            return employeeRepository.findByStatus(Status.CALISIYOR);

    }

    @Override
    public Employee save(Employee employee) {
        if (employee.getId() == null) {
            if (employee.getSsnNumber() == null || employee.getSsnNumber().length() != 11) {
                throw new GeneralException("Geçersiz Kimlik Numarası");
            }
            if (employeeRepository.existsBySsnNumber(employee.getSsnNumber())) {
                throw new GeneralException("Bu Tc Kimlik Numarası Zaten Kayıtlı!");

            }
        }
        return employeeRepository.save(employee);
    }

    @Override
    public Employee getById(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new GeneralException("Böyle bir kullanıcı bulunamadı!"));

    }

    @Override
    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Page<Employee> getAll(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    @Override
    public Employee update(Employee employee) {
        // Mevcut çalışanı bul
        Employee foundEmployee = getById(employee.getId());

        // Çalışanın kimlik numarasının geçerli olup olmadığını kontrol et
        if (employee.getSsnNumber() == null || employee.getSsnNumber().length() != 11) {
            throw new GeneralException("Geçersiz Kimlik Numarası");
        }

        // Eğer kimlik numarası değişmemişse ve başka bir çalışan tarafından kullanılmıyorsa işlemi devam ettir
        if (!foundEmployee.getSsnNumber().equals(employee.getSsnNumber()) && employeeRepository.existsBySsnNumber(employee.getSsnNumber())) {
            throw new GeneralException("Bu TC Kimlik Numarası Zaten Kayıtlı!");
        }

        // Güncellenmiş çalışan bilgilerini kaydet
        return employeeRepository.save(employee);
    }

    @Override
    public void delete(Long id) {
        // Önce izin kayıtlarını güncelle veya sil
        List<Leave> leaveList = leaveRepository.findByEmployeeId(id);
        for (Leave leave : leaveList) {
            leave.setEmployee(null);  // Veya izinRepository.delete(izin); kullanabilirsiniz.
            leaveRepository.save(leave);
        }

        // Sonra çalışan kaydını sil
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
        } else {
            throw new GeneralException("Employee Kaydı Bulunamadı");
        }
    }


}
