package org.testcontainers.repro;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.log4j.Log4j2;

import org.apache.commons.text.StringSubstitutor;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PulsarContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;


@Log4j2
@Testcontainers
public abstract class PulsarIT {

    private static final int PULSAR_ADMIN_PORT = 8080;

    private static final int PULSAR_DATA_PORT = 6650;

    private static final int VAULT_PORT = 8200;

    private static final int DISCOVERY_PORT = 1080;

    private static final int MOCKSERVER_PORT = 1090;

    private static final String PULSAR_DOCKER_IMAGENAME = "apachepulsar/pulsar:2.5.1";

    private static final Network NETWORK;

    private static final String HTTP_PREFIX = "http://";

    private static final String COLON = ":";

    private static final String DISCOVERY_JSON_FILE_PATH = "src/test/resources/test-containers/discovery/discovery-service.json";

    private static final String UPDATED_DISCOVERY_JSON_FILE_PATH = "src/test/resources/test-containers/discovery/updated-discovery-service.json";

    private static final String TXN_WS_JSON_FILE_PATH = "src/test/resources/test-containers/service/txn-ws-service.json";

    @Container
    private static final PulsarContainer PULSAR_CONTAINER;

    @Container
    protected static final GenericContainer<?> MOCKSERVER_DISCOVERY_CONTAINER;

    @Container
    protected static final GenericContainer<?> MOCKSERVER_TXN_WS_CONTAINER;

    public static final String PREFIX = "\"";

    public static final String SUFFIX = "\"";

    static {
        NETWORK = Network.newNetwork();

        PULSAR_CONTAINER = new PulsarContainer(DockerImageName.parse(PULSAR_DOCKER_IMAGENAME)
            .asCompatibleSubstituteFor("pulsar"))
            .withExposedPorts(PULSAR_ADMIN_PORT, PULSAR_DATA_PORT)
            .withNetwork(NETWORK)
            .withNetworkAliases("pulsar")
            .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("Pulsar Container")))
            .waitingFor(Wait.forLogMessage(".*messaging service is ready*\\n", 1));
        PULSAR_CONTAINER.start();

        /* Configure the app to connect to our Pulsar container */
        System.setProperty("pulsar.service-url", PULSAR_CONTAINER.getPulsarBrokerUrl());

        MOCKSERVER_TXN_WS_CONTAINER = new GenericContainer<>(
            DockerImageName.parse("mockserver/mockserver:mockserver-5.11.2"))
            .withExposedPorts(MOCKSERVER_PORT)
            .withAccessToHost(true)
            .withNetwork(NETWORK)
            .withEnv("MOCKSERVER_INITIALIZATION_JSON_PATH", "/config/txn-ws-service.json")

            .withCopyFileToContainer(MountableFile.forHostPath(TXN_WS_JSON_FILE_PATH), "/config/txn-ws-service.json")
            .withNetworkAliases("mockserver")
            .waitingFor(
                Wait.forLogMessage("^(.*?(\\bstarted on port\\b)[^$]*)$", 1)
            )
            .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("MockServer Txn WS Container")));
        MOCKSERVER_TXN_WS_CONTAINER.start();

        final String serviceUrl = new StringBuilder(HTTP_PREFIX)
            .append(MOCKSERVER_TXN_WS_CONTAINER.getHost())
            .append(COLON)
            .append(MOCKSERVER_TXN_WS_CONTAINER.getMappedPort(MOCKSERVER_PORT).toString())
            .toString();

        try (FileWriter writer = new FileWriter(UPDATED_DISCOVERY_JSON_FILE_PATH)){
            String discoveryJsonContent = new String(Files.readAllBytes(Paths.get(DISCOVERY_JSON_FILE_PATH)));

            Map<String, String> substitutes = new HashMap<>();
            substitutes.put("$ENDPOINT_URL", new StringBuilder().append(PREFIX).append(serviceUrl).append(SUFFIX).toString());

            //include double quote prefix and suffix as its json wrapped
            StringSubstitutor stringSubstitutor = new StringSubstitutor(substitutes, PREFIX, SUFFIX);

            String updatedContent = stringSubstitutor.replace(discoveryJsonContent);

            writer.write(updatedContent);
        } catch (IOException e) {
            log.error("Error occurred while updating endpoint URL", e);
        }

        MOCKSERVER_DISCOVERY_CONTAINER = new GenericContainer<>(
            DockerImageName.parse("mockserver/mockserver:mockserver-5.11.2"))
            .withExposedPorts(DISCOVERY_PORT)
            .withNetwork(NETWORK)
            .withEnv("MOCKSERVER_INITIALIZATION_JSON_PATH", "/config/discovery-service.json")
            .withCopyFileToContainer(MountableFile.forHostPath(UPDATED_DISCOVERY_JSON_FILE_PATH), "/config/discovery-service.json")
            .withNetworkAliases("discovery")
            .waitingFor(
                Wait.forLogMessage("^(.*?(\\bstarted on port\\b)[^$]*)$", 1)
            )
            .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("MockServer Discovery Container")));
        MOCKSERVER_DISCOVERY_CONTAINER.start();

        final String discoveryServiceAddr = new StringBuilder(HTTP_PREFIX)
            .append(MOCKSERVER_DISCOVERY_CONTAINER.getHost())
            .append(COLON)
            .append(MOCKSERVER_DISCOVERY_CONTAINER.getMappedPort(DISCOVERY_PORT).toString())
            .toString();

        System.setProperty("DISCOVERY_SERVICE_ADDR", discoveryServiceAddr);
        log.info("DISCOVERY_SERVICE_ADDR set to : {}", discoveryServiceAddr);

        File file = new File(UPDATED_DISCOVERY_JSON_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
    }
}
