package com.eagle.arch.fabric.ksql.listener;

import com.eagle.arch.fabric.ksql.dto.KsqlQuery;
import io.confluent.ksql.api.client.BatchedQueryResult;
import io.confluent.ksql.api.client.Client;
import io.confluent.ksql.api.client.ExecuteStatementResult;
import io.confluent.ksql.api.client.impl.ExecuteQueryResponseHandler;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class KTableCreateListener implements ApplicationListener<ContextRefreshedEvent> {

    private final Client client;

    @Autowired
    public KTableCreateListener(Client client) {
        this.client = client;
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
            // todo run this and kafka admin
            String sql =
                 "CREATE SOURCE TABLE IF NOT EXISTS request_view ( " +
                 "  id String PRIMARY KEY, " +
                 "  jibbleQuery String " +
                 " ) WITH ( " +
                 "  kafka_topic='dna-arch-fabric-query-poc', " +
                 "  value_format='JSON'); ";

            sql = "select * from dna_fabric_event_view where id = 'id';";
            Map<String, Object> properties = Collections.singletonMap("auto.offset.reset", "earliest");
            BatchedQueryResult r = client.executeQuery(sql, properties);
            System.out.println(r.get());

//
//
//        KsqlQuery query = new KsqlQuery(sql);
//
//        Mono<String> response = webClient.post()
//                    .uri("/ksql")
//                    .accept(MediaType.APPLICATION_JSON)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .body(Mono.just(query), KsqlQuery.class)
//                    .retrieve()
//                    .bodyToMono(String.class);
//        String result = response.block();


    }
}