package com.tantsaha.tantsaha.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class FinancialAccount {
    private int id;
    private double amount;
}
