package com.eagle.arch.fabric.broker;

// todo want to try redis pubsub some time
public interface MessagePublisher {
    void publish(final String message);
}