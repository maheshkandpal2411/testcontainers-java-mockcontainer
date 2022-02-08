package org.testcontainers.repro;

import lombok.extern.log4j.Log4j2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.testcontainers.repro.discovery.config.EnableDiscoveryService;

@Log4j2
@SpringBootApplication
@EnableDiscoveryService
public class TxnConsumerApplication {

  public static void main(String[] args) {
    log.info("Starting Application...");
    final SpringApplication application = new SpringApplication(TxnConsumerApplication.class);
    addProfileFromEnv(application);
    application.run(args);
  }

  private static void addProfileFromEnv(SpringApplication application) {
    final String envVar = "MY_ENVIRONMENT";
    final String value = System.getenv().getOrDefault(envVar, "local");
    log.info("{} is set to '{}'", envVar, value);
    if (value.endsWith(".test")) {
      application.setAdditionalProfiles("test-env");
    }
    else {
      application.setAdditionalProfiles(value); //most likely 'prod' or the default value above
    }
  }


}
