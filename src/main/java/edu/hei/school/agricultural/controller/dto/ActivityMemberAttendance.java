package edu.hei.school.agricultural.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityMemberAttendance {
    private String id;
    private MemberInformation memberInformation;
    private AttendanceStatus attendanceStatus;
}
