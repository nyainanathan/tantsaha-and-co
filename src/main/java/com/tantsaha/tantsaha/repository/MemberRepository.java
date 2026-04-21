package com.tantsaha.tantsaha.repository;

import com.tantsaha.tantsaha.entity.Member;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class MemberRepository {
    private final Map<Integer, Member> members =  new HashMap<>();
    public Member save(Member member) {
        members.put(member.getId(), member);
        return member;
    }
    public Member findById(Integer id) {
        return members.get(id);
    }
}
