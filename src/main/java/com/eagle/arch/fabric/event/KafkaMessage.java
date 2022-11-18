package com.eagle.arch.fabric.event;

public interface KafkaMessage<T> {
    T getId();
}
