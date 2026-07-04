---
name: task-wrapup
description: Capture reusable knowledge after finishing a task — write task history, lessons learned, debugging notes, or ADR drafts per the memory update policy. Use after completing a significant task, fixing a hard bug, or making an architecture decision.
---

# Task Wrap-up — ghi tri thức sau task

**Điều kiện tiên quyết:** task đã được verify (test pass hoặc chạy thật và quan sát được kết quả). Task chưa verify thì tri thức rút ra chưa đáng tin — quay lại verify trước, không wrap-up.

Đọc `.claude/memory-update-policy.md` trước. Mặc định là **không ghi gì** — chỉ ghi khi khớp bảng dưới. Hai quy tắc khi ghi: (1) trước khi tạo lesson mới, tìm lesson cùng `area` đã có — trùng thì tăng `seen_count` thay vì tạo file mới; (2) mọi file ghi ra phải trỏ đến ít nhất một đường dẫn code thật. Ghi xong file nào phải cập nhật `00-index/README.md` ngay.

| Điều xảy ra trong task | Ghi vào |
|---|---|
| Task thay đổi nhiều file / có quyết định đáng nhớ / chạm module quan trọng | `21-task-history/YYYY-MM-DD-<slug>.md` |
| Chọn công nghệ, đổi kiến trúc, trade-off có hệ quả dài hạn | ADR trong `19-decision-records/` (`status: proposed`) |
| Bug tốn > 30 phút vì thiếu tri thức project | `23-debugging/<slug>.md` + cân nhắc lesson |
| Nhận ra pattern/convention gặp ≥ 2 lần | `22-lessons-learned/lesson-<slug>.md` |
| Lesson có sẵn được áp dụng lần thứ 3 | Tăng `seen_count`, đề xuất promote thành rule (chờ duyệt) |
| Task routine, không có gì mới | Không ghi — báo "không có tri thức mới" là kết quả hợp lệ |

## Template task history (≤ 30 dòng/file)

```markdown
---
id: <YYYY-MM-DD-slug>
type: task-history
area: [backend, auth]
refs: [<đường dẫn code chính đã chạm>]
related: [<id ADR/lesson liên quan>]
---
# <Tên task>
**Đã làm:** <2-3 câu>
**Quyết định & lý do:** <quyết định nào, vì sao chọn>
**Bất ngờ / giả định sai:** <điều không như dự đoán>
**Lần sau nên:** <hành động cụ thể>
```

## Template lesson learned

```markdown
---
id: lesson-<slug>
type: lesson
area: [<nhóm task>]
refs: [<đường dẫn code liên quan>]
seen_count: <số lần gặp>
promoted: false
---
**Bài học:** <1-2 câu, dạng "làm X thay vì Y">
**Bối cảnh phát hiện:** <task nào, chuyện gì xảy ra>
**Cách áp dụng:** <nhận ra tình huống này bằng dấu hiệu gì, làm gì>
```

## Template ADR

```markdown
---
id: adr-<số>-<slug>
status: proposed | accepted | superseded
date: <YYYY-MM-DD>
---
# <Quyết định>
**Bối cảnh:** <vấn đề, constraint>
**Quyết định:** <chọn gì>
**Phương án đã loại & lý do:** <ngắn gọn>
**Hệ quả:** <đánh đổi chấp nhận, khi nào cần xem lại>
```

## Chế độ audit — `/task-wrapup audit`

Chạy mỗi ~20 task hoặc mỗi tháng: quét `22-lessons-learned/`, ADR và các `refs` theo mục "Vệ sinh tri thức" trong `memory-update-policy.md`, trình danh sách đề xuất xóa/cập nhật cho người dùng duyệt. Tri thức sai nằm lại lâu làm agent kém đi theo thời gian — audit là một phần của vòng học, không phải việc phụ.
