package com.tantsaha.tantsaha.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class MemberHistory {
    private MemberOccupation role;
    private LocalDate startDate;
    private  LocalDate endDate;
}
