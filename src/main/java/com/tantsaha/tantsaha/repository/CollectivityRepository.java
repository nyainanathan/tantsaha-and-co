package com.tantsaha.tantsaha.repository;

import com.tantsaha.tantsaha.entity.Collectivity;
import com.tantsaha.tantsaha.entity.CollectivityCreate;
import com.tantsaha.tantsaha.entity.CollectivityStructure;
import com.tantsaha.tantsaha.entity.CreateCollectivity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
@RequiredArgsConstructor
public class CollectivityRepository {

    private final Connection conn;

    public Collectivity createCollectivity(CreateCollectivity data){

        Collectivity collectivity = null;

        String query = """
                INSERT INTO collectivity
                (name, city, speciality)
                VALUES (? , ? , ?)
                RETURNING id;
                """;

        try {
            PreparedStatement ps = conn.prepareStatement(query);
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
}
