package org.testcontainers.repro.dto;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;

@NoArgsConstructor
@JsonFormat(shape = STRING)
public enum TransactionType {
  SHIP_CONFIRM,
  PURCHASE_ORDER_RECEIPT,
  INVENTORY_ADJUSTMENT
}