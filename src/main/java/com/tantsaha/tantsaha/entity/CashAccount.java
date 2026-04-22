package com.tantsaha.tantsaha.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CashAccount extends FinancialAccount {
    public CashAccount(int id, double amount) {
        super(id, amount);
    }
}
