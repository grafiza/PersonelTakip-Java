package com.personel.PersonelTakip.controller;

import com.personel.PersonelTakip.entity.Leave;
import com.personel.PersonelTakip.service.Impl.LeaveService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/leave")
public class LeaveController {
    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @GetMapping
    ResponseEntity<Page<Leave>> getLeaves(@RequestParam(defaultValue = "0") Integer page,
                                          @RequestParam(defaultValue = "10") Integer pageSize) {
        return ResponseEntity.ok(leaveService.getAll(PageRequest.of(page, pageSize, Sort.by("id"))));
    }
    @GetMapping("/{id}")
    ResponseEntity<Leave> getLeaveById(@PathVariable Long id) {
        return ResponseEntity.ok(leaveService.getById(id));
    }


    @PostMapping
    public ResponseEntity<Leave> saveLeave(@RequestBody Leave leave){
        return ResponseEntity.ok(leaveService.save(leave));

    }
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteById(@PathVariable Long id) {
        leaveService.delete(id);
        return ResponseEntity.ok().build();
    }
}
