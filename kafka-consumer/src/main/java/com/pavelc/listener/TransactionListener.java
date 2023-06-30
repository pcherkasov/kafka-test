package com.pavelc.listener;

import com.pavelc.model.Transaction;
import com.pavelc.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@KafkaListener(topics = "transaction", groupId = "transactions")
public class TransactionListener {

  private final TransactionService service;

  @KafkaHandler
  public void handleTransaction(Transaction tr) {
    log.info("Received transaction with clientId: [{}] and price: [{}]", tr.getClientId(), tr.getPrice());
    Transaction newTransaction = service.saveTransaction(tr);
    log.info("New transaction was created with id: [{}]", newTransaction.getId());
  }
}
