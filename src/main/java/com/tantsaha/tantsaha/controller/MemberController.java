package com.tantsaha.tantsaha.controller;

import com.tantsaha.tantsaha.DTO.CreateMember;
import com.tantsaha.tantsaha.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
public class MemberController {

    private MemberService memberService;

    @PostMapping("/members")
    public ResponseEntity<?> saveAll(
            @RequestBody List<CreateMember> toSave
    ) {
        try {
            return ResponseEntity.status(201)
                    .header("Content-Type", "application/json")
                    .body(this.memberService.saveAll(toSave));
        } catch (Exception e){
            return ResponseEntity.status(500)
                    .header("Content-Type", "text/plain")
                    .body(e.getMessage());
        }
    }
}
