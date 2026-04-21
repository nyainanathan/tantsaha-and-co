package com.tantsaha.tantsaha.service;

import com.tantsaha.tantsaha.entity.Collectivity;
import com.tantsaha.tantsaha.entity.CollectivityCreate;
import com.tantsaha.tantsaha.repository.CollectivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CollectivityService {

    private final CollectivityRepository collectivityRepository;
    private final
    public Collectivity save(CollectivityCreate toSave){

        Collectivity collectivity = this.collectivityRepository.createCollectivity(toSave);


    }
}
