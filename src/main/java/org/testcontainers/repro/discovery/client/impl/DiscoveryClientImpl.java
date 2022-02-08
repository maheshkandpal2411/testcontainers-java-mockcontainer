package org.testcontainers.repro.discovery.client.impl;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Objects;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.Request.Builder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.testcontainers.repro.discovery.client.DiscoveryClient;
import org.testcontainers.repro.discovery.dto.DiscoveryResponse;
import org.testcontainers.repro.discovery.exception.DiscoveryClientException;

@Component
public class DiscoveryClientImpl implements DiscoveryClient {
  private static final Logger log = LogManager.getLogger(DiscoveryClientImpl.class);
  private static final String DISCOVERY_URL = "%s/discovery/%s";
  private final ObjectMapper objectMapper;
  private final OkHttpClient discoveryClient;
  @Value("${discovery-service.base-url}")
  private String baseUrl;

  public DiscoveryResponse getDiscoveryData(String applicationName) {
    String url = String.format("%s/discovery/%s", this.baseUrl, applicationName);
    Request request = (new Builder()).url(url).get().build();

    try {
      Response response = this.discoveryClient.newCall(request).execute();

      DiscoveryResponse var6;
      try {
        DiscoveryResponse discoveryResponse = this.parseResponse(response);
        log.info("applicationName={}, url={}", applicationName, discoveryResponse.getUrl());
        var6 = discoveryResponse;
      } catch (Throwable var8) {
        if (response != null) {
          try {
            response.close();
          } catch (Throwable var7) {
            var8.addSuppressed(var7);
          }
        }

        throw var8;
      }

      if (response != null) {
        response.close();
      }

      return var6;
    } catch (Exception var9) {
      log.error("discovery failed", var9);
      throw new DiscoveryClientException(var9.getMessage());
    }
  }

  DiscoveryResponse parseResponse(Response response) {
    try {
      ResponseBody responseBody = (ResponseBody)Objects.requireNonNull(response.body());

      DiscoveryResponse var8;
      try {
        var8 = (DiscoveryResponse)this.objectMapper.readValue(responseBody.string(), DiscoveryResponse.class);
      } catch (Throwable var6) {
        if (responseBody != null) {
          try {
            responseBody.close();
          } catch (Throwable var5) {
            var6.addSuppressed(var5);
          }
        }

        throw var6;
      }

      if (responseBody != null) {
        responseBody.close();
      }

      return var8;
    } catch (IOException var7) {
      String message = "failed to parse response";
      log.error("failed to parse response", var7);
      throw new DiscoveryClientException("failed to parse response", var7);
    }
  }

  public DiscoveryClientImpl(ObjectMapper objectMapper, OkHttpClient discoveryClient) {
    this.objectMapper = objectMapper;
    this.discoveryClient = discoveryClient;
  }
}
