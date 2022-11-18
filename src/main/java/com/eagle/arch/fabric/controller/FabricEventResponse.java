package com.eagle.arch.fabric.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FabricEventResponse {
    private String id;
    private String inputQuery;
    private String timestamp;
    private String storageId;
    private String status;
    private String errorMessage;

}
