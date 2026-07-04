# Coding Rules — convention THẬT của ElectroStore backend

> Rút từ codebase base (EPIC-00). Đây là những điều người mới/agent sẽ **làm sai nếu không được bảo** — không phải best practice chung chung. Agent không tự sửa file này; đề xuất thay đổi phải được người dùng duyệt.

## Convention project

1. **Ranh giới module là bất khả xâm phạm.** Module A không được inject repository/entity của module B; cần dữ liệu B thì gọi qua application service công khai trong `B/app/`. Xem `src/main/java/com/electrostore/*/package-info.java`.

2. **Controller không tự build response.** Luôn trả `ApiResponse.ok(data)` hoặc `ApiResponse.ofPage(PageResponse.from(page))`. Lỗi thì **throw** `BusinessException`/`NotFoundException` — `GlobalExceptionHandler` map ra envelope + HTTP status. Không try/catch build JSON lỗi trong controller. Xem `common/response/ApiResponse.java`, `common/exception/GlobalExceptionHandler.java`.

3. **Error code sống trong enum `ErrorCode`.** Thêm mã mới ở đó kèm HTTP status; enum `name()` chính là `error.code` trả client — **ổn định, không đổi tên** sau khi public. Xem `common/exception/ErrorCode.java`.

4. **Entity kế thừa `BaseEntity`.** Có sẵn id BIGINT identity + audit tự động. Thời gian dùng `Instant` (UTC); tiền dùng `BigDecimal`, **tuyệt đối không** float/double. Xem `common/audit/BaseEntity.java`.

5. **Schema chỉ thay đổi qua Flyway.** `ddl-auto: validate` — Hibernate không tự tạo/sửa bảng. Migration đã merge **không được sửa**, tạo file mới `VyyyyMMddNNNN__mo_ta.sql`. PR đổi schema phải kèm migration + entity + test. Xem `src/main/resources/db/migration/`.

6. **Không hard-code secret.** Giá trị chung ở `application.yml`, override theo profile ở `application-<env>.yml`, secret luôn dạng `${ENV_VAR}`. Default local trùng `docker-compose.yml` (không phải secret thật). Xem `src/main/resources/application.yml`.

7. **Validation bằng Jakarta Bean Validation.** Request DTO gắn `@NotBlank`/`@Min`... + `@Valid` ở controller; lỗi tự ra `422 VALIDATION_ERROR` kèm details theo field. Không viết validate thủ công trong controller.

8. **Test tách hai loại.** Unit `*Test` (surefire, không cần Docker); integration `*IT` (failsafe, cần Docker) extends `AbstractIntegrationTest` — Testcontainers PG16+Redis7 tự nối qua `@ServiceConnection`. Controller chỉ phục vụ test đặt trong test sources, không copy sang main. Xem `src/test/java/com/electrostore/common/support/`.

9. **Format trước khi commit.** `./mvnw spotless:apply` (palantir-java-format); dòng ≤ 120 ký tự (Checkstyle). CI fail nếu chưa format.

10. **Constructor injection, không field injection.** Dùng `@RequiredArgsConstructor` (Lombok) + `private final` cho dependency; **không** `@Autowired` trên field. Dễ test, phát hiện circular dependency sớm.

11. **`@Transactional` chỉ đặt ở app layer** (application service), `readOnly = true` cho use-case chỉ đọc. Không đặt ở controller hay domain entity.

12. **Không lộ JPA entity ra API.** Controller nhận/trả DTO (ưu tiên `record`), map entity↔DTO bằng MapStruct — không trả thẳng entity. Đã bật `open-in-view: false` (xem `src/main/resources/application.yml`) nên trả entity có quan hệ lazy ra ngoài sẽ ném `LazyInitializationException`.

13. **Repository trả `Optional`, không trả `null`.** App layer `.orElseThrow(() -> new NotFoundException(...))`; không gọi `.get()` trần trên Optional. Xem `common/exception/NotFoundException.java`.

## Definition of Done — chưa đạt thì KHÔNG báo hoàn thành, KHÔNG `/task-wrapup`

- `docker compose up -d` chạy được, PostgreSQL + Redis healthy.
- `./mvnw verify` **BUILD SUCCESS** — gồm spotless:check + checkstyle + unit (surefire) + integration (failsafe/Testcontainers). Cần Docker đang chạy.
- Với thay đổi có runtime: chạy thật (`./mvnw spring-boot:run`) và quan sát kết quả (vd `/actuator/health` UP, endpoint trả đúng envelope).
- Gotcha môi trường (ghi để khỏi mất giờ debug): nếu port 5432 bị chiếm → tạo `.env` với `POSTGRES_HOST_PORT=...` và chạy app với `DB_URL` tương ứng; nếu Testcontainers báo `client version 1.32 is too old` (OrbStack) → thêm `api.version=1.44` vào `~/.docker-java.properties`.

## Luôn HỎI trước khi

- Sửa/xóa migration đã merge (thay vào đó tạo migration mới).
- Đụng luồng tiền: payment, refund, thay đổi trạng thái order (khi các module này có thật).
- Hành động không đảo ngược: drop bảng, xóa dữ liệu, `git push --force`, xóa volume Docker có data.
- Gọi service ngoài có side effect thật: payment gateway, gửi email/SMS/webhook thật.
- Thêm dependency nặng (Elasticsearch, RabbitMQ, cloud SDK) trước khi tới task tương ứng — base cố tình chưa có chúng.
