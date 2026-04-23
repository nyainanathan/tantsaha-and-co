package com.tantsaha.tantsaha.entity.collectivity;

import com.tantsaha.tantsaha.entity.account.FinancialAccount;
import com.tantsaha.tantsaha.entity.member.Member;
import com.tantsaha.tantsaha.enums.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CollectivityTransaction {
    private String id;
    private LocalDate creationDate;
    private double amount;
    private PaymentMode paymentMode;
    private FinancialAccount accountCredited;
    private Member memberDebited;
}
