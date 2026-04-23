package com.tantsaha.tantsaha.controller;

import com.tantsaha.tantsaha.DTO.CreateMembershipFee;
import com.tantsaha.tantsaha.entity.member.MembershipFee;
import com.tantsaha.tantsaha.service.MembershipFeeService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
public class MembershipFeeController {

    private final MembershipFeeService service;

    @GetMapping("/collectivities/{id}/membershipFees")
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

    @PostMapping("/collectivities/{id}/membershipFees")
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
