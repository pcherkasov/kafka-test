package com.pavelc.service;

import com.pavelc.model.Client;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext
@EmbeddedKafka(partitions = 5, topics = "test-topic")
@SpringBootTest
class ClientMessageServiceIT {
  private static final String CLIENT_TOPIC = "test-topic";
  private static final String CLIENTS_GROUP_ID = "clients";

  @Autowired
  private MessageService service;

  @Autowired
  private EmbeddedKafkaBroker kafkaBroker;

  private KafkaMessageListenerContainer<Long, Client> container;
  private BlockingQueue<ConsumerRecord<Long, Client>> records;

  @BeforeEach
  void setUp() {
    records = new LinkedBlockingQueue<>();
    DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
    typeMapper.addTrustedPackages("com.pavelc");

    JsonDeserializer<Client> valueDeserializer = new JsonDeserializer<>(Client.class);
    valueDeserializer.setTypeMapper(typeMapper);

    ContainerProperties props = new ContainerProperties(CLIENT_TOPIC);
    DefaultKafkaConsumerFactory<Long, Client> consumer =
        new DefaultKafkaConsumerFactory<>(
            KafkaTestUtils.consumerProps(CLIENTS_GROUP_ID, "true", kafkaBroker),
            new LongDeserializer(),
            valueDeserializer
        );
    container = new KafkaMessageListenerContainer<>(consumer, props);
    container.setupMessageListener((MessageListener<Long, Client>) records::add);
    container.start();

    ContainerTestUtils.waitForAssignment(container, kafkaBroker.getPartitionsPerTopic());
  }

  @AfterEach
  void tearDown() {
    container.stop();
  }

  @Test
  void testClientWasSentAndReceived() throws InterruptedException {
    Client client = new Client(1L, "testemail@tst.test");
    service.sendMessage(client);

    ConsumerRecord<Long, Client> received = records.poll(10, TimeUnit.SECONDS);

    assertThat(received).isNotNull();
    assertThat(received.value()).isEqualTo(client);
  }
}
