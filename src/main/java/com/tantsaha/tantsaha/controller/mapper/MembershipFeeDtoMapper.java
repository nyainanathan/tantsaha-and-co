package com.tantsaha.tantsaha.controller.mapper;

import com.tantsaha.tantsaha.controller.dto.ActivityStatus;
import com.tantsaha.tantsaha.controller.dto.CreateMembershipFee;
import com.tantsaha.tantsaha.controller.dto.Frequency;
import com.tantsaha.tantsaha.controller.dto.MembershipFee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MembershipFeeDtoMapper {
    public MembershipFee mapToDto(com.tantsaha.tantsaha.entity.MembershipFee membershipFee) {
        return MembershipFee.builder()
                .id(membershipFee.getId())
                .label(membershipFee.getLabel())
                .amount(membershipFee.getAmount())
                .frequency(membershipFee.getFrequency() == null ? null : Frequency.valueOf(membershipFee.getFrequency().name()))
                .status(membershipFee.getStatus() == null ? null : ActivityStatus.valueOf(membershipFee.getStatus().name()))
                .eligibleFrom(membershipFee.getEligibleFrom())
                .build();
    }

    public com.tantsaha.tantsaha.entity.MembershipFee mapToEntity(CreateMembershipFee createMembershipFee) {
        return com.tantsaha.tantsaha.entity.MembershipFee.builder()
                .label(createMembershipFee.getLabel())
                .amount(createMembershipFee.getAmount())
                .frequency(createMembershipFee.getFrequency() == null ? null : com.tantsaha.tantsaha.entity.Frequency.valueOf(createMembershipFee.getFrequency().name()))
                .eligibleFrom(createMembershipFee.getEligibleFrom())
                .build();
    }
}
