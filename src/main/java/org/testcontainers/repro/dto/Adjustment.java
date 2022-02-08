package org.testcontainers.repro.dto;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.testcontainers.repro.dto.deser.LongDeserializer;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;


@Data
@NoArgsConstructor
@JsonPropertyOrder({"transactionId", "itemId", "id", "sku", "timestamp", "quantityOnHandAdjustment", "reasonCode", "type", "rma"})
public class Adjustment {
  private String transactionId;

  @JsonSerialize(using = ToStringSerializer.class)
  @JsonDeserialize(using = LongDeserializer.class)
  private Long itemId;

  @JsonSerialize(using = ToStringSerializer.class)
  @JsonDeserialize(using = LongDeserializer.class)
  private Long id;

  private String sku;

  private Date timestamp;

  private int quantityOnHandAdjustment;

  private String reasonCode;

  private AdjustmentType type;

  private String rma;

}