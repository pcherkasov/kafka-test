package com.pavelc.model;

import com.pavelc.model.enums.OrderType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@Entity
@EntityListeners({AuditingEntityListener.class})
@Table(name = "transaction", schema = "public")
public class Transaction {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(name = "bank")
  private String bank;

  @Column(name = "client_id")
  private Long clientId;

  @Column(name = "order_type")
  @Enumerated(STRING)
  private OrderType orderType;

  @Column(name = "quantity")
  private Integer quantity;

  @Column(name = "price")
  private Double price;

  @Column(name = "total_cost")
  private Double totalCost;

  @Column(name = "created_at")
  private LocalDateTime createdAt;
}
