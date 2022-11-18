package com.eagle.arch.fabric.service;

import com.eagle.arch.fabric.controller.FabricEventAck;
import com.eagle.arch.fabric.controller.FabricEventListResponse;
import com.eagle.arch.fabric.controller.FabricEventResponse;
import com.eagle.arch.fabric.event.FabricEvent;
import com.eagle.arch.fabric.event.QueryStatus;
import com.eagle.arch.fabric.exceptions.QueryNotFoundException;
import com.eagle.arch.fabric.kafka.Producer;
import com.eagle.arch.fabric.ksql.KsqlRepository;
import com.eagle.arch.fabric.ksql.KsqlRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
public class QueryServiceImpl implements QueryService {

    private final Producer producer;

    private final KsqlRepository ksqlRepository;

    @Value("${dna.fabric.event.topic}")
    private String topic;

    @Autowired
    public QueryServiceImpl(Producer producer, KsqlRepositoryImpl ksqlRepository) {
        this.producer = producer;
        this.ksqlRepository = ksqlRepository;
    }

    @Override
    public FabricEventAck executeQuery(String inputQuery) {
        String uuid = UUID.randomUUID().toString();
        FabricEvent event = new FabricEvent(uuid, inputQuery, LocalDateTime.now(), null, QueryStatus.QUEUED, null);
        FabricEvent result = producer.produce(topic, event);
        return new FabricEventAck(result.getId(), result.getTimestamp(), result.getInputQuery());
    }

    @Override
    public FabricEventListResponse getQueryStatusList() {
        return new FabricEventListResponse(ksqlRepository.getAll());
    }

    @Override
    public FabricEventResponse getQueryStatus(String queryId) {
        Optional<FabricEventResponse> status = ksqlRepository.get(queryId);
        if (status.isPresent()) {
            return status.get();
        } else {
            throw new QueryNotFoundException(String.format("Request with id: %s not found", queryId));
        }
    }
}
