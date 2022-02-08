package org.testcontainers.repro.rest;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.testcontainers.repro.dto.Transaction;
import org.testcontainers.repro.service.TxnConsumerService;
import org.testcontainers.repro.util.Constants;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = Constants.TXN_EVENTS_URI)
public class TxnConsumerController {

  private final TxnConsumerService service;

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void create(@RequestBody final Transaction event) {

    try {
      service.processTransactionPayload(event);
    } catch (final IOException exception) {
      log.error("Failed to process event JSON", exception);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not create a valid json message from entity provided.");
    }
  }
}
