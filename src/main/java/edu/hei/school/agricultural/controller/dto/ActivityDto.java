package edu.hei.school.agricultural.controller.dto;

import edu.hei.school.agricultural.entity.ActivityDayOfWeek;
import edu.hei.school.agricultural.entity.ActivityType;
import edu.hei.school.agricultural.entity.MemberOccupation;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class ActivityDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class CreateCollectivityActivity {
        private String label;
        private ActivityType activityType;
        private List<MemberOccupation> memberOccupationConcerned;
        private MonthlyRecurrenceRuleDto recurrenceRule;
        private LocalDate executiveDate;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class CollectivityActivityResponse {
        private String id;
        private String label;
        private ActivityType activityType;
        private List<MemberOccupation> memberOccupationConcerned;
        private MonthlyRecurrenceRuleDto recurrenceRule;
        private LocalDate executiveDate;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class MonthlyRecurrenceRuleDto {
        private Integer weekOrdinal;
        private ActivityDayOfWeek dayOfWeek;
    }
}
