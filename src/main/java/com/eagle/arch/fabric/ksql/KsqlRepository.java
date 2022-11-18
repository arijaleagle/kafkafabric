package com.eagle.arch.fabric.ksql;

import com.eagle.arch.fabric.controller.FabricEventResponse;

import java.util.List;
import java.util.Optional;

public interface KsqlRepository {

    List<FabricEventResponse> getAll();

    Optional<FabricEventResponse> get(String queryId);
}
