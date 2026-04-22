package com.tantsaha.tantsaha.entity.member;

import com.tantsaha.tantsaha.enums.Gender;
import com.tantsaha.tantsaha.enums.MemberOccupation;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class MemberInformation {
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Gender gender;
    private String address;
    private String profession;
    private Integer phoneNumber;
    private String email;
    private MemberOccupation occupation;
}
