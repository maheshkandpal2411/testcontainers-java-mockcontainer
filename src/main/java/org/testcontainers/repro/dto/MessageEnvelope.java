package org.testcontainers.repro.dto;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(doNotUseGetters = true, onlyExplicitlyIncluded = true)
public class MessageEnvelope<T> {
  private Map<String, String> headers = new HashMap<>();

  @NotNull
  @EqualsAndHashCode.Include
  private T payload;

  public MessageEnvelope(final T payload) {
    this.payload = payload;
  }

  /**
   *
   * @param headers key-value map of metadata properties that we care about
   * @param payload DTO
   */
  public MessageEnvelope(final Map<String, String> headers, final T payload) {
    addHeaders(headers);
    this.payload = payload;
  }

  public void addHeader(String name, String value) {
    this.headers.put(name, value);
  }

  public void addHeaders(Map<String, String> headers) {
    this.headers.putAll(headers);
  }

  @Override
  public String toString() {
    return "[{Headers:" + headers + "}, {Payload:" + payload+ "}]";
  }
}
