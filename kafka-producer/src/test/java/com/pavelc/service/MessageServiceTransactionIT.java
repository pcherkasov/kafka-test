//package com.pavelc.service;
//
//import com.pavelc.model.Transaction;
//import com.pavelc.model.enums.OrderType;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.common.serialization.LongDeserializer;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.ClassRule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.listener.ContainerProperties;
//import org.springframework.kafka.listener.KafkaMessageListenerContainer;
//import org.springframework.kafka.listener.MessageListener;
//import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
//import org.springframework.kafka.support.serializer.JsonDeserializer;
//import org.springframework.kafka.test.EmbeddedKafkaBroker;
//import org.springframework.kafka.test.context.EmbeddedKafka;
//import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
//import org.springframework.kafka.test.utils.ContainerTestUtils;
//import org.springframework.kafka.test.utils.KafkaTestUtils;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.time.LocalDateTime;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.TimeUnit;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@RunWith(SpringRunner.class)
//@DirtiesContext
//@SpringBootTest
//@EmbeddedKafka(bootstrapServersProperty = "localhost:9055")
//public class MessageServiceTransactionIT {
//
//  private static final String TRANSACTION_TOPIC = "test-topic";
//  private static final String TRANSACTIONS_GROUP_ID = "transactions";
//
//  @Autowired
//  private MessageService service;
//
//  private KafkaMessageListenerContainer<Long, Transaction> container;
//  private BlockingQueue<ConsumerRecord<Long, Transaction>> records;
//
//  @ClassRule
//  public static EmbeddedKafkaRule embeddedKafkaRule = new EmbeddedKafkaRule(1, true, 5, TRANSACTION_TOPIC);
//
//  @Before
//  public void setUp() {
//
//    EmbeddedKafkaBroker kafka = embeddedKafkaRule.getEmbeddedKafka();
//    kafka.kafkaPorts(9055);
//    DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
//    typeMapper.addTrustedPackages("com.pavelc.model");
//
//    JsonDeserializer<Transaction> valueDeserializer = new JsonDeserializer<>(Transaction.class);
//    valueDeserializer.setTypeMapper(typeMapper);
//
//    records = new LinkedBlockingQueue<>();
//    ContainerProperties props = new ContainerProperties(TRANSACTION_TOPIC);
//    DefaultKafkaConsumerFactory<Long, Transaction> consumer =
//        new DefaultKafkaConsumerFactory<>(
//            KafkaTestUtils.consumerProps(TRANSACTIONS_GROUP_ID, "false", kafka),
//            new LongDeserializer(),
//            valueDeserializer
//        );
//    container = new KafkaMessageListenerContainer<>(consumer, props);
//    container.setupMessageListener((MessageListener<Long, Transaction>) record -> records.add(record));
//    container.start();
//
//    ContainerTestUtils.waitForAssignment(container, kafka.getPartitionsPerTopic());
//  }
//
//  @Test
//  public void successfulSendingOfTransactionMessage() throws InterruptedException {
//
//    Transaction transaction =
//        new Transaction("test-bank", 1L, OrderType.INCOME, 23, 23.23, LocalDateTime.now());
//
//    service.sendMessage(transaction);
//
//    ConsumerRecord<Long, Transaction> received = records.poll(10, TimeUnit.SECONDS);
//
//    assertThat(received).isNotNull();
//    assertThat(received.value()).isEqualTo(transaction);
//
//  }
//
//  @After
//  public void tearDown() {
//
//    container.stop();
//  }
//
//}
