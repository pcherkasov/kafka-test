package com.pavelc.repository;

import com.pavelc.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
  public Client findByClientId(Long id);

}
