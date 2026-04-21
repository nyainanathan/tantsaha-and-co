package com.tantsaha.tantsaha.service;

import com.tantsaha.tantsaha.entity.Collectivity;
import com.tantsaha.tantsaha.entity.CollectivityStructure;
import com.tantsaha.tantsaha.entity.CreateCollectivity;
import com.tantsaha.tantsaha.entity.Member;
import com.tantsaha.tantsaha.exception.AppBadRequestException;
import com.tantsaha.tantsaha.repository.CollectivityRepository;
import com.tantsaha.tantsaha.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectivityService {

    private final CollectivityRepository collectivityRepository;
    private final MemberRepository memberRepository;

    public Collectivity save(CreateCollectivity toSave){

        Collectivity collectivity = this.collectivityRepository.createCollectivity(toSave);

        List<Member> members = this.memberRepository.findByCollectivityId(collectivity.getId());

        if (members.size() < 10){
            throw new AppBadRequestException("You need 10 members to create a collectivity!");
        }

        CollectivityStructure structure = new CollectivityStructure();


    }
}
