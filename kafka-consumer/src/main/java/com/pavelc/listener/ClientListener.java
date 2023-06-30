package com.pavelc.listener;

import com.pavelc.model.Client;
import com.pavelc.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@KafkaListener(topics = "client", groupId = "clients")
public class ClientListener {

  private final ClientService service;

  @KafkaHandler
  public void handleClient(Client client) {
    log.info("Received client with id: [{}].", client.getClientId());
    client.setFake(false);
    Client newClient = service.saveClient(client);
    log.info("Client created: [{}]", newClient.getClientId());
  }

}
