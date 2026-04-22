package com.tantsaha.tantsaha.entity;

import com.tantsaha.tantsaha.enums.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateMemberPayment {
    private Double amount;
    private int membershipFeeIdentifier;
    private int accountCreditedIdentifier;
    private PaymentMode paymentMode;
}
