package com.eagle.arch.fabric.event;

import com.eagle.arch.fabric.controller.FabricEventResponse;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class FabricEvent implements KafkaMessage<String>, Serializable {

    @Serial
    private static final long serialVersionUID = 7156526077883284623L;

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

    public FabricEventResponse toFabricEventResponse() {
        return new FabricEventResponse(id, inputQuery, timestamp.toString(), storageId, status.toString(), errorMessage);
    }
}

