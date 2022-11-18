package com.eagle.arch.fabric.event;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class FabricEvent implements KafkaMessage<String>{
    private String id;
    private String inputQuery;
    private LocalDateTime timestamp;
    private String storageId;
    private QueryStatus status;
    private String errorMessage;

    @Override
    public String getId() {
        return id;
    }
}

