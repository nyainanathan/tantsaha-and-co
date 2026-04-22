package com.tantsaha.tantsaha.entity.account;

import com.tantsaha.tantsaha.enums.MobileBankingService;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MobileBankingAccount extends FinancialAccount {
    private String holderName;
    private MobileBankingService mobileBankingService;
    private String mobileNumber;

    public MobileBankingAccount(int id, double amount, String holderName,
                                MobileBankingService mobileBankingService, String mobileNumber) {
        super(id, amount);
        this.holderName = holderName;
        this.mobileBankingService = mobileBankingService;
        this.mobileNumber = mobileNumber;
    }
}
