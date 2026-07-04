# Cấu trúc Knowledge Base

```text
{{KNOWLEDGE_ROOT}}/
  00-index/            # bảng routing — Agent đọc file này đầu tiên
  01-product-thinking/
  02-architecture/
  03-backend/
  04-frontend/
  05-database/
  06-infrastructure/
  07-security/
  08-testing/
  09-observability/
  10-performance/
  11-scalability/
  12-devops/
  13-incident-response/
  14-ai-engineering/
  15-cost-optimization/
  16-refactoring/
  17-patterns/
  18-anti-patterns/
  19-decision-records/ # ADR
  20-agent-context/    # rule & routing riêng cho Agent
```

Yêu cầu thiết kế: Agent đọc đúng file theo task mà không cần nạp toàn bộ knowledge base; mở rộng được tới hàng trăm file; dễ index bằng RAG hoặc Context Engine; dễ cập nhật sau mỗi task thực tế.

## Spec file index — `00-index/README.md`

Bảng toàn bộ file trong knowledge base, mỗi dòng gồm: đường dẫn, mục đích (1 câu), khi nào Agent cần đọc, tags, độ ưu tiên khi nạp context (P1/P2/P3). Cập nhật ngay mỗi khi thêm/sửa file — index sai còn tệ hơn không có index.

## Spec file routing — `20-agent-context/routing.md`

Mapping *loại task → danh sách file cần đọc*, sắp theo độ ưu tiên. Ví dụ:

| Loại task | File cần đọc |
|---|---|
| Thêm API mới | `api-design`, `security-baseline` |
| Thay đổi schema DB | `database-design`, `19-decision-records/` liên quan |
| Bug production | `incident-response`, `logging`, `monitoring` |
| Chọn công nghệ mới | `tech-stack`, `architecture`, ADR liên quan |

Chỉ liệt kê task type đã có file tương ứng — không tạo mapping trỏ đến file chưa tồn tại.

> Routing canonical của repo này là `.claude/context-routing.md` (xem quy tắc trong `SKILL.md` Giai đoạn 3) — không tạo routing song song ở `20-agent-context/`.
