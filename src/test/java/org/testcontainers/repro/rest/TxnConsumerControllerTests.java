package org.testcontainers.repro.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Paths;

import lombok.extern.log4j.Log4j2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.repro.PulsarIT;
import org.testcontainers.repro.dto.Transaction;
import org.testcontainers.repro.dto.TransactionType;
import org.testcontainers.repro.util.Constants;

import com.fasterxml.jackson.databind.ObjectMapper;

@Log4j2
@AutoConfigureMockMvc
@SpringBootTest
class TxnConsumerControllerTests extends PulsarIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper mapper;

  @Test
  void testCreateTxnConsumer_success() throws Exception {
    Transaction request = new Transaction().
    withJson(new String(Files.readAllBytes(Paths.get("src/test/resources/inv_adjustment.json")))).
    withType(TransactionType.INVENTORY_ADJUSTMENT);

    String content = mapper.writeValueAsString(request);

    log.info("JSON: {}", content);

    mockMvc.perform(post(Constants.TXN_EVENTS_URI)
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
        .andExpect(status().isAccepted())
        .andExpect(content().string(""));
  }

  @Test
  void testCreateGwWarehouseItemTxnConsumer_failure() throws Exception {
    Transaction request = new Transaction();
    request.setJson(new String(Files.readAllBytes(Paths.get("src/test/resources/inv_adjustment.json"))));
    request.setType(null);

    String content = mapper.writeValueAsString(request);

    log.info("JSON: {}", content);

    mockMvc.perform(post(Constants.TXN_EVENTS_URI)
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
        .andExpect(status().isBadRequest());
  }

}