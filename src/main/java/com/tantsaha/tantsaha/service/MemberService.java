package com.tantsaha.tantsaha.service;

import com.tantsaha.tantsaha.DTO.CreateMember;
import com.tantsaha.tantsaha.entity.member.Member;
import com.tantsaha.tantsaha.entity.member.MemberHistory;
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

    public Member save(CreateMember toSave){
        String createdMember = this.memberRepository.create(toSave);
        memberRepository.attachMember(createdMember, toSave.getCollectivityIdentifier(), toSave.getOccupation());
        for(String mentoring : toSave.getReferees()){
            this.memberRepository.mentor(mentoring, createdMember);
        }
        return this.memberRepository.findById(createdMember);
    }

    public List<Member> saveAll(List<CreateMember> toSave){

        List<Member> members = new ArrayList<>();

        for(CreateMember member : toSave){
            members.add(
                    save(member)
            );
        }

        return members;
    }
}
