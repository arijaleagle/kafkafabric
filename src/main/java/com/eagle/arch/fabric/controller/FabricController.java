package com.eagle.arch.fabric.controller;

import com.eagle.arch.fabric.service.QueryService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestController
@AllArgsConstructor
public class FabricController {

    private final QueryService queryService;

    @GetMapping(value = "/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public FabricEventAck query(@RequestParam String inputQuery, @RequestParam boolean redis) {
        return queryService.executeQuery(inputQuery, redis);
    }

    @GetMapping(value = "/query/result", produces = MediaType.APPLICATION_JSON_VALUE)
    public FabricEventListResponse getQueryList() {
        return queryService.getQueryStatusList();
    }

    @GetMapping(value = "/query/result/{queryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public FabricEventResponse getQueryList(@PathVariable @NonNull String queryId, @RequestParam boolean redis) {
        return queryService.getQueryStatus(queryId, redis);
    }

    @Autowired
    private RedisTemplate<String, FabricEventAck> redisTemplate;

    @GetMapping("/redis")
    public String getMessage(@RequestParam String input) {
        redisTemplate.opsForValue().set(input, new FabricEventAck("id", LocalDateTime.now(), input));
        return "OK";
    }

    @GetMapping("/redis/{key}")
    public FabricEventAck getString(@PathVariable("key") String key) {
        FabricEventAck value = redisTemplate.opsForValue().get(key);

        if (value == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "key not found");
        }

        return value;
    }
}
