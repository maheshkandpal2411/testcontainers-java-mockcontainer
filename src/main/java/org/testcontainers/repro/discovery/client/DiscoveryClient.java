package org.testcontainers.repro.discovery.client;

import org.testcontainers.repro.discovery.dto.DiscoveryResponse;

public interface DiscoveryClient {
  DiscoveryResponse getDiscoveryData(String var1);
}