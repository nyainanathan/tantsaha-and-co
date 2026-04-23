package com.tantsaha.tantsaha.entity.collectivity;

import com.tantsaha.tantsaha.entity.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Collectivity {
    private String id;
    private Integer number;
    private String name;
    private String location;
    private CollectivityStructure structure;
    private List<Member> members;
}
