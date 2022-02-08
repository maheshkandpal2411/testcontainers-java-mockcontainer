package org.testcontainers.repro.pulsar;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageListener;
import org.springframework.stereotype.Component;
import org.testcontainers.repro.service.TxnConsumerService;

@Component
@RequiredArgsConstructor
@Log4j2
public class TxnConsumerMessageListener implements MessageListener<String> {
    private final TxnConsumerService service;

    @Override
    public void received(Consumer<String> consumer, Message<String> msg) {
        try {
            log.debug("Received message {}", msg.getValue());
            service.process(msg);
            consumer.acknowledge(msg);
            log.trace("Acknowledged event to pulsar");
        }
        catch (Exception e) {
            log.error("Error consuming message {} with redelivery count {}",
                    msg.getValue(), msg.getRedeliveryCount(), e);
            //TODO Implement DLQ instead of negative ACK
            consumer.negativeAcknowledge(msg);
        }
    }
}
