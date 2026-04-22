package com.tantsaha.tantsaha.service;

import com.tantsaha.tantsaha.DTO.CreateMembershipFee;

import com.tantsaha.tantsaha.entity.member.MembershipFee;
import com.tantsaha.tantsaha.enums.ActivityStatus;
import com.tantsaha.tantsaha.repository.MembershipFeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MembershipFeeService {

    private final MembershipFeeRepository repository;

    public MembershipFeeService(MembershipFeeRepository repository) {
        this.repository = repository;
    }

    public List<MembershipFee> getFees(String collectivityId) {
        return repository.getByCollectivityId(collectivityId);
    }

    public List<MembershipFee> createFees(String collectivityId, List<CreateMembershipFee> dtos) {
        return null;
    }
}
