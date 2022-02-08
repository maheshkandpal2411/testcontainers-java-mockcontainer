package org.testcontainers.repro.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Transaction {
  private TransactionType type;

  private String json;

  public Transaction withType(TransactionType type){
    this.type = type;
    return this;
  }

  public Transaction withJson(String json){
    this.json = json;
    return this;
  }

}