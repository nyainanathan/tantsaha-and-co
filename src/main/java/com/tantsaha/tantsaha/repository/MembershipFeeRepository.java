package com.tantsaha.tantsaha.repository;

import com.tantsaha.tantsaha.DTO.CreateMembershipFee;
import com.tantsaha.tantsaha.entity.member.Member;
import com.tantsaha.tantsaha.entity.member.MembershipFee;
import com.tantsaha.tantsaha.enums.ActivityStatus;
import com.tantsaha.tantsaha.enums.Frequency;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class MembershipFeeRepository {

    private Connection conn;

    public String save(CreateMembershipFee fee, String collectivityId){
        String query = """
                INSERT INTO fee
                (eligible_from, frequency, amount, label, collectivity_id)
                VALUES (?, ?, ?, ?, ?)
                RETURNING id
                """;

        String savedFeeId = null;

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setDate(1, Date.valueOf(fee.getEligibleForm()));
            ps.setString(2, fee.getFrequency().toString());
            ps.setDouble(3, fee.getAmount());
            ps.setString(4, fee.getLabel());
            ps.setString(5, collectivityId);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                savedFeeId = rs.getString("id");
            }

        } catch (Exception e){
            throw new RuntimeException(e);
        }

        return savedFeeId;
    }

    public MembershipFee getById(String id){

        MembershipFee fee = null;

        String query = """
                select eligible_from, frequency,amount,
                    label, id, status
                from fee
                where id = ?
                """;

        try{
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                fee = new MembershipFee();
                fee.setId(rs.getString("id"));
                fee.setAmount(rs.getDouble("amount"));
                fee.setLabel(rs.getString("label"));
                fee.setStatus(ActivityStatus.valueOf(rs.getString("status")));
                fee.setFrequency(Frequency.valueOf(rs.getString("frequency")));
                fee.setEligibleForm(rs.getDate("eligible_from").toLocalDate());
            }

        } catch (Exception e){
            throw new RuntimeException(e);
        }

        return fee;
    }

    public List<MembershipFee> getByCollectivityId(String collectivityId){

        List<MembershipFee> fees = new ArrayList<>();

        String query = """
                select eligible_from, frequency,amount,
                    label, id, status
                from fee
                where collectivity_id = ?
                """;

        try{
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                MembershipFee fee = new MembershipFee();
                fee.setId(rs.getString("id"));
                fee.setAmount(rs.getDouble("amount"));
                fee.setLabel(rs.getString("label"));
                fee.setStatus(ActivityStatus.valueOf(rs.getString("status")));
                fee.setFrequency(Frequency.valueOf(rs.getString("frequency")));
                fee.setEligibleForm(rs.getDate("eligible_from").toLocalDate());

                fees.add(fee);
            }

        } catch (Exception e){
            throw new RuntimeException(e);
        }

        return fees;
    }

}
