package com.tantsaha.tantsaha.service;

import com.tantsaha.tantsaha.entity.Member;
import com.tantsaha.tantsaha.entity.MemberHistory;
import com.tantsaha.tantsaha.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public long getSeniority(String memberId){
        // in days
        List<MemberHistory> history = this.memberRepository.getMemberHistory(memberId);
        long seniorityInDays = 0;

        for(MemberHistory entry : history){
            seniorityInDays += ChronoUnit.DAYS.between(entry.getStartDate(), entry.getEndDate());
        }

        return seniorityInDays;
    }
}
