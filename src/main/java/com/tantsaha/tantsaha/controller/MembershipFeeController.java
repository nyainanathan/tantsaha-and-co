package com.tantsaha.tantsaha.controller;

import com.tantsaha.tantsaha.DTO.CreateMembershipFee;
import com.tantsaha.tantsaha.entity.member.MembershipFee;
import com.tantsaha.tantsaha.service.MembershipFeeService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public class MembershipFeeController {

    private final MembershipFeeService service;

    public MembershipFeeController(MembershipFeeService service) {
        this.service = service;
    }

    @GetMapping("/collectivity/{id}/membershipFees")
    public ResponseEntity<?> getMembershipFees(@PathVariable String id)
    {
        try {
            return ResponseEntity
                    .status(200)
                    .header("Content-Type", "application/json")
                    .body(service.getFees(id));

        } catch (Exception e){
            return ResponseEntity
                    .status(500)
                    .header("Content-Type", "text/plain")
                    .body(e.getMessage());
        }
    }

    @PostMapping("/collectivity/{id}/membershipFees")
    public ResponseEntity<?> createFees(
            @PathVariable String id,
            @RequestBody List<CreateMembershipFee> fees
    )
    {
        try {
            return ResponseEntity
                    .status(201)
                    .header("Content-Type", "application/json")
                    .body(service.createFees(id, fees));

        } catch (Exception e){
            return ResponseEntity
                    .status(500)
                    .header("Content-Type", "text/plain")
                    .body(e.getMessage());
        }
    }
}
