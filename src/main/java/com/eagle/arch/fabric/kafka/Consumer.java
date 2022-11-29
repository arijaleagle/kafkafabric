package com.eagle.arch.fabric.kafka;

import com.eagle.arch.fabric.event.FabricEvent;
import com.eagle.arch.fabric.event.QueryStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class Consumer {

    private final Producer producer;

    @Value("${dna.fabric.event.topic}")
    private String topic;

    @Autowired
    public Consumer(Producer producer) {
        this.producer = producer;
    }

    @KafkaListener(topics = {"${dna.fabric.event.topic}"}, groupId = "${kafka.group.id}")
    public void consume(ConsumerRecord<String, FabricEvent> consumerRecord) {
        FabricEvent fabricEvent = consumerRecord.value();
//        log.info("received = " + consumerRecord.value() + " with key " + consumerRecord.key());
        if (fabricEvent.getStatus() != QueryStatus.QUEUED) {
            return;
        }
        fabricEvent.setStatus(QueryStatus.IN_PROGRESS);
        fabricEvent.setTimestamp(LocalDateTime.now());
        producer.produce(topic, fabricEvent);
        // todo call fabric, store to blob, get url and update fabricEvent
        fabricEvent.setStatus(QueryStatus.SUCCESS);
        fabricEvent.setTimestamp(LocalDateTime.now());
        fabricEvent.setStorageId("storageUrl");
        producer.produce(topic, fabricEvent);
    }
}