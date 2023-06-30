package com.pavelc.service;

import com.pavelc.model.Client;
import com.pavelc.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {

  private final KafkaTemplate<Long, Object> template;

  @Value(value = "${spring.kafka.topic.name.client}")
  private String clientTopicName;

  @Value(value = "${spring.kafka.topic.name.transaction}")
  private String transactionTopicName;

  public void sendMessage(Client client) {

    template
        .send(clientTopicName, client.getClientId(), client)
        .whenComplete((result, ex) -> {
          if (ex == null) {
            log.info("Client with ID: [{}] was sent to partition: [{}]", client.getClientId(), result.getRecordMetadata().partition());
          } else {
            log.error("Client with ID: [{}] was not sent. Reason: ", client.getClientId(), ex);
          }
        });
  }

  public void sendMessage(Transaction transaction) {

    template
        .send(transactionTopicName, transaction.getClientId(), transaction)
        .whenComplete((result, ex) -> {
          if (ex == null) {
            log.info("Transaction for client with Id: [{}] was sent to partition: [{}]", transaction.getClientId(), result.getRecordMetadata().partition());
          } else {
            log.error("Transaction for clientID: [{}] was not sent. Reason: ", transaction.getClientId(), ex);
          }
        });
  }
}
