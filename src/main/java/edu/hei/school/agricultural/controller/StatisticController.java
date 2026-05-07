package edu.hei.school.agricultural.controller;

import edu.hei.school.agricultural.controller.mapper.StatisticDtoMapper;
import edu.hei.school.agricultural.exception.BadRequestException;
import edu.hei.school.agricultural.exception.NotFoundException;
import edu.hei.school.agricultural.service.StatisticService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import static org.springframework.http.HttpStatus.*;

@RestController

public class StatisticController {

    private final StatisticService statisticsService;
    private final StatisticDtoMapper statisticsDtoMapper;

    @Value("${api.key}")
    private String apiKey;

    public StatisticController(StatisticService statisticsService, StatisticDtoMapper statisticsDtoMapper) {
        this.statisticsService = statisticsService;
        this.statisticsDtoMapper = statisticsDtoMapper;
    }

    @GetMapping("/collectivities/{id}/statistics")
    public ResponseEntity<?> getLocalStatistics(@PathVariable String id,
                                                @RequestParam LocalDate from,
                                                @RequestParam LocalDate to,
                                            HttpServletRequest request) {
        try {
            
            if (!apiKey.equals(request.getHeader("x-api-key"))) {
                return ResponseEntity.status(401).body("Bad credentials");
            }
            return ResponseEntity.status(OK)
                    .body(statisticsService.getLocalStatistics(id, from, to).stream()
                            .map(statisticsDtoMapper::mapToDto)
                            .toList());
        } catch (BadRequestException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}

