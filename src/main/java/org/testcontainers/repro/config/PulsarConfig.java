package org.testcontainers.repro.config;

import lombok.RequiredArgsConstructor;

import org.apache.pulsar.client.api.ClientBuilder;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.client.api.SubscriptionType;
import org.apache.pulsar.client.impl.auth.AuthenticationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.repro.pulsar.TxnConsumerMessageListener;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(PulsarProperties.class)
public class PulsarConfig {
  private final PulsarProperties properties;

  @Bean
  public PulsarClient pulsarClient(@Value("${pulsar.txnConsumerToken}") String pulsarToken) throws PulsarClientException {
    ClientBuilder builder = PulsarClient.builder()
        .authentication(new AuthenticationToken(pulsarToken))
        .serviceUrl(properties.getServiceUrl());
    if (properties.getServiceUrl().contains("ssl://")) {
      builder = builder.tlsTrustCertsFilePath("/app/pulsar_cert.pem")
          .enableTlsHostnameVerification(false)
          .allowTlsInsecureConnection(false);
    }
    return builder
        .listenerThreads(properties.getListenerThreads())
        .build();
  }

  @Bean
  public Consumer<String> consumer(PulsarClient client, TxnConsumerMessageListener listener) throws PulsarClientException {
    return client.newConsumer(Schema.STRING)
        .messageListener(listener)
        .topic(properties.getTopicName())
        .subscriptionType(SubscriptionType.Shared)
        .subscriptionName(properties.getSubscriptionName())
        .subscribe();
  }
}