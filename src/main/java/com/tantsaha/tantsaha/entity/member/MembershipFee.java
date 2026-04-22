package com.tantsaha.tantsaha.entity.member;

import com.tantsaha.tantsaha.enums.ActivityStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MembershipFee {
    private int id;
    private ActivityStatus status;
}
