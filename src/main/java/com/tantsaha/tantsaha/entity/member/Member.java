package com.tantsaha.tantsaha.entity.member;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Member extends MemberInformation {
    private String id;
    private List<String> referees;
}
