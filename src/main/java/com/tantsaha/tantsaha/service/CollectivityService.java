package com.tantsaha.tantsaha.service;

import com.tantsaha.tantsaha.DTO.AssignCollectivityIdentity;
import com.tantsaha.tantsaha.DTO.CreateCollectivity;
import com.tantsaha.tantsaha.entity.collectivity.Collectivity;
import com.tantsaha.tantsaha.entity.collectivity.CollectivityStructure;
import com.tantsaha.tantsaha.entity.member.Member;
import com.tantsaha.tantsaha.enums.MemberOccupation;
import com.tantsaha.tantsaha.exception.AppBadRequestException;
import com.tantsaha.tantsaha.exception.AppConflictException;
import com.tantsaha.tantsaha.repository.CollectivityRepository;
import com.tantsaha.tantsaha.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectivityService {

    private final CollectivityRepository collectivityRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    public Collectivity assignIdentity(String id, AssignCollectivityIdentity collecIdentity){

        Collectivity existing = collectivityRepository.findById(id);

        if(collecIdentity.getName() == null || collecIdentity.getName().isEmpty() || collecIdentity.getNumber()==null){
            throw new AppBadRequestException("Name or number are required");
        }

        if(existing == null){
            throw new RuntimeException("Collectivity not found");
        }

        if(existing.getName() != null || existing.getNumber() != null){
            throw new AppConflictException("Identity already assigned and cannot be modified.");
        }

        if(collectivityRepository.existsByName(collecIdentity.getName())){
            throw new AppConflictException("Collectivity name already exists.");
        }

        return collectivityRepository.assignIdentity(
                id,
                collecIdentity.getName(),
                collecIdentity.getNumber()
        );
    }

    public Collectivity save(CreateCollectivity toSave){

        List<Member> members = new ArrayList<>();

        for(String id : toSave.getMembers()){
            members.add(
                    this.memberRepository.findById(id)
            );
        }

        if (members.size() < 10 || !toSave.getFederationApproval()){
            throw new AppBadRequestException("Collectivity without federation approval or structure missing.");
        }

        int president = 0;
        int vicePresident = 0;
        int treasurer = 0;
        int secretary = 0;

        for(Member member : members){
            switch (member.getOccupation()){
                case SECRETARY -> secretary++;
                case TREASURER -> treasurer++;
                case VICE_PRESIDENT -> vicePresident++;
                case PRESIDENT -> president++;
                default -> {}
            }
        }

        if(president != 1 || vicePresident != 1 || treasurer != 1 || secretary != 1){
            throw  new AppBadRequestException("Collectivity without federation approval or structure missing.");
        }

        List<Long> seniorityOfMembers = new ArrayList<>();

        for(Member member : members){
            seniorityOfMembers.add(
                    this.memberService.getSeniority(member.getId())
            );
        }

        int memberWithEnoughSeniority = seniorityOfMembers.stream()
                .filter(s -> s > 180)
                .toList().size();

//        if(memberWithEnoughSeniority < 5){
//            throw  new AppBadRequestException("Collectivity without federation approval or structure missing.");
//        }

        Collectivity collectivity = this.collectivityRepository.createCollectivity(toSave);

        for(Member member : members) {
            this.memberRepository.attachMember(member.getId() , collectivity.getId(), member.getOccupation());
        }

        collectivity.setMembers(members);

        CollectivityStructure structure = new CollectivityStructure();

        for(Member member : members){
            if(member.getOccupation() == MemberOccupation.PRESIDENT){
                structure.setPresident(member);
            } else if(member.getOccupation() == MemberOccupation.VICE_PRESIDENT){
                structure.setVicePresident(member);
            } else if(member.getOccupation() == MemberOccupation.TREASURER){
                structure.setTreasurer(member);
            } else if(member.getOccupation() == MemberOccupation.SECRETARY){
                structure.setSecretary(member);
            }
        }


        collectivity.setStructure(structure);

        return collectivity;
    }

    public List<Collectivity> saveAll(List<CreateCollectivity> toSave){
        List<Collectivity> collectivities = new ArrayList<>();
        for(CreateCollectivity collectivity : toSave){
            collectivities.add(
                    this.save(collectivity)
            );
        }
        return collectivities;
    }

}
