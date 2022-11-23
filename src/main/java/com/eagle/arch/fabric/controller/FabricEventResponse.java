package com.eagle.arch.fabric.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FabricEventResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 7156526077883281623L;

    private String id;
    private String inputQuery;
    private String timestamp;
    private String storageId;
    private String status;
    private String errorMessage;

}
