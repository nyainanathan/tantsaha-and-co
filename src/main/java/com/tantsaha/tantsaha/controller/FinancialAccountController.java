package com.tantsaha.tantsaha.controller;

import com.tantsaha.tantsaha.DTO.FinancialAccountDTO;
import com.tantsaha.tantsaha.service.FinancialAccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;

@RestController
@AllArgsConstructor
public class FinancialAccountController {

    private FinancialAccountService  financialAccountService;

    @GetMapping ("/collectivities/{id}/financialAccounts")
    public ResponseEntity<?> getFinancialAccounts(
            @PathVariable String id,
            @RequestParam LocalDate at
    ){
        try{
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(financialAccountService.getFinancialAccounts(id, at));
        } catch (RuntimeException e){
            return ResponseEntity
                    .status(500)
                    .header("Content-Type", "text/plain")
                    .body(e.getMessage());
        }
    }
}
