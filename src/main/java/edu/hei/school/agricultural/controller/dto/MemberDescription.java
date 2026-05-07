package edu.hei.school.agricultural.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDescription {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private MemberOccupation occupation;
}
