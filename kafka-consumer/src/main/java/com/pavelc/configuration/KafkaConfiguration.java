package com.pavelc.configuration;

import com.pavelc.model.Client;
import com.pavelc.model.Transaction;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;
import static org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID;

@Configuration
public class KafkaConfiguration {

  @Value(value = "${spring.kafka.bootstrap-servers}")
  private String bootstrapAddress;

  @Bean
  public RecordMessageConverter typeConverter() {

    StringJsonMessageConverter converter = new StringJsonMessageConverter();
    DefaultJackson2JavaTypeMapper mapper = new DefaultJackson2JavaTypeMapper();
    mapper.setTypePrecedence(TYPE_ID);
    mapper.addTrustedPackages("com.pavelc.model");
    mapper.setIdClassMapping(Map.of(
        "client", Client.class,
        "transaction", Transaction.class
    ));
    converter.setTypeMapper(mapper);
    return converter;
  }

  @Bean
  public ConsumerFactory<Long, Object> consumerFactory() {

    return new DefaultKafkaConsumerFactory<>(Map.of(
        BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
        KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class,
        VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
    ));
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<Long, Object> listenerContainerFactory() {

    ConcurrentKafkaListenerContainerFactory<Long, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    factory.setRecordMessageConverter(typeConverter());
    return factory;
  }
}
