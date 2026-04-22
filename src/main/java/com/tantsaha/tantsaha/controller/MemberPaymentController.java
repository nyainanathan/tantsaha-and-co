package com.tantsaha.tantsaha.controller;

import com.tantsaha.tantsaha.DTO.CreateMemberPayment;
import com.tantsaha.tantsaha.DTO.CreateMembershipFee;
import com.tantsaha.tantsaha.service.MemberPaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class MemberPaymentController {
    private final MemberPaymentService memberPaymentService;

    @PostMapping("/members/{id}/payments")
    public ResponseEntity<?> createPayment(
            @PathVariable String id,
            @RequestBody List<CreateMemberPayment> payments
    ) {
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .header("Content-Type", "application/json")
                    .body(memberPaymentService.createPayments(id, payments));
        } catch (Exception e){
            return ResponseEntity
                    .status(500)
                    .header("Content-Type", "text/plain")
                    .body(e.getMessage());
        }
    }
}
