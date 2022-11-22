package com.eagle.arch.fabric.controller;

import com.eagle.arch.fabric.service.QueryService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class FabricController {

    private final QueryService queryService;

    @GetMapping(value = "/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public FabricEventAck query(@RequestParam String inputQuery, @RequestParam boolean persistTime) {
        return queryService.executeQuery(inputQuery, persistTime);
    }

    @GetMapping(value = "/query/result", produces = MediaType.APPLICATION_JSON_VALUE)
    public FabricEventListResponse getQueryList() {
        return queryService.getQueryStatusList();
    }

    @GetMapping(value = "/query/result/{queryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public FabricEventResponse getQueryList(@PathVariable @NonNull String queryId, @RequestParam boolean persistTime) {
        return queryService.getQueryStatus(queryId, persistTime);
    }
}
