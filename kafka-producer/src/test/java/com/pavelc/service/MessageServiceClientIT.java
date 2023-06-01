package com.pavelc.service;

import com.pavelc.model.Client;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest
@EmbeddedKafka(bootstrapServersProperty = "localhost:9055")
public class MessageServiceClientIT {

  private static final String CLIENT_TOPIC = "test-topic";
  private static final String CLIENTS_GROUP_ID = "clients";

  @Autowired
  private MessageService service;

  private KafkaMessageListenerContainer<Long, Client> container;
  private BlockingQueue<ConsumerRecord<Long, Client>> records;

  @ClassRule
  public static EmbeddedKafkaRule embeddedKafkaRule = new EmbeddedKafkaRule(1, true, 5, CLIENT_TOPIC);

  @Before
  public void setUp() {

    EmbeddedKafkaBroker kafka = embeddedKafkaRule.getEmbeddedKafka();
    kafka.kafkaPorts(9055);

    DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
    typeMapper.addTrustedPackages("com.pavelc.model");

    JsonDeserializer<Client> valueDeserializer = new JsonDeserializer<>(Client.class);
    valueDeserializer.setTypeMapper(typeMapper);

    records = new LinkedBlockingQueue<>();
    ContainerProperties props = new ContainerProperties(CLIENT_TOPIC);
    DefaultKafkaConsumerFactory<Long, Client> consumer =
        new DefaultKafkaConsumerFactory<>(
            KafkaTestUtils.consumerProps(CLIENTS_GROUP_ID, "false", kafka),
            new LongDeserializer(),
            valueDeserializer
        );
    container = new KafkaMessageListenerContainer<>(consumer, props);
    container.setupMessageListener((MessageListener<Long, Client>) record -> records.add(record));
    container.start();

    ContainerTestUtils.waitForAssignment(container, kafka.getPartitionsPerTopic());
  }

  @Test
  public void successfulSendingOfClientMessage() throws InterruptedException {

    Client client = new Client(1L, "testemail@tst.test");

    service.sendMessage(client);

    ConsumerRecord<Long, Client> received = records.poll(10, TimeUnit.SECONDS);

    assertThat(received).isNotNull();
    assertThat(received.value()).isEqualTo(client);

  }

  @After
  public void tearDown() {

    container.stop();
  }


}
