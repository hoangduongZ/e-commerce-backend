# ElectroStore Backend

Ecommerce backend đồ điện tử — Modular Monolith trên Spring Boot 3.5 / Java 21 / Maven. Cách chạy & convention chi tiết: `README.md`.

## Agent Learning System

- Trước mỗi task: phân loại task và đọc theo `.claude/context-routing.md` — không quét toàn bộ knowledge/repo.
- Rule bắt buộc của project: `.claude/rules/coding-rules.md`.
- Sau task đáng nhớ (quyết định lớn, bug khó, pattern mới): chạy `/task-wrapup`.
- Quyền ghi và approval: theo `.claude/memory-update-policy.md`. Không tự sửa file trong `.claude/` — chỉ đề xuất diff.

## Knowledge Base

Trước khi làm task thiết kế/kiến trúc (thêm module, chọn công nghệ, thay đổi schema...), tra `docs/knowledge/00-index/README.md` và đọc đúng file theo `.claude/context-routing.md`. Để xây hoặc bổ sung knowledge base, gọi skill `/knowledge-os`.
