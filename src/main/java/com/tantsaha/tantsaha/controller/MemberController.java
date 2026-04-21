package com.tantsaha.tantsaha.controller;

import com.tantsaha.tantsaha.entity.Member;
import com.tantsaha.tantsaha.repository.MemberRepository;
import com.tantsaha.tantsaha.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
public class MemberController {
    private MemberService memberService;
    @PostMapping("/members")
    public List<Member> createMember(@RequestBody List<Member> members) {
        return memberService.createListMembers(members);
    }
}
