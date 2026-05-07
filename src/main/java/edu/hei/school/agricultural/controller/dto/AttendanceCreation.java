package edu.hei.school.agricultural.controller.dto;

import lombok.Getter;

@Getter
public class AttendanceCreation {
    private String memberIdentifier;
    private AttendanceStatus attendanceStatus;
}
