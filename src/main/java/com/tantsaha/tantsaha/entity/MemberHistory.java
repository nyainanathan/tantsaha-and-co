package com.tantsaha.tantsaha.entity;

import lombok.AllArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
public class MemberHistory {
    private MemberOccupation role;
    private LocalDate startDate;
    private  LocalDate endDate;
}
