package com.tantsaha.tantsaha.entity.collectivity;

import com.tantsaha.tantsaha.entity.member.Member;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CollectivityStructure {
    private Member president;
    private Member vicePresident;
    private Member treasurer;
    private Member secretary;
}
