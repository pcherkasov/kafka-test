package com.pavelc.service;

import com.pavelc.model.Transaction;
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

import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static com.pavelc.model.enums.OrderType.INCOME;
import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext
@EmbeddedKafka(partitions = 5, topics = "test-topic")
@SpringBootTest
class TransactionMessageServiceIT {
  private static final String TRANSACTION_TOPIC = "test-topic";
  private static final String TRANSACTIONS_GROUP_ID = "transactions";

  @Autowired
  private MessageService service;

  @Autowired
  private EmbeddedKafkaBroker kafkaBroker;

  private KafkaMessageListenerContainer<Long, Transaction> container;
  private BlockingQueue<ConsumerRecord<Long, Transaction>> records;

  @BeforeEach
  void setUp() {
    records = new LinkedBlockingQueue<>();
    DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
    typeMapper.addTrustedPackages("com.pavelc");

    JsonDeserializer<Transaction> valueDeserializer = new JsonDeserializer<>(Transaction.class);
    valueDeserializer.setTypeMapper(typeMapper);

    ContainerProperties props = new ContainerProperties(TRANSACTION_TOPIC);
    DefaultKafkaConsumerFactory<Long, Transaction> consumer =
        new DefaultKafkaConsumerFactory<>(
            KafkaTestUtils.consumerProps(TRANSACTIONS_GROUP_ID, "true", kafkaBroker),
            new LongDeserializer(),
            valueDeserializer
        );
    container = new KafkaMessageListenerContainer<>(consumer, props);
    container.setupMessageListener((MessageListener<Long, Transaction>) records::add);
    container.start();

    ContainerTestUtils.waitForAssignment(container, kafkaBroker.getPartitionsPerTopic());
  }

  @AfterEach
  void tearDown() {
    container.stop();
  }

  @Test
  void testClientWasSentAndReceived() throws InterruptedException {
    Transaction transaction = new Transaction(
        "test bank",
        1L,
        INCOME,
        123,
        12.23,
        LocalDateTime.now()
    );
    service.sendMessage(transaction);

    ConsumerRecord<Long, Transaction> received = records.poll(10, TimeUnit.SECONDS);

    assertThat(received).isNotNull();
    assertThat(received.value()).isEqualTo(transaction);
  }
}
