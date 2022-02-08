package org.testcontainers.repro.discovery.impl;

import org.springframework.stereotype.Service;
import org.testcontainers.repro.discovery.DiscoveryService;
import org.testcontainers.repro.discovery.client.DiscoveryClient;
import org.testcontainers.repro.discovery.dto.DiscoveryResponse;
import org.testcontainers.repro.discovery.exception.DiscoveryClientException;

@Service
public class DiscoveryServiceImpl implements DiscoveryService {
  private final DiscoveryClient discoveryClient;

  public String getUrl(String applicationName) throws DiscoveryClientException {
    return this.getData(applicationName).getUrl();
  }

  public DiscoveryResponse getData(String applicationName) throws DiscoveryClientException {
    return this.discoveryClient.getDiscoveryData(applicationName);
  }

  public DiscoveryServiceImpl(DiscoveryClient discoveryClient) {
    this.discoveryClient = discoveryClient;
  }
}
