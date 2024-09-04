package com.personel.PersonelTakip.controller;

import com.personel.PersonelTakip.entity.Employee;
import com.personel.PersonelTakip.entity.Leave;
import com.personel.PersonelTakip.service.Impl.LeaveService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leave")
public class LeaveController {
    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @GetMapping
    ResponseEntity<Page<Leave>> getLeaves(@RequestParam(defaultValue = "0") Integer page,
                                          @RequestParam(defaultValue = "50") Integer pageSize) {
        return ResponseEntity.ok(leaveService.getAll(PageRequest.of(page, pageSize, Sort.by("id"))));
    }

    @GetMapping("/getAllLeaves")
    ResponseEntity<List<Leave>> getAllLeaves(){
        return ResponseEntity.ok(leaveService.getAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<Leave> getLeaveById(@PathVariable Long id) {
        return ResponseEntity.ok(leaveService.getById(id));
    }

    @GetMapping("/employee/{id}")
    ResponseEntity<List<Leave>> getLeaveByEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(leaveService.getAllLeaveByEmployee(id));
    }


    @PostMapping
    public ResponseEntity<Leave> saveLeave(@RequestBody Leave leave){
        return ResponseEntity.ok(leaveService.save(leave));

    }
    @PutMapping("/edit/{id}")
    public ResponseEntity<Leave> updateLeave(@PathVariable Long id, @RequestBody Leave leave) {
        Leave updatedLeave = leaveService.update(id, leave);
        return ResponseEntity.ok(updatedLeave);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteById(@PathVariable Long id) {
        leaveService.delete(id);
        return ResponseEntity.ok().build();
    }
}
