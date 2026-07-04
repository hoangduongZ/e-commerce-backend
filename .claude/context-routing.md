# Context Routing — đọc gì trước khi làm task

Phân loại task theo bảng dưới (một task có thể khớp nhiều nhóm — đọc hợp của các nhóm, vẫn giới hạn tối đa ~5 file). **Luôn đọc `rules/coding-rules.md` trước.**

> Trạng thái knowledge base: dự án đang ở giai đoạn base (EPIC-00 xong). Knowledge-os content (`02`–`17`) **chưa sinh** — chạy `/knowledge-os` khi cần. Cho tới lúc đó, nguồn tri thức chính là `rules/coding-rules.md`, `README.md`, và tài liệu thiết kế ở repo `e-commerce-docs` (`docs/backend-plan/`, `docs/main/`).

| Nhóm task | Tín hiệu nhận biết | Đọc (theo thứ tự) |
|---|---|---|
| Backend / API | endpoint, service, controller, business logic | `rules/coding-rules.md`; `README.md` (§API response, §thêm endpoint); ADR `*api*`; `02-architecture/`, `03-backend/` *(chưa có)* |
| Database | schema, migration, query, index | `rules/coding-rules.md` (mục migration); `README.md` (§migration Flyway); ADR `*db*`, `*schema*`; `05-database/` *(chưa có)* |
| Auth (authn/authz) | login, token, session, permission, role | `rules/coding-rules.md`; ADR `*auth*`; history `*auth*` gần nhất; `07-security/` *(chưa có)* |
| Payment | thanh toán, order, refund, webhook | `rules/coding-rules.md` (mục "hỏi trước khi đụng tiền"); ADR `*payment*`; `03-backend/payment*` *(chưa có)* |
| Cache | cache, TTL, invalidation, Redis | `rules/coding-rules.md`; lesson `area: cache`; `06-infrastructure/cache*` *(chưa có)* |
| Search | search, index, full-text | `README.md` (module search); `06-infrastructure/search*` *(chưa có)* |
| Testing | test, coverage, mock, Testcontainers | `rules/coding-rules.md` (mục test); `README.md` (§Test); `08-testing/` *(chưa có)* |
| Bug fixing | fix, lỗi, không chạy | `23-debugging/` cùng khu vực lỗi; history cùng module |
| Refactoring | refactor, cleanup, tách module | `rules/coding-rules.md`; ADR liên quan module; `16-refactoring/` *(chưa có)* |
| Performance | chậm, N+1, tối ưu | `10-performance/` *(chưa có)*; `23-debugging/` liên quan perf |
| Security | lỗ hổng, CVE, hardening | `rules/coding-rules.md`; ADR `*security*`; `07-security/` *(chưa có)* |
| Deployment / CI | deploy, pipeline, release | `README.md` (§CI); lesson `area: deploy`; `12-devops/` *(chưa có)* |
| AI integration | LLM, prompt, embedding, agent | ADR `*ai*`; `14-ai-engineering/` *(chưa có)* |

Task đọc thêm (khi có): lesson cùng `area` trong `22-lessons-learned/`, tối đa 2–3 task history gần nhất cùng nhóm trong `21-task-history/`. Không đọc toàn bộ knowledge, không đọc history khác nhóm, không đọc lại file đã có trong context.

Không khớp nhóm nào → đọc `docs/knowledge/00-index/README.md` để tự tìm, tối đa 3 file. Vẫn không có → làm với context của codebase và ghi nhận khoảng trống knowledge trong wrap-up.
