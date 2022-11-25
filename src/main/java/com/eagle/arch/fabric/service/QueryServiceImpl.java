package com.eagle.arch.fabric.service;

import com.eagle.arch.fabric.broker.RedisCache;
import com.eagle.arch.fabric.controller.FabricEventAck;
import com.eagle.arch.fabric.controller.FabricEventListResponse;
import com.eagle.arch.fabric.controller.FabricEventResponse;
import com.eagle.arch.fabric.event.FabricEvent;
import com.eagle.arch.fabric.event.QueryStatus;
import com.eagle.arch.fabric.exceptions.QueryNotFoundException;
import com.eagle.arch.fabric.kafka.Producer;
import com.eagle.arch.fabric.ksql.KsqlRepository;
import com.eagle.arch.fabric.ksql.KsqlRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class QueryServiceImpl implements QueryService {

    private final Producer producer;

    private final KsqlRepository ksqlRepository;

    private final RedisCache<FabricEvent> redisCache;

    @Value("${dna.fabric.event.topic}")
    private String topic;

    @Autowired
    public QueryServiceImpl(Producer producer, KsqlRepositoryImpl ksqlRepository, RedisCache<FabricEvent> redisCache) {
        this.producer = producer;
        this.ksqlRepository = ksqlRepository;
        this.redisCache = redisCache;
    }

    @Override
    public FabricEventAck executeQuery(String inputQuery, boolean redis) {
        long start = System.currentTimeMillis();
        String uuid = UUID.randomUUID().toString();
        FabricEvent event = new FabricEvent(uuid, inputQuery, LocalDateTime.now(), null, QueryStatus.QUEUED, null);
        FabricEvent result = producer.produce(topic, event);
        // persist into redis, todo need transaction?
        if (redis) {
            redisCache.put(uuid, event);
        }
        long end = System.currentTimeMillis();
        long diff = end - start;
        String logMessage = String.format("Time to produce message for id %s is %s.", uuid, diff);
        if (redis) {
            logMessage += " Using redis";
        }
        log.info(logMessage);
        return new FabricEventAck(result.getId(), result.getTimestamp(), result.getInputQuery());
    }

    @Override
    public FabricEventListResponse getQueryStatusList() {
        return new FabricEventListResponse(ksqlRepository.getAll());
    }

    @Override
    public FabricEventResponse getQueryStatus(String queryId, boolean redis) {
        long start = System.currentTimeMillis();
        FabricEventResponse response;
        if (redis) {
            Optional<FabricEvent> val = redisCache.get(queryId);
            if (val.isPresent()) {
                response = val.get().toFabricEventResponse();
            } else {
                throw new QueryNotFoundException(String.format("Request with id: %s not found", queryId));
            }
        } else {
            Optional<FabricEventResponse> status = ksqlRepository.get(queryId);
            if (status.isPresent()) {
                response = status.get();
            } else {
                throw new QueryNotFoundException(String.format("Request with id: %s not found", queryId));
            }
        }
        long end = System.currentTimeMillis();
        long diff = end - start;
        String logMessage = String.format("Time to check status for id %s is %s.", queryId, diff);
        if (redis) {
            logMessage += " Using redis";
        }
        log.info(logMessage);

        return response;
    }
}
