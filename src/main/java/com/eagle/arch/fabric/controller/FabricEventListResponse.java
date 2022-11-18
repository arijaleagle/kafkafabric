package com.eagle.arch.fabric.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class FabricEventListResponse {
    private List<FabricEventResponse> fabricEventList ;

    public FabricEventListResponse(List<FabricEventResponse> fabricEventList) {
        this.fabricEventList = fabricEventList;
    }
}
