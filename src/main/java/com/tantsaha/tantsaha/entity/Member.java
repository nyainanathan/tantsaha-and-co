package com.tantsaha.tantsaha.entity;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Member extends MemberInformation {
    private String id;
    private List<String> referees;
}
