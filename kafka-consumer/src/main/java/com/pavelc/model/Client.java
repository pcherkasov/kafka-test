package com.pavelc.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "client", schema = "public")
public class Client {

  @Id
  @Column(name = "client_id")
  private Long clientId;

  @Column(name = "email")
  private String email;

  @Column(name = "is_fake")
  private boolean isFake;
}
