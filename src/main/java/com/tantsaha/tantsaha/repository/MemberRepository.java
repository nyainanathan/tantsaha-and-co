package com.tantsaha.tantsaha.repository;

import com.tantsaha.tantsaha.entity.Gender;
import com.tantsaha.tantsaha.entity.Member;
import com.tantsaha.tantsaha.entity.MemberOccupation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final Map<Integer, Member> members =  new HashMap<>();
    private final Connection conn;

    public Member save(Member member) {
        members.put(member.getId(), member);
        return member;
    }
    public Member findById(Integer id) {
        return members.get(id);
    }

    public List<Member> findByCollectivityId(String id){

        String query = """
                SELECT last_name, first_name, birth_date,
                    gender, address, occupation, phone,
                    email, id
                FROM member
                WHERE id_collectivity = ?
                """;

        try{
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Member member = new Member();
                //can lwk be a string. Change accordingly
                member.setId(rs.getInt("id"));
                member.setFirstName(rs.getString("first_name"));
                member.setLastName(rs.getString("last_name"));
                member.setBirthDate(rs.getDate("birth_date").toLocalDate());
                member.setGender(Gender.valueOf(rs.getString("gender")));
                member.setAddress(rs.getString("address"));
                member.setProfession(rs.getString("profession"));
                member.setPhoneNumber(rs.getString("phone"));
                member.setEmail(rs.getString("email"));
                member.setOccupation(MemberOccupation.valueOf(rs.getString("occupation")));
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
