package com.eagle.arch.fabric.kafka;

import com.eagle.arch.fabric.event.FabricEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
//@Slf4j
public class Producer {

    private final KafkaTemplate<String, FabricEvent> kafkaTemplate;

    public FabricEvent produce(String topic, FabricEvent message) {
        kafkaTemplate.send(topic, message.getId(), message);
        return message;
    }
}

// todo figure out a way to make it generic
//public class Producer<T> {
//
//    private final KafkaTemplate<T, FabricMessage> kafkaTemplate;
//
//    public void produce(String topic, KafkaMessage<T> message) {
//        kafkaTemplate.send(topic, message.getId(), message);
//    }
//}

