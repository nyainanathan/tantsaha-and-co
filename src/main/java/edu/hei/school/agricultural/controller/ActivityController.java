package edu.hei.school.agricultural.controller;

import edu.hei.school.agricultural.controller.dto.ActivityDto;
import edu.hei.school.agricultural.controller.dto.AttendanceCreation;
import edu.hei.school.agricultural.controller.mapper.ActivityDtoMapper;
import edu.hei.school.agricultural.exception.BadRequestException;
import edu.hei.school.agricultural.exception.NotFoundException;
import edu.hei.school.agricultural.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;
    private final ActivityDtoMapper activityDtoMapper;

    @PostMapping("/collectivities/{id}/activities")
    public ResponseEntity<?> createActivities(@PathVariable String id,
                                              @RequestBody List<ActivityDto.CreateCollectivityActivity> createActivities) {
        try {
            return ResponseEntity.status(OK)
                    .body(activityService.createActivities(id,
                                    createActivities.stream()
                                            .map(activityDtoMapper::mapToEntity)
                                            .toList())
                            .stream()
                            .map(activityDtoMapper::mapToDto)
                            .toList());
        } catch (BadRequestException e) {
            return ResponseEntity.status(BAD_REQUEST).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/collectivities/{id}/activities")
    public ResponseEntity<?> getActivities(@PathVariable String id) {
        try {
            return ResponseEntity.status(OK)
                    .body(activityService.getActivities(id).stream()
                            .map(activityDtoMapper::mapToDto)
                            .toList());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/collectivities/{id}/activities{activityId}/attendance")
    public ResponseEntity<?> saveAttendance(
        @PathVariable(name = "id") String collectivityId,
        @PathVariable(name = "activityId") String activityId,
        @RequestBody List<AttendanceCreation> toSave
    ){
        try {
            return ResponseEntity.status(CREATED)
                    .body(
                        activityService.saveAttendance(collectivityId, activityId, toSave)
                    );
        } catch (BadRequestException e) {
            return ResponseEntity.status(BAD_REQUEST).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/collectivities/{id}/activities{activityId}/attendance")
    public ResponseEntity<?> findAttendance(
        @PathVariable(name = "id") String collectivityId,
        @PathVariable(name = "activityId") String activityId
    ){
        try {
            return ResponseEntity.status(CREATED)
                    .body(
                        activityService.findAttendance(collectivityId, activityId)
                    );
        } catch (BadRequestException e) {
            return ResponseEntity.status(BAD_REQUEST).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}