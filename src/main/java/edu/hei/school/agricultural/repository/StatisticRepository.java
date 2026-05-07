package edu.hei.school.agricultural.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class StatisticRepository {

    private final Connection connection;

    public Map<String, Double> getEarnedAmountByMember(String collectivityId,
                                                       LocalDate from,
                                                       LocalDate to) {
        Map<String, Double> result = new HashMap<>();

        String sql = """
                SELECT t.member_debited_id  AS member_id,
                       SUM(t.amount)        AS earned_amount
                FROM "transaction" t
                WHERE t.transaction_type  = 'IN'
                  AND t.creation_date BETWEEN ? AND ?
                  AND t.financial_account_id IN (
                      SELECT id FROM cash_account           WHERE collectivity_id = ?
                      UNION ALL
                      SELECT id FROM bank_account           WHERE collectivity_id = ?
                      UNION ALL
                      SELECT id FROM mobile_banking_account WHERE collectivity_id = ?
                  )
                GROUP BY t.member_debited_id
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));
            ps.setString(3, collectivityId);
            ps.setString(4, collectivityId);
            ps.setString(5, collectivityId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.put(rs.getString("member_id"), rs.getDouble("earned_amount"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public Map<String, Double> getUnpaidAmountByMember(String collectivityId,
                                                       LocalDate from,
                                                       LocalDate to) {
        Map<String, Double> result = new HashMap<>();

        String sql = """
                SELECT cm.member_id         AS member_id,
                       COALESCE(SUM(CASE
                           WHEN NOT EXISTS (
                               SELECT 1
                               FROM member_payment mp
                               WHERE mp.member_debited_id = cm.member_id
                                 AND mp.membership_fee_id = mf.id
                                 AND mp.creation_date BETWEEN ? AND ?
                           )
                           THEN mf.amount
                           ELSE 0
                       END), 0)             AS unpaid_amount
                FROM collectivity_member cm
                JOIN membership_fee mf ON mf.collectivity_id = cm.collectivity_id
                                      AND mf.status          = 'ACTIVE'
                WHERE cm.collectivity_id = ?
                GROUP BY cm.member_id
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));
            ps.setString(3, collectivityId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.put(rs.getString("member_id"), rs.getDouble("unpaid_amount"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}