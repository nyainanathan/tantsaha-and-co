package com.tantsaha.tantsaha.service;

import com.tantsaha.tantsaha.DTO.CreateMemberPayment;
import com.tantsaha.tantsaha.entity.member.MemberPayment;
import com.tantsaha.tantsaha.exception.AppNotFoundException;
import com.tantsaha.tantsaha.repository.CollectivityRepository;
import com.tantsaha.tantsaha.repository.MemberPaymentRepository;
import com.tantsaha.tantsaha.repository.MemberRepository;
import com.tantsaha.tantsaha.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class MemberPaymentService {
    private MemberPaymentRepository paymentRepository;
    private MemberRepository memberRepository;
    private TransactionRepository transactionRepository;
    private CollectivityRepository collectivityRepository;

    public List<MemberPayment> createPayments(String memberId, List<CreateMemberPayment> payments) {

        if (!memberRepository.existsById(memberId)) {
            throw new AppNotFoundException("Member not found");
        }

        String collectivityId = memberRepository.getCollectivityId(memberId);
        List<String> ids = new ArrayList<>();

        for (CreateMemberPayment payment : payments) {
            String paymentId = paymentRepository.save(
                    payment.getAmount(),
                    payment.getAccountCreditedIdentifier(),
                    payment.getPaymentMode(),
                    memberId
            );
            ids.add(paymentId);

            transactionRepository.saveTransaction(
                    collectivityId,
                    payment.getAmount(),
                    payment.getAccountCreditedIdentifier(),
                    memberId,
                    payment.getPaymentMode().toString()
            );
        }

        List<MemberPayment> pays = new ArrayList<>();
        for (String id : ids) {
            pays.add(paymentRepository.getById(Integer.parseInt(id)));
        }

        return pays;
    }
}
