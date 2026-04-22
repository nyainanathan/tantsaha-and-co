package com.tantsaha.tantsaha.controller;

import com.tantsaha.tantsaha.DTO.AssignCollectivityIdentity;
import com.tantsaha.tantsaha.DTO.CreateCollectivity;
import com.tantsaha.tantsaha.exception.AppBadRequestException;
import com.tantsaha.tantsaha.service.CollectivityService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class CollectivityController {

    private CollectivityService collectivityService;

    @PostMapping("/collectivities")
    public ResponseEntity<?> saveALl(
            @RequestBody List<CreateCollectivity> toSave
            ) {
        try {
            return ResponseEntity.status(201)
                    .header("Content-Type", "application/json")
                    .body(this.collectivityService.saveAll(toSave));
        } catch (Exception e){
            return ResponseEntity.status(500)
                    .header("Content-Type", "text/plain")
                    .body(this.collectivityService.saveAll(toSave));
        }
    }

    @PutMapping("/collectivities/{id}/identity")
    public ResponseEntity<?> assignIdentity(
            @PathVariable String id,
            @RequestBody AssignCollectivityIdentity body
    ){
        try {
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(collectivityService.assignIdentity(id, body));

        } catch (AppBadRequestException e){
            return ResponseEntity.status(400)
                    .header("Content-Type", "text/plain")
                    .body(e.getMessage());

        } catch (RuntimeException e){
            return ResponseEntity.status(404)
                    .header("Content-Type", "text/plain")
                    .body("Collectivity not found");
        }
    }
}
