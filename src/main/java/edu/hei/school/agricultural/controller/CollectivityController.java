package edu.hei.school.agricultural.controller;

import edu.hei.school.agricultural.controller.dto.CollectivityInformation;
import edu.hei.school.agricultural.controller.dto.CreateCollectivity;
import edu.hei.school.agricultural.controller.dto.CreateMembershipFee;
import edu.hei.school.agricultural.controller.mapper.CollectivityDtoMapper;
import edu.hei.school.agricultural.controller.mapper.FinancialAccountDtoMapper;
import edu.hei.school.agricultural.controller.mapper.MembershipFeeDtoMapper;
import edu.hei.school.agricultural.controller.mapper.TransactionDtoMapper;
import edu.hei.school.agricultural.entity.Collectivity;
import edu.hei.school.agricultural.entity.MembershipFee;
import edu.hei.school.agricultural.exception.BadRequestException;
import edu.hei.school.agricultural.exception.NotFoundException;
import edu.hei.school.agricultural.service.CollectivityService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class CollectivityController {
    private final CollectivityDtoMapper collectivityDtoMapper;
    private final MembershipFeeDtoMapper membershipFeeDtoMapper;
    private final CollectivityService collectivityService;
    private final FinancialAccountDtoMapper financialAccountDtoMapper;
    private final TransactionDtoMapper transactionDtoMapper;

    @Value("${api.key}")
    private String apiKey;

    @GetMapping("/collectivities/{id}")
    public ResponseEntity<?> getCollectivityById(@PathVariable String id, HttpServletRequest request) {
        try {
            if (!apiKey.equals(request.getHeader("x-api-key"))) {
                return ResponseEntity.status(401).body("Bad credentials");
            }
            return ResponseEntity.status(OK).body(collectivityDtoMapper.mapToDto(collectivityService.getCollectivityById(id)));
        } catch (BadRequestException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }

    }

    @PostMapping("/collectivities")
    public ResponseEntity<?> createCollectivity(@RequestBody List<CreateCollectivity> createCollectivities, HttpServletRequest request) {
        try {
            if (!apiKey.equals(request.getHeader("x-api-key"))) {
                return ResponseEntity.status(401).body("Bad credentials");
            }
            List<Collectivity> collectivities = createCollectivities.stream()
                    .map(collectivityDtoMapper::mapToEntity)
                    .toList();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(collectivityService.createCollectivities(collectivities).stream()
                            .map(collectivityDtoMapper::mapToDto)
                            .toList());
        } catch (
                BadRequestException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(e.getMessage());
        } catch (
                NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/collectivities/{id}/informations")
    public ResponseEntity<?> updateCollectivityInformation(@PathVariable String id,
                                                           @RequestBody CollectivityInformation collectivityInformation,
                                                           HttpServletRequest request
                                                        ) {
        String name = collectivityInformation.getName();
        Integer number = collectivityInformation.getNumber();
        try {
            if (!apiKey.equals(request.getHeader("x-api-key"))) {
                return ResponseEntity.status(401).body("Bad credentials");
            }
            return ResponseEntity.status(OK)
                    .body(collectivityDtoMapper.mapToDto(collectivityService.updateInformations(id, name, number)));
        } catch (BadRequestException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/collectivities/{id}/membershipFees")
    public ResponseEntity<?> getCollectivityMembershipFeesByCollectivity(@PathVariable String id, HttpServletRequest request) {
        try {
            if (!apiKey.equals(request.getHeader("x-api-key"))) {
                return ResponseEntity.status(401).body("Bad credentials");
            }
            return ResponseEntity.status(OK)
                    .body(collectivityService.getMembershipFeesByCollectivityIdentifier(id).stream()
                            .map(membershipFeeDtoMapper::mapToDto)
                            .toList());
        } catch (BadRequestException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/collectivities/{id}/membershipFees")
    public ResponseEntity<?> createCollectivityMembershipFee(
            @PathVariable String id,
            @RequestBody List<CreateMembershipFee> membershipFees,
            HttpServletRequest request
        ) {
        try {
            if (!apiKey.equals(request.getHeader("x-api-key"))) {
                return ResponseEntity.status(401).body("Bad credentials");
            }
            List<MembershipFee> membershipFeesToCreate = membershipFees.stream()
                    .map(membershipFeeDtoMapper::mapToEntity)
                    .toList();
            return ResponseEntity.status(OK)
                    .body(collectivityService.createMembershipFees(id, membershipFeesToCreate).stream()
                            .map(membershipFeeDtoMapper::mapToDto)
                            .toList());
        } catch (BadRequestException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/collectivities/{id}/financialAccounts")
    public ResponseEntity<?> getCollectivityFinancialAccounts(@PathVariable String id,
                                                              @RequestParam(required = false) LocalDate at,
                                                            HttpServletRequest request) {
        try {
            if (!apiKey.equals(request.getHeader("x-api-key"))) {
                return ResponseEntity.status(401).body("Bad credentials");
            }
            return ResponseEntity.status(OK)
                    .body(collectivityService.getFinancialAccounts(id).stream()
                            .map(financialAccount -> financialAccountDtoMapper.mapToDto(financialAccount, at))
                            .toList());
        } catch (BadRequestException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/collectivities/{id}/transactions")
    public ResponseEntity<?> getCollectivityTransactions(@PathVariable String id, @RequestParam LocalDate from, @RequestParam LocalDate to, HttpServletRequest request) {
        try {
            if (!apiKey.equals(request.getHeader("x-api-key"))) {
                return ResponseEntity.status(401).body("Bad credentials");
            }
            return ResponseEntity.status(OK)
                    .body(collectivityService.getTransactionsByCollectivity(id, from, to).stream()
                            .map(transactionDtoMapper::mapToDto)
                            .toList());
        } catch (BadRequestException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

  @GetMapping("/collectivities/statistics")
    public ResponseEntity<?> getCollectiesStats(@RequestParam(required = false) LocalDate from, @RequestParam(required = false) LocalDate to, HttpServletRequest request) {
        try {
            if (!apiKey.equals(request.getHeader("x-api-key"))) {
                return ResponseEntity.status(401).body("Bad credentials");
            }
            return ResponseEntity.status(OK)
                    .body(collectivityService.findGlobalStats(from, to));
        } catch (BadRequestException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}
