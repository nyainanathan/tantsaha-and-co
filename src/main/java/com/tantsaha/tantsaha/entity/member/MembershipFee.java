package com.tantsaha.tantsaha.entity.member;

import com.tantsaha.tantsaha.DTO.CreateMembershipFee;
import com.tantsaha.tantsaha.enums.ActivityStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MembershipFee extends CreateMembershipFee {
    private String id;
    private ActivityStatus status;
}
