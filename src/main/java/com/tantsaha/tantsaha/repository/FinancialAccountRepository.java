package com.tantsaha.tantsaha.repository;

import com.tantsaha.tantsaha.DTO.FinancialAccountDTO;
import com.tantsaha.tantsaha.enums.Bank;
import com.tantsaha.tantsaha.enums.MobileBankingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class FinancialAccountRepository {
    private DataSource dataSource;

    public List<FinancialAccountDTO> getFinancialAccounts(String collectivityId, LocalDate at) {

        List<FinancialAccountDTO> result = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {

            String cashQuery = """
            SELECT amount
            FROM cash_account
            WHERE collectivity_id = ?
        """;

            PreparedStatement ps1 = connection.prepareStatement(cashQuery);
            ps1.setString(1, collectivityId);

            ResultSet rs1 = ps1.executeQuery();

            double cashBalance = 0;

            while (rs1.next()) {
                cashBalance += rs1.getDouble("amount");
            }

            result.add(new FinancialAccountDTO(
                    "cash_account",
                    null,
                    null,
                    cashBalance
                        ));

            String bankQuery = """
            SELECT amount, bank_name
            FROM bank_account
            WHERE collectivity_id = ?
        """;

            PreparedStatement ps2 = connection.prepareStatement(bankQuery);
            ps2.setString(1, collectivityId);

            ResultSet rs2 = ps2.executeQuery();

            Map<Bank, Double> bankBalances = new HashMap<>();

            while (rs2.next()) {
                Bank bank = Bank.valueOf(rs2.getString("bank_name"));
                double amount = rs2.getDouble("amount");

                bankBalances.put(bank,
                        bankBalances.getOrDefault(bank, 0.0) + amount);
            }

            for (Map.Entry<Bank, Double> entry : bankBalances.entrySet()) {
                result.add(new FinancialAccountDTO(
                        "bank_account",
                        entry.getKey(),
                        null,
                        entry.getValue()
                                ));
            }

            String mobileQuery = """
            SELECT amount, service
            FROM mobile_banking_account
            WHERE collectivity_id = ?
        """;

            PreparedStatement ps3 = connection.prepareStatement(mobileQuery);
            ps3.setString(1, collectivityId);

            ResultSet rs3 = ps3.executeQuery();

            Map<MobileBankingService, Double> mobileBalances = new HashMap<>();

            while (rs3.next()) {
                MobileBankingService service =
                        MobileBankingService.valueOf(rs3.getString("service"));

                double amount = rs3.getDouble("amount");

                mobileBalances.put(service,
                        mobileBalances.getOrDefault(service, 0.0) + amount);
            }

            for (Map.Entry<MobileBankingService, Double> entry : mobileBalances.entrySet()) {
                result.add(new FinancialAccountDTO(
                        "mobile_banking_account",
                        null,
                        entry.getKey(),
                        entry.getValue()
                                ));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
