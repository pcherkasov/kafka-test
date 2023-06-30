package com.pavelc.listener;

import com.pavelc.model.Client;
import com.pavelc.model.Transaction;
import com.pavelc.producer.TestProducer;
import com.pavelc.repository.ClientRepository;
import com.pavelc.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.time.LocalDateTime;

import static com.pavelc.model.enums.OrderType.INCOME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EmbeddedKafka
@SpringBootTest
@Slf4j
public class TransactionListenerIT {

  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private ClientRepository clientRepository;

  @Autowired
  private TestProducer producer;

  @Test
  public void handleNewTransactionWithExistingClient() throws InterruptedException {
    Client client = new Client();
    client.setClientId(10L);
    client.setEmail("test@mail.test");
    client.setFake(false);

    Transaction producedTransaction = new Transaction();
    producedTransaction.setClientId(client.getClientId());
    producedTransaction.setBank("TEST BANK");
    producedTransaction.setPrice(12.12);
    producedTransaction.setQuantity(10);
    producedTransaction.setCreatedAt(LocalDateTime.now());
    producedTransaction.setOrderType(INCOME);

    clientRepository.save(client);

    producer.sendMessage(producedTransaction);
    Thread.sleep(1_000);

    Client clientFromDBAfterSavingTransaction = clientRepository.findByClientId(producedTransaction.getClientId());
    Transaction consumedTransaction = transactionRepository.findByClientId(producedTransaction.getClientId()).get(0);

    assertNotNull(consumedTransaction);
    assertFalse(clientFromDBAfterSavingTransaction.isFake());
    assertEquals(producedTransaction.getClientId(), consumedTransaction.getClientId());
  }

  @Test
  public void handleNewTransactionWithNotExistingClient() throws InterruptedException {
    Transaction producedTransaction = new Transaction();
    producedTransaction.setClientId(404L);
    producedTransaction.setBank("TEST BANK");
    producedTransaction.setPrice(12.12);
    producedTransaction.setQuantity(10);
    producedTransaction.setCreatedAt(LocalDateTime.now());
    producedTransaction.setOrderType(INCOME);

    producer.sendMessage(producedTransaction);
    Thread.sleep(10_000);

    Client clientFromDBAfterSavingTransaction = clientRepository.findByClientId(producedTransaction.getClientId());
    log.info("client from db; [{}]", clientFromDBAfterSavingTransaction);
    Transaction consumedTransaction = transactionRepository.findByClientId(producedTransaction.getClientId()).get(0);

    assertNotNull(consumedTransaction);
    assertTrue(clientFromDBAfterSavingTransaction.isFake());
    assertEquals(producedTransaction.getClientId(), consumedTransaction.getClientId());

  }
}
