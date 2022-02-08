package org.testcontainers.repro.dto;

import java.util.Date;
import java.util.Set;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Data
@JsonPropertyOrder({"id", "supplierId", "itemId", "transactionDate", "lastActivityDate", "quantity", "status", "ledgerDetails"})
public class Ledger {
  private Long id;

  private Long supplierId;

  private Long itemId;

  private Date transactionDate;

  private Date lastActivityDate;

  private Integer quantity;

  private LedgerStatus status;

  @JsonProperty("ledgerDetail")
  private Set<LedgerDetail> ledgerDetails;
}
