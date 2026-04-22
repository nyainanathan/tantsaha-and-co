package com.tantsaha.tantsaha.repository;

import com.tantsaha.tantsaha.entity.member.MemberPayment;
import com.tantsaha.tantsaha.enums.PaymentMode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

@Repository
@RequiredArgsConstructor
public class MemberPaymentRepository {

    private DataSource dataSource;

    public String save(Double amount, int accountId, PaymentMode mode, String memberId) {
        String query = """
                INSERT INTO member_payment(amount, account_id, payment_mode, member_id, creation_date)
                VALUES (?, ?, ?, ?, ?)
                RETURNING id
                """;

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setDouble(1, amount);
            ps.setInt(2, accountId);
            ps.setString(3, mode.toString());
            ps.setString(4, memberId);
            ps.setDate(5, Date.valueOf(LocalDate.now()));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("id");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public MemberPayment getById(int id) {
        String query = """
                SELECT mp.id, mp.amount, mp.payment_mode, mp.creation_date
                FROM member_payment mp
                WHERE mp.id = ?
                """;

        try (Connection connection= dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                MemberPayment payment = new MemberPayment();
                payment.setId(rs.getInt("id"));
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
