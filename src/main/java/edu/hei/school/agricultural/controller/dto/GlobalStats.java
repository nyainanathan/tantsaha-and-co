package edu.hei.school.agricultural.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GlobalStats {
    private CollectivityInformation collectivityInformation;
    private Integer newMembersNumber;
    private Double overallMemberCurrentDuePercentage;
    private Double overallMemberAssiduityPercentage;
}
