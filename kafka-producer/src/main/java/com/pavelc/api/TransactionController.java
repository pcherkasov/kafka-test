package com.pavelc.api;

import com.pavelc.model.Transaction;
import com.pavelc.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

  private final MessageService service;

  @PostMapping
  public ResponseEntity<Void> createTransaction(@RequestBody Transaction transaction) {
    transaction.setCreatedAt(LocalDateTime.now());
    service.sendMessage(transaction);
    return ResponseEntity.noContent().build();
  }
}
