package com.tantsaha.tantsaha.repository;

import com.tantsaha.tantsaha.entity.member.MembershipFee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MembershipFeeRepository {
    private final Map<String, List<MembershipFee>> storage = new HashMap<>();

    public List<MembershipFee> findByCollectivityId(String collectivityId) {
        return storage.getOrDefault(collectivityId, new ArrayList<>());
    }

    public List<MembershipFee> saveAll(String collectivityId, List<MembershipFee> fees) {
        storage.putIfAbsent(collectivityId, new ArrayList<>());
        storage.get(collectivityId).addAll(fees);
        return fees;
    }
}
