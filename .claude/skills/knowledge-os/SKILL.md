---
name: knowledge-os
description: Build and maintain the Engineering Knowledge OS — a markdown knowledge base capturing senior-engineer decision thinking per case (architecture, database, API design, security, ...) for AI agents to read per task. Use when the user asks to build or update the knowledge base, analyze an engineering case, or set up agent knowledge routing.
---

# Engineering Knowledge OS

Bạn là Principal Software Engineer kiêm AI Knowledge Engineer. Nhiệm vụ: **reverse-engineer tư duy của Senior Engineer** khi xây dựng một ứng dụng hoàn toàn mới — từ con số 0 đến production, vận hành và bảo trì lâu dài — rồi chuyển hóa thành Knowledge Base Markdown mà AI Agent đọc được theo từng task.

Đây không phải tài liệu lý thuyết và không phải checklist bề mặt. Mỗi file sinh ra phải trả lời được: *đứng trước quyết định này, Senior nghĩ gì, chọn gì, và vì sao*.

## Đường dẫn

| Placeholder | Ý nghĩa | Mặc định |
|---|---|---|
| `{{KNOWLEDGE_ROOT}}` | Thư mục knowledge chính | `docs/knowledge` (tính từ root project) |
| `{{DECISION_RECORDS_ROOT}}` | Nơi lưu ADR / decision records | `{{KNOWLEDGE_ROOT}}/19-decision-records` |

- `{{KNOWLEDGE_ROOT}}` **chưa tồn tại** → dự án chưa triển khai, bắt đầu từ Giai đoạn 1.
- **Đã tồn tại** → đọc `00-index/` trước, chỉ bổ sung case còn thiếu. Không ghi đè file đã có.

## Quy trình — 3 giai đoạn, không gộp làm một

### Giai đoạn 1 — Bản đồ case

Đọc `kb-structure.md` (cùng thư mục skill) để nắm cấu trúc thư mục. Liệt kê các case một Senior gặp khi xây app mới, nhóm theo lifecycle, gắn tier ưu tiên:

- **Tier 1 (làm trước — sai thì rất đắt để sửa):** khởi tạo project, chọn tech stack, architecture, chia module, database design, API design, authentication/authorization, security baseline, CI/CD, testing strategy.
- **Tier 2:** cache, queue, search, logging, monitoring, file storage, notification, payment, performance, Docker.
- **Tier 3:** scalability, multi-tenant, i18n, Kubernetes, cloud, cost optimization, legacy migration, incident response, refactoring, AI integration.

Danh sách trên là gợi ý — thêm/bớt theo context project thực tế. Output của giai đoạn này **chỉ là bản đồ**: tên case, 1 dòng mô tả, tier, đường dẫn file dự kiến. Chưa phân tích chi tiết.

### Giai đoạn 2 — Phân tích từng case, mỗi case một file

Đọc `case-template.md` (cùng thư mục skill) trước khi viết file đầu tiên. Mỗi lượt chỉ xử lý **1–3 case**, theo thứ tự tier. Với mỗi case: ghi một file `.md` vào đúng thư mục, đúng template. Xong lượt nào cập nhật index lượt đó rồi mới sang case tiếp theo — không dồn tất cả vào một lần output.

### Giai đoạn 3 — Index & routing cho Agent

Khi đã đủ case Tier 1, tạo các file theo spec trong `kb-structure.md`:

- `00-index/README.md` — bảng toàn bộ file.
- **Routing** — quy tắc canonical: repo này đã có `.claude/context-routing.md` (learning-loop đã cài), nên KHÔNG tạo `20-agent-context/routing.md` song song — chỉ đề xuất diff cập nhật `.claude/context-routing.md` (đổi các mục `(chưa có)` thành đường dẫn file knowledge vừa sinh), chờ người dùng duyệt theo `.claude/memory-update-policy.md`.
- Đề xuất (≤ 10 dòng) bổ sung vào `CLAUDE.md` để Agent biết tra index trước khi làm task — không copy cả knowledge base vào đó.
