package com.tantsaha.tantsaha.service;

import com.tantsaha.tantsaha.DTO.CreateMembershipFee;

import com.tantsaha.tantsaha.entity.member.MembershipFee;
import com.tantsaha.tantsaha.enums.ActivityStatus;
import com.tantsaha.tantsaha.repository.MembershipFeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Transactional
    public List<MembershipFee> createFees(String collectivityId, List<CreateMembershipFee> dtos) {
        List<String> createdFeesId = new ArrayList<>();

        for(CreateMembershipFee fee : dtos){

            System.out.println(fee);

            createdFeesId.add(
                    this.repository.save(fee, collectivityId)
            );

            System.out.println("After creation");
        }

        List<MembershipFee> savedFees = new ArrayList<>();

        for(String id : createdFeesId){

            System.out.println(id);
            savedFees.add(
                    this.repository.getById(id)
            );
        }

        return savedFees;
    }
}
