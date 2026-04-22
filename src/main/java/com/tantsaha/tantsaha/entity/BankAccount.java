package com.tantsaha.tantsaha.entity;

import com.tantsaha.tantsaha.enums.Bank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BankAccount extends  FinancialAccount {
    private String holderName;
    private Bank bankName;
    private int bankCode;
    private int bankBranchCode;
    private int bankAccountNumber;
    private int bankAccountKey;

    public BankAccount(int id, double amount, String holderName,
                       Bank bankName, int bankCode,
                       int bankBranchCode, int bankAccountNumber, int bankAccountKey) {

        super(id, amount);
        this.holderName = holderName;
        this.bankName = bankName;
        this.bankCode = bankCode;
        this.bankBranchCode = bankBranchCode;
        this.bankAccountNumber = bankAccountNumber;
        this.bankAccountKey = bankAccountKey;
    }
}
