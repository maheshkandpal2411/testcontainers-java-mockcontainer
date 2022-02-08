package org.testcontainers.repro.discovery;

import org.testcontainers.repro.discovery.dto.DiscoveryResponse;

public interface DiscoveryService {
  String getUrl(String appName);

  DiscoveryResponse getData(String appName);
}