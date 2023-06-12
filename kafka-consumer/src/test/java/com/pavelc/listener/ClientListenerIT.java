package com.pavelc.listener;

import com.pavelc.model.Client;
import com.pavelc.producer.TestProducer;
import com.pavelc.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@EmbeddedKafka
@SpringBootTest
public class ClientListenerIT {

  @Autowired
  private ClientRepository repository;

  @Autowired
  private TestProducer producer;

  @Test
  public void handleClientMessageAndSaveItToDB() throws InterruptedException {
    Client createdClient = new Client();
    createdClient.setClientId(12L);
    createdClient.setEmail("test@mail.test");

    producer.sendMessage(createdClient);
    Thread.sleep(1_000);

    Client savedClient = repository.findByClientId(createdClient.getClientId());

    assertNotNull(savedClient);
    assertEquals(createdClient, savedClient);

  }

}
