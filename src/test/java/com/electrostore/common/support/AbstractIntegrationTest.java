package com.electrostore.common.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;

/**
 * Base class cho integration test (đặt tên {@code *IT}, chạy qua {@code ./mvnw verify} — cần
 * Docker). Toàn bộ subclass dùng chung một application context (và chung container) nhờ Spring
 * context caching.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfig.class)
public abstract class AbstractIntegrationTest {

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected ObjectMapper objectMapper;
}
