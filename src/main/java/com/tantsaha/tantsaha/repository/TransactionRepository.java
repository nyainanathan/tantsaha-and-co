package com.tantsaha.tantsaha.repository;

import com.tantsaha.tantsaha.entity.collectivity.CollectivityTransaction;
import com.tantsaha.tantsaha.enums.PaymentMode;
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

@Repository
@RequiredArgsConstructor
public class TransactionRepository {
    private DataSource dataSource;

    public void saveTransaction(
            String collectivityId,
            Double amount,
            int accountId,
            String memberId,
            String paymentMode
    ) {
        String query = """
            INSERT INTO transaction(
                collectivity_id, amount, account_id, member_id, payment_mode, creation_date
            )
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, collectivityId);
            ps.setDouble(2, amount);
            ps.setInt(3, accountId);
            ps.setString(4, memberId);
            ps.setString(5, paymentMode);
            ps.setDate(6, Date.valueOf(LocalDate.now()));

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
