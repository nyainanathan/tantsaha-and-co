package com.tantsaha.tantsaha.entity;

import lombok.Setter;

import java.util.List;

@Setter
public class CreateMember extends MemberInformation{
    private String collectivityIdentifier;
    private List<Member> referees;
    private Boolean registrationFeePaid;
    private Boolean membershipDuesPaid;
}
