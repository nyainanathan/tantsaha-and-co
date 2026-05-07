package edu.hei.school.agricultural.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecurrenceRule {
    private Integer weekOrdinal;
    private DayOfWeek dayOfWeek;
}
