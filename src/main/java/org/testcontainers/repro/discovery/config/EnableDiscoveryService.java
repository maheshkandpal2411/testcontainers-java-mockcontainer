package org.testcontainers.repro.discovery.config;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.context.annotation.Import;

@Import({DiscoveryServiceConfig.class})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableDiscoveryService {
}