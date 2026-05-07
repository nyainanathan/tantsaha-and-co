package edu.hei.school.agricultural.controller.mapper;

import edu.hei.school.agricultural.controller.dto.ActivityDto;
import edu.hei.school.agricultural.entity.CollectivityActivity;
import edu.hei.school.agricultural.entity.MonthlyRecurrenceRule;
import org.springframework.stereotype.Component;

@Component
public class ActivityDtoMapper {

    public CollectivityActivity mapToEntity(ActivityDto.CreateCollectivityActivity dto) {
        MonthlyRecurrenceRule recurrenceRule = null;
        if (dto.getRecurrenceRule() != null) {
            recurrenceRule = MonthlyRecurrenceRule.builder()
                    .weekOrdinal(dto.getRecurrenceRule().getWeekOrdinal())
                    .dayOfWeek(dto.getRecurrenceRule().getDayOfWeek())
                    .build();
        }

        return CollectivityActivity.builder()
                .label(dto.getLabel())
                .activityType(dto.getActivityType())
                .memberOccupationConcerned(dto.getMemberOccupationConcerned())
                .recurrenceRule(recurrenceRule)
                .executiveDate(dto.getExecutiveDate())
                .build();
    }

    public ActivityDto.CollectivityActivityResponse mapToDto(CollectivityActivity entity) {
        ActivityDto.MonthlyRecurrenceRuleDto recurrenceRuleDto = null;
        if (entity.getRecurrenceRule() != null) {
            recurrenceRuleDto = ActivityDto.MonthlyRecurrenceRuleDto.builder()
                    .weekOrdinal(entity.getRecurrenceRule().getWeekOrdinal())
                    .dayOfWeek(entity.getRecurrenceRule().getDayOfWeek())
                    .build();
        }

        return ActivityDto.CollectivityActivityResponse.builder()
                .id(entity.getId())
                .label(entity.getLabel())
                .activityType(entity.getActivityType())
                .memberOccupationConcerned(entity.getMemberOccupationConcerned())
                .recurrenceRule(recurrenceRuleDto)
                .executiveDate(entity.getExecutiveDate())
                .build();
    }
}