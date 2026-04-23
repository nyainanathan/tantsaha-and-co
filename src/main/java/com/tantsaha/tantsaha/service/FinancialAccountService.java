package com.tantsaha.tantsaha.service;

import com.tantsaha.tantsaha.DTO.FinancialAccountDTO;
import com.tantsaha.tantsaha.repository.FinancialAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class FinancialAccountService {

    private FinancialAccountRepository financialAccountRepository;
    public List<FinancialAccountDTO> getFinancialAccounts(String id, LocalDate at) {
        return financialAccountRepository.getFinancialAccounts(id, at);
    }
}
