package com.tantsaha.tantsaha.service;

import com.tantsaha.tantsaha.entity.Member;
import com.tantsaha.tantsaha.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    public List<Member> createListMembers(List<Member> members) {
        List<Member> newMembers = new ArrayList<>();
        for (Member member : members) {
            validate (member);
//            member.setId();
//            memberRepository.save(member);
            newMembers.add(member);
        }
        return newMembers;
    }
    private void validate(Member m) {

        if (m.getRegistrationFeePaid() == null || !m.getRegistrationFeePaid()
                || m.getMembershipDuesPaid() == null || !m.getMembershipDuesPaid()) {
            throw new RuntimeException("Membership dues not paid or registration fee not paid.");
        }

        if (m.getReferees() == null || m.getReferees().size() < 2) {
            throw new RuntimeException("Member with bad referees.");
        }

//        if (!collectivityRepository.existsById(m.getCollectivityIdentifier())) {
//            throw new RuntimeException("Collectivity not found.");
//        }
    }
}
