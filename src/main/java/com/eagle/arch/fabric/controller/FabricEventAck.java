package com.eagle.arch.fabric.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class FabricEventAck {
    private String id;
    private LocalDateTime timestamp;
    private String inputQuery;
}
