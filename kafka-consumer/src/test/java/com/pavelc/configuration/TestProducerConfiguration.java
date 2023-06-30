package com.pavelc.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.*;
import static org.springframework.kafka.support.serializer.JsonSerializer.TYPE_MAPPINGS;

@Configuration
public class TestProducerConfiguration {

  @Value(value = "${spring.kafka.bootstrap-servers}")
  private String bootstrapAddress;

  @Value(value = "${spring.kafka.topic.name.client}")
  private String clientTopicName;

  @Value(value = "${spring.kafka.topic.name.transaction}")
  private String transactionTopicName;

  @Bean
  public ProducerFactory<Long, Object> producerFactory() {

    return new DefaultKafkaProducerFactory<>(
        Map.of(
            BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
            KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class,
            VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class,
            TYPE_MAPPINGS, "client:com.pavelc.model.Client, transaction:com.pavelc.model.Transaction"
        )
    );
  }

  @Bean
  public KafkaTemplate<Long, Object> kafkaTemplate() {

    return new KafkaTemplate<>(producerFactory());
  }

  @Bean
  public KafkaAdmin kafkaAdmin() {

    return new KafkaAdmin(
        Map.of(
            BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress
        )
    );
  }

  @Bean
  public NewTopic clientTopic() {

    return new NewTopic(clientTopicName, 5, (short) 1);
  }

  @Bean
  public NewTopic transactionTopic() {

    return new NewTopic(transactionTopicName, 5, (short) 1);
  }
}
