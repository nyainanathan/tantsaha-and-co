package edu.hei.school.agricultural.controller.mapper;

import edu.hei.school.agricultural.controller.dto.CollectivityLocalStatDto;
import edu.hei.school.agricultural.entity.CollectivityLocalStatistic;
import edu.hei.school.agricultural.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class StatisticDtoMapper {
    public CollectivityLocalStatDto mapToDto(CollectivityLocalStatistic entity) {
        Member md = entity.getMemberDescription();
        return CollectivityLocalStatDto.builder()
                .memberDescription(CollectivityLocalStatDto.MemberDescriptionDto.builder()
                        .id(md.getId())
                        .firstName(md.getFirstName())
                        .lastName(md.getLastName())
                        .email(md.getEmail())
                        .occupation(String.valueOf(md.getOccupation()))
                        .build())
                .earnedAmount(entity.getEarnedAmount())
                .unpaidAmount(entity.getUnpaidAmount())
                .assiduityPercentage(entity.getAssiduityPercentage())
                .build();
    }
}
