package edu.hei.school.agricultural.controller.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateActivity {
    private String label;
    private ActivityType activityType;
    private List<MemberOccupation> memberOccupationConcerned;
    private LocalDate executiveDate;
    private RecurrenceRule recurrenceRule;
}
