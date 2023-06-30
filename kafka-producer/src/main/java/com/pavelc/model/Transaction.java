package com.pavelc.model;

import com.pavelc.model.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
  private String bank;
  private Long clientId;
  private OrderType orderType;
  private Integer quantity;
  private Double price;
  private LocalDateTime createdAt;
}
