package org.testcontainers.repro.dto.deser;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.type.LogicalType;

public class LongDeserializer extends StdScalarDeserializer<Long> {
  private static final long serialVersionUID = 1L;

  public LongDeserializer() {
    super(Long.class);
  }

  public Long deserialize(JsonParser p, DeserializationContext context) throws IOException {
    if (p.isExpectedNumberIntToken()) {
      return p.getLongValue();
    } else {
      Long l = this._parseLong(p, context, Long.class);
      return l == null ? null : (long) l.intValue();
    }
  }

  @Override
  public LogicalType logicalType() {
    return LogicalType.Integer;
  }

  @Override
  public Object getEmptyValue(DeserializationContext context) {
    return 0L;
  }
}