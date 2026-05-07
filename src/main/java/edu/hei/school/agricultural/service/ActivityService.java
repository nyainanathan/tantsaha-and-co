package edu.hei.school.agricultural.service;

import edu.hei.school.agricultural.controller.dto.ActivityMemberAttendance;
import edu.hei.school.agricultural.controller.dto.AttendanceCreation;
import edu.hei.school.agricultural.controller.dto.AttendanceStatus;
import edu.hei.school.agricultural.entity.Collectivity;
import edu.hei.school.agricultural.entity.CollectivityActivity;
import edu.hei.school.agricultural.entity.Member;
import edu.hei.school.agricultural.exception.BadRequestException;
import edu.hei.school.agricultural.exception.NotFoundException;
import edu.hei.school.agricultural.repository.ActivityRepository;
import edu.hei.school.agricultural.repository.CollectivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.UUID.randomUUID;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final CollectivityRepository collectivityRepository;

    public List<CollectivityActivity> createActivities(String collectivityId,
                                                              List<CollectivityActivity> activities) {
        collectivityRepository.findById(collectivityId)
                .orElseThrow(() -> new NotFoundException("Collectivity.id= " + collectivityId + " not found"));

        for (CollectivityActivity activity : activities) {
            if (activity.getRecurrenceRule() != null && activity.getExecutiveDate() != null) {
                throw new BadRequestException(
                        "Activity cannot have both recurrenceRule and executiveDate at the same time");
            }
            activity.setId(randomUUID().toString());
            activity.setCollectivityId(collectivityId);
        }

        return activityRepository.saveAll(activities);
    }

    public List<CollectivityActivity> getActivities(String collectivityId) {
        collectivityRepository.findById(collectivityId)
                .orElseThrow(() -> new NotFoundException("Collectivity.id= " + collectivityId + " not found"));

        return activityRepository.findAllByCollectivityId(collectivityId);
    }

    @Transactional
    public List<ActivityMemberAttendance> saveAttendance(String collectivityId, String activityId, List<AttendanceCreation> toSave){

        collectivityRepository.findById(collectivityId)
                        .orElseThrow(() -> new NotFoundException("Collectivity.id= " + collectivityId + " not found"));

        activityRepository.findById(activityId)
        .orElseThrow(() -> new NotFoundException("Activity.id= " + activityId + " not found"));

        List<String> createdEntries = new ArrayList<>();

        for(AttendanceCreation create : toSave){
            ActivityMemberAttendance attendance = activityRepository.findAttendance(activityId, create.getMemberIdentifier());

            if(attendance == null){
                String createdAttendance = activityRepository.saveAttendance(create, activityId);
                createdEntries.add(createdAttendance);
            } else {
                if(attendance.getAttendanceStatus() == AttendanceStatus.UNDEFINED){
                    activityRepository.updateAttendanceStatus(attendance.getId(), create.getAttendanceStatus());
                    createdEntries.add(attendance.getId());
                } else {
                    throw new BadRequestException("You can only update an attendance with status UNDEFINED, here it is " + attendance.getAttendanceStatus());
                }
            }

        }

        List<ActivityMemberAttendance> attendances = new ArrayList<>();

        for(String entry : createdEntries){
            attendances.add(
                activityRepository.findAttendanceById(entry)
            );
        }


        return attendances;
        
    }
}
