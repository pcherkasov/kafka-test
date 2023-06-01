package com.pavelc.service;

import com.pavelc.model.Client;
import com.pavelc.model.Transaction;
import com.pavelc.repository.ClientRepository;
import com.pavelc.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {

  private final TransactionRepository transactionRepository;
  private final ClientRepository clientRepository;

  public Transaction saveTransaction(Transaction transaction) {

    transaction.setTotalCost(transaction.getPrice() * transaction.getQuantity());
    try {
      return transactionRepository.save(transaction);
    } catch (DataIntegrityViolationException ex) {
      log.warn("Transaction with not existing client was received. Client id: [{}]", transaction.getClientId());
      Client fakeClient = new Client();
      fakeClient.setClientId(transaction.getClientId());
      fakeClient.setFake(true);
      clientRepository.save(fakeClient);
      log.warn("Fake client with id: [{}] was created", fakeClient.getClientId());
      return transactionRepository.save(transaction);
    }
  }
}
