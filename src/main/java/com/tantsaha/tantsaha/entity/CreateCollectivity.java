package com.tantsaha.tantsaha.entity;

import lombok.Getter;

import java.util.List;

@Getter
public class CreateCollectivity {
    private String location;
    private List<String> members;
    private Boolean federationApproval;
    private CreateCollectivityStructure structure;
}