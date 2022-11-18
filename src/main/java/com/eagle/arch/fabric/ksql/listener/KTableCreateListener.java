package com.eagle.arch.fabric.ksql.listener;

import com.eagle.arch.fabric.ksql.dto.KsqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class KTableCreateListener implements ApplicationListener<ContextRefreshedEvent> {

    private final WebClient webClient;

    @Autowired
    public KTableCreateListener(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
// todo run this and kafka admin
//            String sql =
//                 "CREATE SOURCE TABLE IF NOT EXISTS request_view ( " +
//                 "  id String PRIMARY KEY, " +
//                 "  jibbleQuery String " +
//                 " ) WITH ( " +
//                 "  kafka_topic='dna-arch-fabric-query-poc', " +
//                 "  value_format='JSON'); ";
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