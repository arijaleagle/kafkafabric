package com.eagle.arch.fabric.service;

import com.eagle.arch.fabric.controller.FabricEventAck;
import com.eagle.arch.fabric.controller.FabricEventListResponse;
import com.eagle.arch.fabric.controller.FabricEventResponse;

public interface QueryService {
    FabricEventAck executeQuery(String inputQuery, boolean persistTime);

    FabricEventListResponse getQueryStatusList();

    FabricEventResponse getQueryStatus(String queryId, boolean persistTime);
}
