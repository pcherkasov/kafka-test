package com.pavelc.service;

import com.pavelc.model.Client;
import com.pavelc.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

  private final ClientRepository repository;

  public Client saveClient(Client client) {
    return repository.save(client);
  }

}
