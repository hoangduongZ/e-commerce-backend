# ElectroStore Backend

Backend cho website ecommerce đồ điện tử — **Modular Monolith** trên Spring Boot 3.5 / Java 21 / Maven.

Tài liệu thiết kế gốc nằm ở repo `e-commerce-docs` (`docs/backend-plan/*`, `docs/main/api-conventions.md`).

## Stack

| Thành phần | Công nghệ |
|---|---|
| Ngôn ngữ / Framework | Java 21, Spring Boot 3.5 (Maven) |
| Database / Cache | PostgreSQL 16, Redis 7 (docker-compose local) |
| ORM / Migration | Spring Data JPA + Hibernate, Flyway |
| API Docs | springdoc-openapi (Swagger UI) |
| Mapping / Boilerplate | MapStruct, Lombok |
| Test | JUnit 5, Mockito, Testcontainers |
| Observability | Actuator, Micrometer/Prometheus, structured logging + correlation id |
| Quality | Spotless (palantir-java-format), Checkstyle, JaCoCo |
| CI | GitHub Actions (`.github/workflows/ci.yml`) |

## Chạy local

Yêu cầu: JDK 21, Docker (Desktop) đang chạy. Không cần cài Maven — dùng wrapper `./mvnw`.

```bash
# 1. Bật PostgreSQL + Redis
docker compose up -d

# 2. Chạy app (profile mặc định: local)
./mvnw spring-boot:run
```

| URL | Mô tả |
|---|---|
| http://localhost:8080/swagger-ui.html | Swagger UI |
| http://localhost:8080/actuator/health | Health (kèm `/liveness`, `/readiness`) |
| http://localhost:8080/actuator/prometheus | Metrics Prometheus (local/dev/staging) |

Dừng hạ tầng: `docker compose down` (thêm `-v` nếu muốn xoá data volume).

**Máy đã có service chiếm port 5432/6379?** Tạo file `.env` (đã gitignore) cạnh `docker-compose.yml`:

```bash
POSTGRES_HOST_PORT=5435   # host port cho PostgreSQL
# REDIS_HOST_PORT=6380    # nếu cần
```

rồi chạy app với `DB_URL=jdbc:postgresql://localhost:5435/electrostore ./mvnw spring-boot:run`.

**Dùng OrbStack / Docker engine mới mà Testcontainers báo `client version 1.32 is too old`?** Thêm một dòng vào `~/.docker-java.properties`:

```
api.version=1.44
```

## Cấu trúc project

```
src/main/java/com/electrostore/
├── EcommerceApplication.java
├── common/                  # cross-cutting, KHÔNG chứa business logic
│   ├── config/              # OpenApiConfig, JpaAuditingConfig, WebConfig (CORS), RedisConfig
│   ├── exception/           # ErrorCode, BusinessException, NotFoundException, GlobalExceptionHandler
│   ├── response/            # ApiResponse<T>, PageResponse<T>, ErrorResponse
│   ├── audit/               # BaseEntity, AuditorAwareImpl
│   ├── logging/             # CorrelationIdFilter, CorrelationIdHolder
│   └── util/                # SlugUtil...
├── iam/ catalog/ attribute/ inventory/ cart/ promotion/
├── order/ payment/ search/ admin/ notification/
```

Mỗi module domain (iam, catalog...) theo layout 4 tầng, tạo sub-package khi có class đầu tiên:

- `api/` — Controller, Request/Response DTO
- `app/` — Application Service, use-case orchestration, transaction boundary (`@Transactional`)
- `domain/` — Entity, Value Object, domain rule, domain service
- `infra/` — Repository (JPA), adapter ngoài

**Boundary rule (bắt buộc):** module A không được inject trực tiếp repository của module B. Cần dữ liệu/hành vi của B → gọi qua application service / interface công khai trong `B.app`. Không tách microservice/DB ở giai đoạn này.

## Quy ước API response

Base path: `/api/v1` (admin: `/api/v1/admin/...`). Mọi response bọc envelope (chi tiết: `docs/main/api-conventions.md` bên repo docs):

```json
{ "success": true,  "data": { },  "error": null, "meta": null }
{ "success": false, "data": null, "error": { "code": "RESOURCE_NOT_FOUND", "message": "...", "details": [] }, "meta": null }
```

- Controller trả `ApiResponse.ok(data)`; phân trang trả `ApiResponse.ofPage(PageResponse.from(page))` — items nằm trong `data`, thông tin trang trong `meta`.
- Lỗi nghiệp vụ: throw `BusinessException`/`NotFoundException` — `GlobalExceptionHandler` tự map ra envelope + HTTP status đúng (400/401/403/404/405/409/422/500). Không bao giờ trả stacktrace ra client.
- `error.code` là hằng `SCREAMING_SNAKE_CASE` ổn định (FE map thông điệp theo code) — không đổi code đã public.

## Quy ước migration (Flyway)

- File đặt tại `src/main/resources/db/migration/`, tên: `VyyyyMMddNNNN__mo_ta_ngan.sql` (NNNN = số thứ tự trong ngày, bắt đầu `0001`). Ví dụ: `V202607030001__baseline.sql`.
- **Migration đã merge không được sửa** — muốn đổi schema thì tạo migration mới.
- Mỗi PR thay đổi schema phải kèm: migration + entity tương ứng + test.
- `ddl-auto: validate` — Hibernate không bao giờ tự tạo/sửa schema; schema là trách nhiệm của Flyway.

## Cách thêm module mới

1. Tạo package `com.electrostore.<module>` + `package-info.java` mô tả module và boundary rule (xem các module hiện có).
2. Tạo sub-package `api/app/domain/infra` khi có class đầu tiên.
3. Entity extends `BaseEntity` (có sẵn id + createdAt/updatedAt/createdBy/updatedBy tự động).
4. Kèm migration tạo bảng + integration test.

## Cách thêm endpoint mới

1. Controller đặt trong `<module>/api/`, mapping bắt đầu bằng `/api/v1/...`.
2. Request DTO dùng Jakarta Validation (`@NotBlank`, `@Min`...) + `@Valid` ở controller — lỗi validation tự ra `422 VALIDATION_ERROR` kèm `details` theo field.
3. Trả `ApiResponse.ok(...)` / `ApiResponse.ofPage(...)`; lỗi thì throw exception, không tự build response lỗi.
4. Logic nghiệp vụ + `@Transactional` đặt ở `<module>/app/`, không đặt trong controller.
5. Thêm `GroupedOpenApi` bean trong `OpenApiConfig` nếu muốn nhóm docs theo module.

## Cách thêm error code mới

Thêm hằng vào `ErrorCode` (kèm HTTP status + message mặc định). Code nghiệp vụ chi tiết (vd `PRODUCT_NOT_FOUND`, `COUPON_INVALID`) thêm dần theo EPIC — giữ ổn định sau khi public.

## Test

```bash
./mvnw test        # unit test (không cần Docker)
./mvnw verify      # format check + checkstyle + unit + integration test (cần Docker)
```

- Unit test: `*Test.java` (surefire).
- Integration test: `*IT.java` (failsafe), extends `AbstractIntegrationTest` — tự bật PostgreSQL 16 + Redis 7 qua Testcontainers (`TestcontainersConfig`, dùng `@ServiceConnection` nên không phải set property tay). Các IT dùng chung context/container nhờ Spring context caching.
- `EnvelopeTestController` chỉ tồn tại trong test sources để verify envelope khi chưa có business endpoint.

## Format & lint

```bash
./mvnw spotless:apply   # tự format code (palantir-java-format)
./mvnw spotless:check   # CI sẽ fail nếu chưa format
```

Checkstyle (`config/checkstyle/checkstyle.xml`) chạy tự động trong `./mvnw verify`.

## Cấu hình & profile

| Profile | Dùng cho | Ghi chú |
|---|---|---|
| `local` (default) | Máy dev | Default credential trùng docker-compose; log text + SQL debug |
| `dev` | Môi trường dev | DB/Redis bắt buộc qua env (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `REDIS_HOST`...) |
| `staging` | UAT | Như dev, log INFO, Swagger vẫn bật |
| `prod` | Production | Swagger tắt, Actuator chỉ expose `health,prometheus`, log JSON |

Chạy profile khác: `SPRING_PROFILES_ACTIVE=dev ./mvnw spring-boot:run` (hoặc env tương ứng khi deploy).
**Không commit secret** — mọi credential ngoài local đều qua env variable. Thêm config mới: đặt giá trị chung ở `application.yml`, override theo môi trường ở `application-<profile>.yml`, secret luôn ở dạng `${ENV_VAR}`.

## Logging & correlation id

- Mọi request được gắn `X-Correlation-ID` (nhận từ client nếu hợp lệ, không thì sinh UUID), có trong MDC của mọi dòng log và trả lại ở response header.
- Profile `local` log text; profile khác log JSON (logstash encoder) — sẵn sàng cho ELK/Loki.
- **Không log** password, token, số thẻ, PII nhạy cảm.

## CI

`.github/workflows/ci.yml`: push/PR vào `main` → `./mvnw verify` (build + format + checkstyle + unit/integration test, có upload báo cáo JaCoCo) + Trivy scan dependency (fail khi có CVE CRITICAL/HIGH có bản fix).
