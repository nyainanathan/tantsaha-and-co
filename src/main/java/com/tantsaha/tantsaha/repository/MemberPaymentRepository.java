package com.tantsaha.tantsaha.repository;

import com.tantsaha.tantsaha.entity.member.MemberPayment;
import com.tantsaha.tantsaha.enums.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class MemberPaymentRepository {

    private final DataSource dataSource;

    @Transactional
    public String save(Double amount, String accountId, PaymentMode mode, String memberId, String feeId) {

        String query1 = """
                INSERT INTO payment
                    (amount, account_credited_cash_id, payment_mode, member_id, creation_date, id, fee_id)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                RETURNING id
                """;

        String query2 = """
                INSERT INTO payment
                    (amount, account_credited_bank_id, payment_mode, member_id, creation_date, id, fee_id)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                RETURNING id
                """;

        String query3 = """
                INSERT INTO payment
                    (amount, account_credited_mobile_id, payment_mode, member_id, creation_date, id, fee_id)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                RETURNING id
                """;

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = null;

            if(mode == PaymentMode.CASH){
                ps = connection.prepareStatement(query1);
            }  else if (mode == PaymentMode.BANK_TRANSFER){
                ps = connection.prepareStatement(query2);
            } else {
                ps = connection.prepareStatement(query3);
            }

            ps.setDouble(1, amount);
            ps.setString(2, accountId);
            ps.setString(3, mode.toString());
            ps.setString(4, memberId);
            ps.setDate(5, Date.valueOf(LocalDate.now()));
            ps.setString(6, UUID.randomUUID().toString());
            ps.setString(7, feeId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("id");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public MemberPayment getById(String id) {
        String query = """
                SELECT mp.id, mp.amount, mp.payment_mode, mp.creation_date
                FROM payment mp
                WHERE mp.id = ?
                """;

        try (Connection connection= dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                MemberPayment payment = new MemberPayment();
                payment.setId(rs.getString("id"));
                payment.setAmount(rs.getInt("amount"));
                payment.setPaymentMode(PaymentMode.valueOf(rs.getString("payment_mode")));
                payment.setCreationDate(rs.getDate("creation_date").toLocalDate());

                return payment;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
