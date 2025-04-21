package io.bvb.smarthealthcare.backend.controller;

import io.bvb.smarthealthcare.backend.model.StringResponse;
import io.bvb.smarthealthcare.backend.service.AdminService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(final AdminService adminService) {
        this.adminService = adminService;
    }

    @PutMapping("/approve-doctor/{doctorId}")
    public ResponseEntity<StringResponse> approveDoctor(@PathVariable Long doctorId) {
        adminService.approveDoctor(doctorId);
        return ResponseEntity.ok(new StringResponse("Doctor approved successfully"));
    }

    @PutMapping("/reject-doctor/{doctorId}")
    public ResponseEntity<StringResponse> rejectDoctor(@PathVariable Long doctorId) {
        adminService.rejectDoctor(doctorId);
        return ResponseEntity.ok().body(new StringResponse("Doctor rejected"));
    }
}
