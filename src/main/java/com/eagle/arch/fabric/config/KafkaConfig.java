package com.eagle.arch.fabric.config;

import com.eagle.arch.fabric.event.FabricEvent;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${kafka.properties.bootstrap.servers}")
    private String bootstrapServers;

    @Value("${kafka.properties.sasl.mechanism}")
    private String saslMechanism;

    @Value("${kafka.properties.sasl.jaas.config}")
    private String jaasConfig;

    @Value("${kafka.properties.security.protocol}")
    private String securityProtocol;


    private Map<String, Object> getCommonConfigs() {
        return Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                SaslConfigs.SASL_MECHANISM, saslMechanism,
                SaslConfigs.SASL_JAAS_CONFIG, jaasConfig,
                CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, securityProtocol,
                JsonDeserializer.TRUSTED_PACKAGES, "*");
    }


    @Bean
    public ProducerFactory<String, FabricEvent> producerFactory() {
        Map<String, Object> configProps = new HashMap<>(getCommonConfigs());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }


    @Bean
    public KafkaTemplate<String, FabricEvent> kafkaTemplate(ProducerFactory<String, FabricEvent> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }


    @Bean
    public ConsumerFactory<String, FabricEvent> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>(getCommonConfigs());
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, FabricEvent> kafkaListenerContainerFactory(
            ConsumerFactory<String, FabricEvent> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, FabricEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

}
