package com.tantsaha.tantsaha.DTO;

import com.tantsaha.tantsaha.enums.Bank;
import com.tantsaha.tantsaha.enums.MobileBankingService;
import com.tantsaha.tantsaha.enums.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FinancialAccountDTO {
    private String type;
    private Bank bankName;
    private MobileBankingService mobileBankingService;
    private Double amount;
}
