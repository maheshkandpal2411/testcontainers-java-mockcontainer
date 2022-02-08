package org.testcontainers.repro.config;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "pulsar")
public class PulsarProperties {

    /**
     * Where to connect to Pulsar
     *
     * @see <a href="https://confluence.overstock.com/display/AL/Creating+Pulsar+Clients+with+auth">Creating Pulsar clients with auth</a>
     */
    private String serviceUrl;

    /**
     * The number of threads to use for consuming messages
     */
    private int listenerThreads = 10;

    /**
     * The name of the topic to use for consuming messages
     */
    private String topicName;

    /**
     * The name of the subscription to use for consuming messages
     */
    private String subscriptionName;

    @Override
    public String toString() {
        return "PulsarProperties{" +
                "serviceUrl='" + serviceUrl + '\'' +
                ", listenerThreads=" + listenerThreads +
                ", topicName=" + topicName +
                ", subscriptionName=" + subscriptionName +
                '}';
    }
}
