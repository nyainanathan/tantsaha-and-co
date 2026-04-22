package com.tantsaha.tantsaha.repository;

import com.tantsaha.tantsaha.entity.collectivity.Collectivity;
import com.tantsaha.tantsaha.DTO.CreateCollectivity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CollectivityRepository {

    private DataSource dataSource;


    public Collectivity findById(String id){
        String query = """
        SELECT id, location, name, number
        FROM collectivity
        WHERE id = ?
    """;

        try (Connection connection = dataSource.getConnection();){
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                Collectivity collec = new Collectivity();
                collec.setId(rs.getString("id"));
                collec.setLocation(rs.getString("location"));
                collec.setName(rs.getString("name"));
                collec.setNumber(rs.getInt("number"));
                return collec;
            }

        } catch (Exception e){
            throw new RuntimeException(e);
        }

        return null;
    }

    public boolean existsByName(String name){
        String query = """
        SELECT id
        FROM collectivity
        WHERE name = ?
    """;

        try (Connection connection = dataSource.getConnection();) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public boolean existsById(String id){
        String query = """
        SELECT 1
        FROM collectivity
        WHERE id = ?
    """;

        try (Connection connection = dataSource.getConnection();) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public Collectivity assignIdentity(String id, String name, Integer number){

        String query = """
        UPDATE collectivity
        SET name = ?, number = ?
        WHERE id = ?
        RETURNING id, location, name, number
    """;

        try (Connection connection = dataSource.getConnection();) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, name);
            ps.setInt(2, number);
            ps.setString(3, id);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                Collectivity collec = new Collectivity();
                collec.setId(rs.getString("id"));
                collec.setLocation(rs.getString("location"));
                collec.setName(rs.getString("name"));
                collec.setNumber(rs.getInt("number"));
                return collec;
            }

        } catch (Exception e){
            throw new RuntimeException(e);
        }

        return null;
    }

    public Collectivity createCollectivity(CreateCollectivity data){

        Collectivity collectivity = null;

        String query = """
                INSERT INTO collectivity
                (name, location, specialty, id)
                VALUES (? , ? , ? , ?)
                RETURNING id;
                """;

        try (Connection connection = dataSource.getConnection();) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, UUID.randomUUID().toString());
            ps.setString(2, data.getLocation());
            ps.setString(3, UUID.randomUUID().toString());
            ps.setString(4, UUID.randomUUID().toString());
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                collectivity = new Collectivity();
                collectivity.setId(
                        rs.getString("id")
                );
                collectivity.setLocation(data.getLocation());
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }

        return collectivity;
    }
//
//    public Collectivity assignIdentity(String id, Integer number, String name) {
//
//        String checkQuery = "SELECT name, number FROM collectivity WHERE id = ?";
//        String checkName = "SELECT COUNT(id) FROM collectivity WHERE name = ?";
//
//        String updateQuery = """
//        UPDATE collectivity
//        SET name = ?, number = ?
//        WHERE id = ?
//    """;
//
//        try {
//            PreparedStatement ps = conn.prepareStatement(checkQuery);
//            ps.setString(1, id);
//            ResultSet rs = ps.executeQuery();
//
//            if (!rs.next()) {
//                throw new RuntimeException("Collectivity not found");
//            }
//
//            if (rs.getString("name") != null || rs.getObject("number") != null) {
//                throw new RuntimeException("Already assigned");
//            }
//
//            PreparedStatement ps2 = conn.prepareStatement(checkName);
//            ps2.setString(1, name);
//            ResultSet rs2 = ps2.executeQuery();
//
//            if (rs2.next() && rs2.getInt(1) > 0) {
//                throw new RuntimeException("Name already exists");
//            }
//
//            PreparedStatement ps3 = conn.prepareStatement(updateQuery);
//            ps3.setString(1, name);
//            ps3.setInt(2, number);
//            ps3.setString(3, id);
//            ps3.executeUpdate();
//
//            Collectivity c = new Collectivity();
//            c.setId(id);
//            c.setName(name);
//            c.setNumber(number);
//
//            return c;
//
//        } catch (Exception e){
//            throw new RuntimeException(e);
//        }
//    }
}
