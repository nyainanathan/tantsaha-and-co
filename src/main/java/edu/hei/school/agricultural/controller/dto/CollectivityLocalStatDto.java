package edu.hei.school.agricultural.controller.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CollectivityLocalStatDto {

    private MemberDescriptionDto memberDescription;
    private Double earnedAmount;
    private Double unpaidAmount;
    private Double assiduityPercentage;

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class MemberDescriptionDto {
        private String id;
        private String firstName;
        private String lastName;
        private String email;
        private String occupation;
    }
}

