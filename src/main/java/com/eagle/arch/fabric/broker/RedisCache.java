package com.eagle.arch.fabric.broker;

import java.io.Serializable;
import java.util.Optional;

public interface RedisCache<T extends Serializable> {
    void put(String key, T t);

    Optional<T> get(String key);
}
