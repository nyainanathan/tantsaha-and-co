package edu.hei.school.agricultural.service;

import edu.hei.school.agricultural.controller.dto.CollectivityInformation;
import edu.hei.school.agricultural.controller.dto.GlobalStats;
import edu.hei.school.agricultural.entity.*;
import edu.hei.school.agricultural.exception.BadRequestException;
import edu.hei.school.agricultural.exception.NotFoundException;
import edu.hei.school.agricultural.repository.CollectivityRepository;
import edu.hei.school.agricultural.repository.FinancialAccountRepository;
import edu.hei.school.agricultural.repository.MemberRepository;
import edu.hei.school.agricultural.repository.MembershipFeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static edu.hei.school.agricultural.entity.ActivityStatus.ACTIVE;
import static edu.hei.school.agricultural.entity.PaymentMode.*;
import static java.util.UUID.randomUUID;

@Service
@RequiredArgsConstructor
public class CollectivityService {
    private final CollectivityRepository collectivityRepository;
    private final MembershipFeeRepository membershipFeeRepository;
    private final FinancialAccountRepository financialAccountRepository;
    private final MemberRepository memberRepository;

    public List<Collectivity> createCollectivities(List<Collectivity> collectivities) {
        for (Collectivity collectivity : collectivities) {
            if (!collectivity.hasEnoughMembers()) {
                throw new BadRequestException("Collectivity must have at least 10 members, otherwise actual is " + collectivity.getMembers().size());
            }
            collectivity.setId(randomUUID().toString());
        }
        return collectivityRepository.saveAll(collectivities);
    }

    public Collectivity getCollectivityById(String id) {
        return collectivityRepository.findById(id).orElseThrow(() -> new NotFoundException("Collectivity.id= " + id + " not found"));
    }

    public Collectivity updateInformations(String collectivityId, String actualName, Integer actualNumber) {
        Collectivity collectivity = collectivityRepository.findById(collectivityId)
                .orElseThrow(() -> new NotFoundException("Collectivity.id= " + collectivityId + " not found"));
        if (actualNumber != null && collectivityRepository.isNumberExists(actualNumber)) {
            throw new BadRequestException("Collectivity.number=" + actualNumber + " already exists");
        }
        if (actualName != null && collectivityRepository.isNameExists(actualName)) {
            throw new BadRequestException("Collectivity.name=" + actualName + " already exists");
        }
        collectivity.setName(actualName);
        collectivity.setNumber(actualNumber);
        return collectivityRepository.saveAll(List.of((collectivity))).getFirst();
    }

    public List<MembershipFee> getMembershipFeesByCollectivityIdentifier(String collectivityIdentifier) {
        Collectivity collectivity = collectivityRepository.findById(collectivityIdentifier)
                .orElseThrow(() ->
                        new NotFoundException("Collectivity.id= " + collectivityIdentifier + " not found"));

        return membershipFeeRepository.getMembershipFeesByCollectivityId(collectivity.getId());
    }

    public List<MembershipFee> createMembershipFees(String collectivityIdentifier, List<MembershipFee> membershipFees) {
        Collectivity collectivity = collectivityRepository.findById(collectivityIdentifier)
                .orElseThrow(() ->
                        new NotFoundException("Collectivity.id= " + collectivityIdentifier + " not found"));
        for (MembershipFee membershipFee : membershipFees) {
            membershipFee.setId(randomUUID().toString());
            membershipFee.setStatus(ACTIVE);
            membershipFee.setCollectivityOwner(collectivity);
        }
        return membershipFeeRepository.saveAll(membershipFees);
    }

    public List<FinancialAccount> getFinancialAccounts(String collectivityIdentifier) {
        Collectivity collectivity = collectivityRepository.findById(collectivityIdentifier)
                .orElseThrow(() ->
                        new NotFoundException("Collectivity.id= " + collectivityIdentifier + " not found"));

        CashAccount cashAccount = financialAccountRepository.getCashAccountByCollectivityId(collectivity.getId());
        List<BankAccount> bankAccounts = financialAccountRepository.getBankAccountsByCollectivityId(collectivity.getId());
        List<MobileBankingAccount> mobileBankingAccountsByCollectivityId = financialAccountRepository.getMobileBankingAccountsByCollectivityId(collectivity.getId());

        return Stream.concat(
                Stream.concat(
                        Stream.of(cashAccount),
                        bankAccounts.stream()),
                mobileBankingAccountsByCollectivityId.stream()
        ).toList();
    }

    public List<CollectivityTransaction> getTransactionsByCollectivity(String collectivityIdentifier, LocalDate from, LocalDate to) {
        List<FinancialAccount> financialAccounts = getFinancialAccounts(collectivityIdentifier);

        return financialAccounts.stream()
                .map(financialAccount -> {
                    var transactionList = financialAccount.getTransactions().stream()
                            .filter(transaction -> (transaction.getCreationDate().isAfter(from) || transaction.getCreationDate().equals(from))
                                    && (transaction.getCreationDate().isBefore(to) || transaction.getCreationDate().equals(to)))
                            .toList();
                    var paymentMode = getPaymentMode(financialAccount);
                    return transactionList.stream()
                            .map(transaction -> {
                                CollectivityTransaction collectivityTransaction = CollectivityTransaction.builder()
                                        .id(transaction.getId())
                                        .type(transaction.getType())
                                        .amount(transaction.getAmount())
                                        .creationDate(transaction.getCreationDate())
                                        .accountCredited(financialAccount)
                                        .paymentMode(paymentMode)
                                        .memberDebited(transaction.getMemberDebited())
                                        .build();
                                return collectivityTransaction;
                            })
                            .toList();
                })
                .flatMap(List::stream)
                .toList();
    }

    private PaymentMode getPaymentMode(FinancialAccount financialAccount) {
        PaymentMode paymentMode;
        paymentMode = switch (financialAccount) {
            case BankAccount ignored -> BANK_TRANSFER;
            case MobileBankingAccount ignored -> MOBILE_BANKING;
            case CashAccount ignored -> CASH;
            default ->
                    throw new IllegalArgumentException("Unknown financial account type " + financialAccount.getClass().getTypeName());
        };
        return paymentMode;
    }

    public List<GlobalStats> findGlobalStats(LocalDate from, LocalDate to) {
    List<Collectivity> collectivities = collectivityRepository.findALl();
    List<GlobalStats> stats = new ArrayList<>();

    for (Collectivity collectivity : collectivities) {

        List<MembershipFee> fees = membershipFeeRepository
                .getMembershipFeesByCollectivityId(collectivity.getId())
                .stream()
                .filter(c -> c.getStatus() == ActivityStatus.ACTIVE
                          && c.getEligibleFrom().isBefore(to))
                .toList();

        List<Member> members = memberRepository.findAllByCollectivity(collectivity, to);

        int totalMember = members.size();
        int thoseWhoAreNotLate = 0;

        for (Member m : members) {

            LocalDate adhesionDate = memberRepository
                    .findAdhesionDate(m.getId(), collectivity.getId());

            double memberTotalFee = 0;

            for (MembershipFee fee : fees) {

                LocalDate startDate = fee.getEligibleFrom().isBefore(adhesionDate)
                        ? adhesionDate
                        : fee.getEligibleFrom();

                while (startDate.isBefore(to)) {
                    if (!startDate.isBefore(from)) {
                        memberTotalFee += fee.getAmount();
                    }
                    if (fee.getFrequency() == Frequency.ANNUALLY) {
                        startDate = startDate.plusYears(1);
                    } else if (fee.getFrequency() == Frequency.MONTHLY) {
                        startDate = startDate.plusMonths(1);
                    } else if (fee.getFrequency() == Frequency.WEEKLY) {
                        startDate = startDate.plusWeeks(1);
                    } else {
                        break;
                    }
                }
            }

            double totalPaid = memberRepository.findTotalPaid(m.getId(), from, to);
            if (totalPaid >= memberTotalFee) {
                thoseWhoAreNotLate++;
            }
        }

        double percentage = totalMember == 0 ? 0 : ((double) thoseWhoAreNotLate / totalMember) * 100;

        int newMembers = collectivityRepository.findNewMembers(collectivity.getId(), from, to);

        GlobalStats stat = new GlobalStats();
        stat.setCollectivityInformation(new CollectivityInformation(
                collectivity.getName(),
                collectivity.getNumber()
        ));
        stat.setNewMembersNumber(newMembers);
        stat.setOverallMemberCurrentDuePercentage(percentage);
        stats.add(stat);
    }

    return stats;
}
}
