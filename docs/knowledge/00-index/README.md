# Knowledge Base — Index

Bảng routing toàn bộ tri thức của ElectroStore backend. Agent đọc file này để tự tìm khi task không khớp nhóm nào trong `.claude/context-routing.md`. **Cập nhật ngay mỗi khi thêm/sửa file knowledge** — index sai còn tệ hơn không có index.

## Trạng thái hiện tại

Dự án vừa xong base (EPIC-00). Learning loop đã cài; knowledge-os content **chưa sinh**. Tri thức đang nằm ở:

- `.claude/rules/coding-rules.md` — convention thật của project (P1, đọc trước mọi task).
- `README.md` (root) — cách chạy, cấu trúc module, quy ước API/migration/test.
- Repo `e-commerce-docs`: `docs/backend-plan/` (kiến trúc, DB, sprint), `docs/main/api-conventions.md`, `docs/main/system-design.md`.

## File knowledge

| Đường dẫn | Mục đích | Khi nào đọc | Tags | Ưu tiên |
|---|---|---|---|---|
| *(chưa có — chạy `/knowledge-os` để sinh case Tier 1)* | | | | |

## Tri thức tích lũy (learning loop)

| Đường dẫn | Loại | Mục đích | Tags |
|---|---|---|---|
| `19-decision-records/adr-001-spring-boot-3-5-thay-vi-4-x.md` | ADR (proposed) | Vì sao dùng Spring Boot 3.5 thay 4.x + đổi package root; cảnh báo EOL 06/2026 | tech-stack, build |
| `21-task-history/2026-07-04-khoi-tao-base-backend-epic-00.md` | task-history | Dựng base EPIC-00 (modular monolith, common layer, CI, test) | backend, devops, foundation |
| `21-task-history/2026-07-04-fix-ci-trivy-and-cves.md` | task-history | Vá lỗi CI pipeline (Trivy, checkstyle, missing class) và upgrade Spring Boot chặn CVEs | devops, security |
| `23-debugging/testcontainers-orbstack-docker-api-version.md` | debugging | Testcontainers fail trên OrbStack + port 5432 bị chiếm — root cause & nhận diện | testing, devops |
| `22-lessons-learned/` | — | *(chưa có — sẽ ghi khi pattern gặp ≥ 2 lần)* | |

> Cấu trúc thư mục knowledge đầy đủ (`01`–`20`): xem `.claude/skills/knowledge-os/kb-structure.md`.
