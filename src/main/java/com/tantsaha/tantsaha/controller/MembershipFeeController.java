package com.tantsaha.tantsaha.controller;

import com.tantsaha.tantsaha.DTO.CreateMembershipFee;
import com.tantsaha.tantsaha.entity.member.MembershipFee;
import com.tantsaha.tantsaha.service.MembershipFeeService;
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

    @GetMapping
    public List<MembershipFee> getMembershipFees(@PathVariable String id) {
        return service.getFees(id);
    }

    @PostMapping
    public List<MembershipFee> createMembershipFees(
            @PathVariable String id,
            @RequestBody List<CreateMembershipFee> fees
    ) {
        return service.createFees(id, fees);
    }
}
