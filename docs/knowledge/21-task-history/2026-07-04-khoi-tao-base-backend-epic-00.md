---
id: 2026-07-04-khoi-tao-base-backend-epic-00
type: task-history
area: [backend, devops, foundation]
refs: [pom.xml, src/main/java/com/electrostore/, docker-compose.yml, .github/workflows/ci.yml, src/main/resources/application.yml]
related: [adr-001-spring-boot-3-5-thay-vi-4-x, testcontainers-orbstack-docker-api-version]
---
# Khởi tạo base backend EPIC-00 (Foundation & DevOps)

**Đã làm:** Dựng nền modular monolith Spring Boot 3.5 / Java 21 / Maven từ skeleton `spring init`: 11 module domain (package-info + boundary rule), common layer (ApiResponse envelope, ErrorCode, GlobalExceptionHandler, BaseEntity + JPA Auditing, CorrelationIdFilter, SlugUtil, 4 config), 5 profile, Flyway baseline, docker-compose PG16+Redis7, Testcontainers, Spotless/Checkstyle/JaCoCo, CI GitHub Actions. `./mvnw verify` xanh (7 unit + 8 IT), app chạy local health UP. Cover trọn ECM-001→011.

**Quyết định & lý do:** (1) Hạ Spring Boot 4.1.0→3.5.3 + đổi package root `com.electronic_store`→`com.electrostore` — chi tiết ở ADR `adr-001-spring-boot-3-5-thay-vi-4-x`. (2) Tách unit `*Test` (surefire, no Docker) vs integration `*IT` (failsafe, Testcontainers) để `mvn test` không cần Docker. (3) `ddl-auto: validate` — schema chỉ qua Flyway.

**Bất ngờ / giả định sai:** `mvn verify` fail ở IT vì Testcontainers không nói chuyện được với OrbStack + port 5432 bị dự án khác chiếm — root cause ở debugging note `testcontainers-orbstack-docker-api-version`. Giả định "Docker chạy là Testcontainers chạy" sai trên máy dùng OrbStack.

**Lần sau nên:** Module mới → `package-info.java` + boundary rule trước, sub-package `api/app/domain/infra` khi có class đầu. Entity extends `BaseEntity`, tiền `BigDecimal`. Endpoint → DTO `@Valid`, trả `ApiResponse.ok`, logic ở `app/`. Đọc `.claude/rules/coding-rules.md` trước mọi task backend.
