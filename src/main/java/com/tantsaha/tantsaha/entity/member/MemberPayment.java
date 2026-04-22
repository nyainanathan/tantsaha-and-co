package com.tantsaha.tantsaha.entity.member;

import com.tantsaha.tantsaha.entity.account.FinancialAccount;
import com.tantsaha.tantsaha.enums.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class MemberPayment {
    private int id;
    private int amount;
    private PaymentMode paymentMode;
    private FinancialAccount accountCredited;
    private LocalDate creationDate;
}
