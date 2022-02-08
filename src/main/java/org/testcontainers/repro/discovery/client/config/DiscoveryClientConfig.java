package org.testcontainers.repro.discovery.client.config;

import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiscoveryClientConfig {
  @Value("${discovery-service.connection-timeout-milliseconds}")
  private Integer connectionTimeoutMilliseconds;
  @Value("${discovery-service.read-timeout-milliseconds}")
  private Integer readTimeoutMilliseconds;

  public DiscoveryClientConfig() {
  }

  @Bean
  public OkHttpClient discoveryClient() {
    LinkedList<Interceptor> interceptors = new LinkedList();
    return this.client(interceptors);
  }

  private OkHttpClient client(LinkedList<Interceptor> interceptors) {
    Builder builder = (new Builder())
        .connectTimeout((long)this.connectionTimeoutMilliseconds, TimeUnit.MILLISECONDS)
        .readTimeout((long)this.readTimeoutMilliseconds, TimeUnit.MILLISECONDS);
    Objects.requireNonNull(builder);
    interceptors.forEach(builder::addInterceptor);
    return builder.build();
  }
}
