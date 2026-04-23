package com.tantsaha.tantsaha.repository;

import com.tantsaha.tantsaha.entity.collectivity.CollectivityTransaction;
import com.tantsaha.tantsaha.enums.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class TransactionRepository {
    private final DataSource dataSource;

    public void saveTransaction(
            String collectivityId,
            Double amount,
            String accountId,
            String memberId,
            String paymentMode
    ) {

        String query1 = """
                INSERT INTO transaction
                    (amount, account_credited_cash_id, payment_mode, member_id, creation_date, collectivity_id)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        String query2 = """
                INSERT INTO transaction
                    (amount, account_credited_bank_id, payment_mode, member_id, creation_date, id, collectivity_id)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        String query3 = """
                INSERT INTO transaction
                    (amount, account_credited_mobile_id, payment_mode, member_id, creation_date, id, collectivity_id)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = dataSource.getConnection()) {

            PreparedStatement ps = null;

            if(PaymentMode.valueOf(paymentMode) == PaymentMode.CASH){
                ps = connection.prepareStatement(query1);
            }  else if (PaymentMode.valueOf(paymentMode) == PaymentMode.BANK_TRANSFER){
                ps = connection.prepareStatement(query2);
            } else {
                ps = connection.prepareStatement(query3);
            }

            ps.setDouble(1, amount);
            ps.setString(2, accountId);
            ps.setString(3, paymentMode);
            ps.setString(4, memberId);
            ps.setDate(5, Date.valueOf(LocalDate.now()));
            ps.setString(6, UUID.randomUUID().toString());
            ps.setString(7, collectivityId);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<CollectivityTransaction> getByCollectivityAndPeriod(
            String collectivityId,
            java.time.LocalDate from,
            java.time.LocalDate to
    ) {

        List<CollectivityTransaction> transactions = new ArrayList<>();

        String query = """
                SELECT id, creation_date, amount, payment_mode
                FROM transaction
                WHERE collectivity_id = ?
                AND creation_date BETWEEN ? AND ?
                """;

        try (Connection connection = dataSource.getConnection();){
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, collectivityId);
            ps.setDate(2, Date.valueOf(from));
            ps.setDate(3, Date.valueOf(to));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                CollectivityTransaction t = new CollectivityTransaction();

                t.setId(rs.getInt("id"));
                t.setCreationDate(rs.getDate("creation_date").toLocalDate());
                t.setAmount(rs.getDouble("amount"));
                t.setPaymentMode(PaymentMode.valueOf(rs.getString("payment_mode")));

                t.setAccountCredited(null);
                t.setMemberDebited(null);

                transactions.add(t);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return transactions;
    }
}
