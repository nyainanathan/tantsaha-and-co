package edu.hei.school.agricultural.service;

import edu.hei.school.agricultural.entity.Collectivity;
import edu.hei.school.agricultural.entity.CollectivityLocalStatistic;
import edu.hei.school.agricultural.exception.BadRequestException;
import edu.hei.school.agricultural.exception.NotFoundException;
import edu.hei.school.agricultural.repository.CollectivityRepository;
import edu.hei.school.agricultural.repository.MemberRepository;
import edu.hei.school.agricultural.repository.StatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final CollectivityRepository collectivityRepository;
    private final MemberRepository memberRepository;
    private final StatisticRepository statisticRepository;

    public List<CollectivityLocalStatistic> getLocalStatistics(String collectivityId,
                                                               LocalDate from,
                                                               LocalDate to) {

        if(from == null || to == null){
                throw new BadRequestException("Mandatory query parameteres not provided or malformed");
        }

        Collectivity collectivity = collectivityRepository.findById(collectivityId)
                .orElseThrow(() -> new NotFoundException("Collectivity.id= " + collectivityId + " not found"));

        Map<String, Double> earnedByMember = statisticRepository.getEarnedAmountByMember(collectivityId, from, to);
        Map<String, Double> unpaidByMember = statisticRepository.getUnpaidAmountByMember(collectivityId, from, to);
        Map<String, Double> assiduityByMember = statisticRepository.getAssiduityPercentageByMember(collectivityId, from, to);

        return memberRepository.findAllByCollectivity(collectivity).stream()
                .map(member -> CollectivityLocalStatistic.builder()
                        .memberDescription(member)
                        .earnedAmount(earnedByMember.getOrDefault(member.getId(), 0.0))
                        .unpaidAmount(unpaidByMember.getOrDefault(member.getId(), 0.0))
                        .assiduityPercentage(assiduityByMember.getOrDefault(member.getId(), null))
                        .build())
                .toList();
    }
}