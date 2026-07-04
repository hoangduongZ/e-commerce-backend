package com.electrostore;

import static org.assertj.core.api.Assertions.assertThat;

import com.electrostore.common.logging.CorrelationIdHolder;
import com.electrostore.common.support.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Smoke test nền tảng: context load (kèm Flyway migrate trên PostgreSQL thật), Actuator
 * health/liveness/readiness, correlation id filter.
 */
class ApplicationIT extends AbstractIntegrationTest {

    @Test
    void healthEndpointReturnsUp() {
        ResponseEntity<String> response = restTemplate.getForEntity("/actuator/health", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("\"status\":\"UP\"");
    }

    @Test
    void livenessAndReadinessProbesAvailable() {
        assertThat(restTemplate
                        .getForEntity("/actuator/health/liveness", String.class)
                        .getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(restTemplate
                        .getForEntity("/actuator/health/readiness", String.class)
                        .getStatusCode())
                .isEqualTo(HttpStatus.OK);
    }

    @Test
    void responseHasGeneratedCorrelationIdWhenClientSendsNone() {
        ResponseEntity<String> response = restTemplate.getForEntity("/actuator/health", String.class);

        assertThat(response.getHeaders().getFirst(CorrelationIdHolder.HEADER)).isNotBlank();
    }

    @Test
    void responseEchoesCorrelationIdFromClient() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(CorrelationIdHolder.HEADER, "it-test-123");

        ResponseEntity<String> response =
                restTemplate.exchange("/actuator/health", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        assertThat(response.getHeaders().getFirst(CorrelationIdHolder.HEADER)).isEqualTo("it-test-123");
    }
}
