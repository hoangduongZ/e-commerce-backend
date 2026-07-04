---
id: testcontainers-orbstack-docker-api-version
type: debugging
area: [testing, devops]
refs: [src/test/java/com/electrostore/common/support/TestcontainersConfig.java, docker-compose.yml, README.md]
---
# Testcontainers fail trên OrbStack + port 5432 bị chiếm

**Triệu chứng:** `./mvnw verify` fail toàn bộ `*IT`: `Could not find a valid Docker environment ... client version 1.32 is too old. Minimum supported API version is 1.40`. Riêng `docker compose up` thì postgres báo `Bind for 0.0.0.0:5432 failed: port is already allocated`.

**Root cause:** (1) Máy dùng OrbStack (không phải Docker Desktop). docker-java mà Testcontainers dùng thương lượng API version thấp (1.32), OrbStack từ chối → Testcontainers không tìm được Docker dù `docker` CLI chạy tốt. (2) Ba container postgres của dự án khác đã giữ 5432/5433/5434.

**Cách nhận ra sớm:** Docker CLI chạy OK nhưng Testcontainers báo "no valid Docker environment" / "client version too old" → nghĩ ngay tới engine không phải Docker Desktop. Lỗi "port is already allocated" → `lsof -nP -iTCP:5432 -sTCP:LISTEN` để tìm thủ phạm.

**Fix:** (1) Thêm `api.version=1.44` vào `~/.docker-java.properties`. (2) `.env` (gitignored) đặt `POSTGRES_HOST_PORT=5435`, chạy app với `DB_URL=jdbc:postgresql://localhost:5435/electrostore`. Cả hai đã ghi trong `.claude/rules/coding-rules.md` (Definition of Done) + `README.md` — file này chỉ giữ phần root-cause/nhận diện để chẩn đoán nhanh, không lặp lại fix.
