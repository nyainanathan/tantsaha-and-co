package com.tantsaha.tantsaha.DTO;

import com.tantsaha.tantsaha.entity.member.MemberInformation;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateMember extends MemberInformation {
    private String collectivityIdentifier;
    private List<String> referees;
    private Boolean registrationFeePaid;
    private Boolean membershipDuesPaid;
}
