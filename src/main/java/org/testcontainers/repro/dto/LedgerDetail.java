package org.testcontainers.repro.dto;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LedgerDetail {
  private Long id;
  private Date createdDate;
  private Date transactionDate;
  private TransactionType transactionType;
  private String transactionLogId;
  private Integer quantity;
  private String controlKey;
}