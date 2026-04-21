package com.tantsaha.tantsaha.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class Member extends MemberInformation {
    private String id;
    private List<Member> referees;
}
