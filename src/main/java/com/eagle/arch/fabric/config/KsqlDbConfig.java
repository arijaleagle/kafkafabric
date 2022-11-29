package com.eagle.arch.fabric.config;

import io.confluent.ksql.api.client.Client;
import io.confluent.ksql.api.client.ClientOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KsqlDbConfig {

    @Value("${ksqldb.host}")
    private String ksqlDbHost;

    @Value("${ksqldb.port}")
    private int ksqlDbPort;

    @Value("${ksqldb.username}")
    private String userName;

    @Value("${ksqldb.password}")
    private String password;

    // todo this client is not working currently
    @Bean
    public Client ksqlDbClient() {
        ClientOptions options = ClientOptions.create()
                .setBasicAuthCredentials(userName, password)
                .setHost(ksqlDbHost)
                .setPort(ksqlDbPort)
                .setUseTls(true)
                .setUseAlpn(true);
        return Client.create(options);
    }
}
