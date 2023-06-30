package com.pavelc.producer;

import com.pavelc.model.Client;
import com.pavelc.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestProducer {
  private final KafkaTemplate<Long, Object> template;

  @Value(value = "${spring.kafka.topic.name.client}")
  private String clientTopicName;

  @Value(value = "${spring.kafka.topic.name.transaction}")
  private String transactionTopicName;

  public void sendMessage(Client client) {
    template.send(clientTopicName, client.getClientId(), client);
  }

  public void sendMessage(Transaction transaction) {
    template.send(transactionTopicName, transaction.getClientId(), transaction);
  }
}
