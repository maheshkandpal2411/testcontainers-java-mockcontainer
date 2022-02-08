package org.testcontainers.repro.service;

import static org.testcontainers.repro.util.Constants.JSON;
import static org.testcontainers.repro.util.Constants.SLASH;
import static org.testcontainers.repro.util.Constants.TRANSACTION_WS_APP_NAME;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import org.apache.pulsar.client.api.Message;
import org.springframework.stereotype.Service;
import org.testcontainers.repro.discovery.DiscoveryService;
import org.testcontainers.repro.discovery.dto.DiscoveryResponse;
import org.testcontainers.repro.dto.Adjustment;
import org.testcontainers.repro.dto.MessageEnvelope;
import org.testcontainers.repro.dto.Transaction;

import com.fasterxml.jackson.databind.ObjectMapper;


//import com.overstock.utilities.discovery.DiscoveryService;
//import com.overstock.utilities.discovery.dto.DiscoveryResponse;

@Log4j2
@Service
@RequiredArgsConstructor
public class TxnConsumerService {
  private final ObjectMapper mapper;

  private final DiscoveryService discoveryService;

  public void process(final Message<String> envelope) throws IOException {
    final String message = envelope.getValue();
    final MessageEnvelope<String> messageEnvelope = new MessageEnvelope<>(message);

    log.info("Created envelope and message: " + message);
    Transaction event = mapper.readValue(message, Transaction.class);

    processTransactionPayload(event);
  }

  public void processTransactionPayload(Transaction transaction) throws IOException {
    if (transaction.getType() == null) {
      log.error("Processing aborted as null or unsupported transaction type");
      throw new IOException("Transaction type is null");
    }
    DiscoveryResponse discoveryResponse = discoveryService.getData(TRANSACTION_WS_APP_NAME);
    String apiUrlPrefix = new StringBuilder().append(discoveryResponse.getUrl()).append(SLASH).append(discoveryResponse.getAppName()).toString();

    switch(transaction.getType()) {
      case INVENTORY_ADJUSTMENT:
        log.info("JSON: {}", transaction.getJson());
        Adjustment adjustment = mapper.readValue(transaction.getJson(), Adjustment.class);

        log.info("Adjustment DTO: {}", adjustment);
        String adjContent = mapper.writeValueAsString(adjustment);

        log.info("DTO JSON: {}", adjContent);
        put(apiUrlPrefix, "adjustment", adjContent);

        break;
    }
  }

  private ResponseBody put(String urlPrefix, String operation, String json) throws IOException {
    final OkHttpClient client = new OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .retryOnConnectionFailure(false)
        .build();

    final String completeUrl = new StringBuilder().append(urlPrefix).append(SLASH).append(operation).toString();
    log.info("Complete URL: {}", completeUrl);

    RequestBody body = RequestBody.create(json, JSON);
    Request request = new Request.Builder()
        .url(completeUrl)
        .put(body)
        .build();

    try (Response response = client.newCall(request).execute()) {
      return response.body();
    }
  }
}
