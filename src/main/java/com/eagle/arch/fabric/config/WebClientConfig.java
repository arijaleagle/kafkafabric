package com.eagle.arch.fabric.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${ksqldb.server.url}")
    private String ksqldbUrl;

    @Value("${ksqldb.username}")
    private String userName;

    @Value("${ksqldb.password}")
    private String password;

    @Bean
    public WebClient getWebClient() {
        return WebClient.builder()
                .baseUrl(ksqldbUrl)
                .defaultHeaders(header -> header.setBasicAuth(userName, password))
                .build();
    }


}
