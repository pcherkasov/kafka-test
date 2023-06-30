package com.pavelc.api;

import com.pavelc.model.Client;
import com.pavelc.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/clients")
public class ClientController {

  private final MessageService service;

  @PostMapping
  public ResponseEntity<Void> createClient(@RequestBody Client client) {
    service.sendMessage(client);
    return ResponseEntity.noContent().build();
  }

}
