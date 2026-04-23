package com.tantsaha.tantsaha.repository;

import com.tantsaha.tantsaha.DTO.FinancialAccountDTO;
import com.tantsaha.tantsaha.enums.Bank;
import com.tantsaha.tantsaha.enums.MobileBankingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
@AllArgsConstructor
public class FinancialAccountRepository {

    private DataSource dataSource;

    private static final String CASH_SQL = """
            SELECT id, amount
            FROM cash_account
            WHERE collectivity_id = ?
            """;

    private static final String BANK_SQL = """
            SELECT id, amount, bank_name
            FROM bank_account
            WHERE collectivity_id = ?
            """;

    private static final String MOBILE_SQL = """
            SELECT id, amount, service
            FROM mobile_banking_account
            WHERE collectivity_id = ?
            """;

    private static final String PAYMENTS_AFTER = """
            SELECT COALESCE(SUM(mp.amount), 0)
            FROM member_payment mp
            WHERE mp.account_credited_type = ?
              AND mp.account_credited_id = ?
              AND mp.creation_date > ?
            """;

    public List<FinancialAccountDTO> getFinancialAccounts(String collectivityId, LocalDate at) {
        List<FinancialAccountDTO> result = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {

            try (PreparedStatement ps = connection.prepareStatement(CASH_SQL)) {
                ps.setString(1, collectivityId);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        double amount = rs.getDouble("amount");
                        if (at != null) {
                            amount -= paymentsAfter(connection, "CASH", id, at);
                        }
                        result.add(new FinancialAccountDTO(
                                "cash_account",
                                null,
                                null,
                                amount
                        ));
                    }
                }
            }

            try (PreparedStatement ps = connection.prepareStatement(BANK_SQL)) {
                ps.setString(1, collectivityId);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {

                        int id = rs.getInt("id");

                        Bank bank = Bank.valueOf(rs.getString("bank_name"));
                        double amount = rs.getDouble("amount");

                        if (at != null) {
                            amount -= paymentsAfter(connection, "BANK_TRANSFER", id, at);
                        }

                        result.add(new FinancialAccountDTO(
                                "bank_account",
                                bank,
                                null,
                                amount
                        ));
                    }
                }
            }

            try (PreparedStatement ps = connection.prepareStatement(MOBILE_SQL)) {
                ps.setString(1, collectivityId);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {

                        int id = rs.getInt("id");

                        MobileBankingService service =
                                MobileBankingService.valueOf(rs.getString("service"));

                        double amount = rs.getDouble("amount");

                        if (at != null) {
                            amount -= paymentsAfter(connection, "MOBILE_BANKING", id, at);
                        }

                        result.add(new FinancialAccountDTO(
                                "mobile_banking_account",
                                null,
                                service,
                                amount
                        ));
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    private double paymentsAfter(Connection connection, String accountType, int accountId, LocalDate at) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(PAYMENTS_AFTER)) {
            ps.setString(1, accountType);
            ps.setInt(2, accountId);
            ps.setDate(3, at != null ? Date.valueOf(at) : Date.valueOf(LocalDate.now()));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getDouble(1) : 0.0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}