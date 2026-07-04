package com.electrostore.common.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.electrostore.common.support.AbstractIntegrationTest;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/** Verify envelope thống nhất + mapping exception → HTTP status qua controller test-support. */
class GlobalExceptionHandlerIT extends AbstractIntegrationTest {

    @Test
    void successResponseUsesEnvelope() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/test-support/ok", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        JsonNode body = objectMapper.readTree(response.getBody());
        assertThat(body.get("success").asBoolean()).isTrue();
        assertThat(body.get("data").get("ping").asText()).isEqualTo("pong");
        assertThat(body.get("error").isNull()).isTrue();
    }

    @Test
    void notFoundExceptionMapsTo404WithErrorEnvelope() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/test-support/not-found", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        JsonNode body = objectMapper.readTree(response.getBody());
        assertThat(body.get("success").asBoolean()).isFalse();
        assertThat(body.get("data").isNull()).isTrue();
        assertThat(body.get("error").get("code").asText()).isEqualTo("RESOURCE_NOT_FOUND");
        assertThat(body.get("error").get("message").asText()).contains("42");
    }

    @Test
    void invalidBodyMapsTo422WithFieldDetails() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"name\":\"\",\"quantity\":0}", headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity("/api/v1/test-support/validate", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        JsonNode body = objectMapper.readTree(response.getBody());
        assertThat(body.get("error").get("code").asText()).isEqualTo("VALIDATION_ERROR");
        assertThat(body.get("error").get("details").size()).isEqualTo(2);
    }

    @Test
    void unknownUrlMapsTo404Envelope() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/khong-ton-tai", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        JsonNode body = objectMapper.readTree(response.getBody());
        assertThat(body.get("success").asBoolean()).isFalse();
        assertThat(body.get("error").get("code").asText()).isEqualTo("NOT_FOUND");
    }
}
