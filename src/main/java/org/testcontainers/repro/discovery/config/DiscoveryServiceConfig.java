package org.testcontainers.repro.discovery.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.repro.discovery.DiscoveryServiceMarker;

@Configuration
@ComponentScan(
  basePackageClasses = { DiscoveryServiceMarker.class}
)
public class DiscoveryServiceConfig {
  public DiscoveryServiceConfig() {
  }
}