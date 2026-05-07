package edu.hei.school.agricultural.service;

import edu.hei.school.agricultural.entity.CollectivityActivity;
import edu.hei.school.agricultural.entity.Member;
import edu.hei.school.agricultural.exception.BadRequestException;
import edu.hei.school.agricultural.exception.NotFoundException;
import edu.hei.school.agricultural.repository.ActivityRepository;
import edu.hei.school.agricultural.repository.CollectivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.UUID.randomUUID;

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
}
