package com.eagle.arch.fabric.broker;

import com.eagle.arch.fabric.event.FabricEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

//todo this itself can be generic, will do later
@Component
public class RedisCacheImpl implements RedisCache<FabricEvent> {

    private final RedisTemplate<String, FabricEvent> redisTemplate;

    @Autowired
    public RedisCacheImpl(RedisTemplate<String, FabricEvent> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void put(String key, FabricEvent val) {
        redisTemplate.opsForValue().set(key, val);
    }

    @Override
    public Optional<FabricEvent> get(String key) {
        FabricEvent val = redisTemplate.opsForValue().get(key);
        if (val == null) {
            return Optional.empty();
        }
        return Optional.of(val);
    }
}
