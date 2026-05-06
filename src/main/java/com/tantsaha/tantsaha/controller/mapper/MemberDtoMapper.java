package com.tantsaha.tantsaha.controller.mapper;

import com.tantsaha.tantsaha.controller.dto.CreateMember;
import com.tantsaha.tantsaha.controller.dto.MemberOccupation;
import com.tantsaha.tantsaha.entity.Collectivity;
import com.tantsaha.tantsaha.entity.Gender;
import com.tantsaha.tantsaha.entity.Member;
import com.tantsaha.tantsaha.exception.NotFoundException;
import com.tantsaha.tantsaha.repository.CollectivityRepository;
import com.tantsaha.tantsaha.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MemberDtoMapper {
    private final CollectivityRepository collectivityRepository;
    private final MemberRepository memberRepository;

    public Member mapToEntity(CreateMember createMemberDto) {
        Optional<Collectivity> optionalCollectivity = collectivityRepository.findById(createMemberDto.getCollectivityIdentifier());

        if (optionalCollectivity.isEmpty()) {
            throw new NotFoundException("Collectivity.id=" + createMemberDto.getCollectivityIdentifier() + " not found");
        }

        var refereeMembers = createMemberDto.getReferees().stream()
                .map(refereeId -> {
                    var memberReferee = memberRepository.findById(refereeId).orElseThrow(
                            () -> new NotFoundException("Member.id=" + refereeId + "not found"));
                    memberReferee.addCollectivity(optionalCollectivity.get());
                    return memberReferee;
                })
                .toList();

        var member = Member.builder()
                .firstName(createMemberDto.getFirstName())
                .lastName(createMemberDto.getLastName())
                .birthDate(createMemberDto.getBirthDate())
                .gender(createMemberDto.getGender() == null ? null : Gender.valueOf(createMemberDto.getGender().name()))
                .occupation(createMemberDto.getOccupation() == null ? null : com.tantsaha.tantsaha.entity.MemberOccupation.valueOf(createMemberDto.getOccupation().name()))
                .address(createMemberDto.getAddress())
                .profession(createMemberDto.getProfession())
                .phoneNumber(createMemberDto.getPhoneNumber())
                .email(createMemberDto.getEmail())
                .registrationFeePaid(createMemberDto.getRegistrationFeePaid())
                .membershipDuesPaid(createMemberDto.getMembershipDuesPaid())
                .build();

        member.addCollectivity(optionalCollectivity.get());
        member.addReferees(refereeMembers);

        return member;
    }

    public com.tantsaha.tantsaha.controller.dto.Member mapToDto(Member member) {
        if (member == null) {
            return null;
        }
        return com.tantsaha.tantsaha.controller.dto.Member.builder()
                .id(member.getId())
                .firstName(member.getFirstName())
                .lastName(member.getLastName())
                .birthDate(member.getBirthDate())
                .address(member.getAddress())
                .profession(member.getProfession())
                .phoneNumber(member.getPhoneNumber())
                .profession(member.getProfession())
                .email(member.getEmail())
                .gender(member.getGender() == null ? null : com.tantsaha.tantsaha.controller.dto.Gender.valueOf(member.getGender().name()))
                .occupation(member.getOccupation() == null ? null : MemberOccupation.valueOf(member.getOccupation().name()))
                .referees(member.getReferees() == null ? List.of() : member.getReferees().stream()
                        .map(this::mapToDto)
                        .toList())
                .build();
    }
}
