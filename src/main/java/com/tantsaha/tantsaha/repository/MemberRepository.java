package com.tantsaha.tantsaha.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.tantsaha.tantsaha.entity.CreateMember;
import com.tantsaha.tantsaha.entity.Gender;
import com.tantsaha.tantsaha.entity.Member;
import com.tantsaha.tantsaha.entity.MemberHistory;
import com.tantsaha.tantsaha.entity.MemberOccupation;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final Connection conn;

    public Member findById(String id){
        String query = """
                SELECT last_name, first_name, birth_date,
                    gender, address, occupation, phone,
                    email, id, profession
                FROM member
                WHERE id = ?
                """;

        String query1 = """
                SELECT mentee_member_id
                FROM mentor
                WHERE mentor_member_id = ?
                """;

        Member member = null;

        try{
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                member = new Member();
                member.setId(rs.getString("id"));
                member.setFirstName(rs.getString("first_name"));
                member.setLastName(rs.getString("last_name"));
                member.setBirthDate(rs.getDate("birth_date").toLocalDate());
                member.setGender(Gender.valueOf(rs.getString("gender")));
                member.setAddress(rs.getString("address"));
                member.setProfession(rs.getString("profession"));
                member.setPhoneNumber(rs.getInt("phone"));
                member.setEmail(rs.getString("email"));
                member.setOccupation(MemberOccupation.valueOf(rs.getString("occupation")));
            }

            List<String> referees = new ArrayList<>();

            PreparedStatement ps1 = conn.prepareStatement(query1);
            ps1.setString(1, id);
            ResultSet rs1 = ps1.executeQuery();

            while(rs1.next()){
                referees.add(
                        rs1.getString("mentee_member_id")
                );
            }

            assert member != null;
            member.setReferees(referees);
        } catch (Exception e){
            throw new RuntimeException(e);
        }

        return member;
    }

    public List<Member> findByCollectivityId(String collectivityId){

        List<Member> members = new ArrayList<>();
        List<String> memberIds = new ArrayList<>();

        String query = """
                SELECT id
                FROM member
                WHERE collectivity_id = ?
                """;
        try{
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1 , collectivityId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                memberIds.add(
                        rs.getString("id")
                );
            }

            for(String id : memberIds){
                members.add(
                        this.findById(id)
                );
            }

        } catch (Exception e){
            throw new RuntimeException(e);
        }

        return members;
    }

    public List<MemberHistory> getMemberHistory(String memberId){

        String query = """
                SELECT r.label, ra.attributed_at, ra.ended_at
                FROM role_attribution ra
                JOIN role r ON ra.role_id = r.id
                WHERE ra.member_id = ?
                """;

        List<MemberHistory> history = new ArrayList<>();

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, memberId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Date endedAt = rs.getDate("ended_at");
                LocalDate endDate = endedAt != null ? endedAt.toLocalDate() : LocalDate.now();

                history.add(
                        new MemberHistory(
                                MemberOccupation.valueOf(rs.getString("label")),
                                rs.getDate("attributed_at").toLocalDate(),
                                endDate
                        )
                );
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }

        return history;
    }

    public void detachMember(String memberId){
        String query = """
                UPDATE member
                SET collectivity_id = null
                WHERE id = ?
                """;

        String query1 = """
                UPDATE role_attribution
                SET ended_at = current_date
                WHERE ended_at is  null
                AND member_id = ?
                """;

        try{

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1 , memberId);
            ps.execute();

            PreparedStatement ps1 = conn.prepareStatement(query1);
            ps1.setString(1, memberId);
            ps1.execute();

        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public void attachMember(String memberId, String collectivityId, MemberOccupation occupation){
        String query = """
                UPDATE member
                SET collectivity_id = ?
                WHERE id = ?
                """;

        boolean isUniquePerMandate = occupation != MemberOccupation.JUNIOR && occupation != MemberOccupation.SENIOR;

        String query1 = """
                SELECT id
                FROM role
                WHERE label = ?
                AND is_unique_per_mandate = ?
                LIMIT 1
                """;

        String query2 = """
                INSERT INTO role_attribution
                (member_id, role_id)
                VALUES (?, ?)
                """;

        try{

            PreparedStatement ps1 = conn.prepareStatement(query1);
            ps1.setString(1, occupation.name());
            ps1.setBoolean(2, isUniquePerMandate);

            Integer roleId = null;

            ResultSet rs = ps1.executeQuery();;
            if(rs.next()){
                roleId = rs.getInt("id");
            }

            if(roleId == null){
                throw new RuntimeException("Role not found");
            }

            PreparedStatement ps2= conn.prepareStatement(query2);
            ps2.setString(1,memberId);
            ps2.setInt(2, roleId);
            ps2.executeUpdate();

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1 , collectivityId);
            ps.setString(2 , memberId);
            ps.executeUpdate();

        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public void mentor(String mentorId, String menteeId){
        String query = """
                INSERT INTO mentor
                (mentor_member_id, mentee_member_id)
                VALUES (?, ?)
                """;

        try{
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, mentorId);
            ps.setString(2, menteeId);
            ps.executeUpdate();

        } catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    public String create(CreateMember toSave){
        String query = """
                INSERT INTO member
                (last_name, first_name, birth_date,
                gender, address, occupation,
                phone, email, profession, collectivity_id, id)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                RETURNING id
                """;

        String memberId = null;
        try{
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, toSave.getLastName());
            ps.setString(2, toSave.getFirstName());
            ps.setDate(3, Date.valueOf(toSave.getBirthDate()));
            ps.setString(4, toSave.getGender().name());
            ps.setString(5, toSave.getAddress());
            ps.setString(6, toSave.getOccupation().name());
            ps.setInt(7, toSave.getPhoneNumber());
            ps.setString(8, toSave.getEmail());
            ps.setString(9, toSave.getProfession());
            ps.setString(10, toSave.getCollectivityIdentifier());
            ps.setString(11, UUID.randomUUID().toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                memberId = rs.getString("id");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return memberId;
    }
}
