package org.testcontainers.repro.discovery.exception;

public class DiscoveryClientException extends RuntimeException {
  public DiscoveryClientException(String message, Throwable cause) {
    super(message, cause);
  }

  public DiscoveryClientException(String message) {
    super(message);
  }
}