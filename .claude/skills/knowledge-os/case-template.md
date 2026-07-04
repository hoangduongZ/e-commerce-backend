# Template cho mỗi file case

## Frontmatter bắt buộc — để Agent/RAG lọc được mà không cần đọc thân file

```markdown
---
id: <slug>
title: <tên case>
tier: 1 | 2 | 3
tags: [architecture, database, ...]
when_to_read: <Agent cần đọc file này khi làm loại task gì>
related: [<id các file liên quan>]
updated: <YYYY-MM-DD>
---
```

## Thân file — đủ 8 mục, mỗi mục viết để ra quyết định được, không viết cho đủ chữ

1. **Bối cảnh & vấn đề** — case này giải quyết gì; làm sai thì hậu quả gì đến product, engineering, business, security, cost, operation.
2. **Senior nghĩ gì khi gặp case này** — họ tự hỏi câu gì, ưu tiên gì, sợ rủi ro gì; cái gì phải quyết ngay, cái gì hoãn được, cái gì tuyệt đối không làm.
3. **Luồng tư duy từng bước** — xác định bản chất vấn đề → constraint → đánh giá option → chọn hướng → thiết kế để dễ thay đổi → rủi ro vận hành. Mỗi bước phải giải thích *vì sao Senior nghĩ vậy*, không chỉ *làm gì*.
4. **Các option & trade-off** — với mỗi option: khi nào dùng, khi nào không, ưu/nhược, trade-off, sai lầm thường gặp. (Ví dụ: REST vs GraphQL vs gRPC; SQL vs NoSQL; monolith vs modular monolith vs microservice; queue vs sync API; serverless vs container vs VM.)
5. **Sai lầm Junior hay mắc** — cụ thể theo case này kèm hậu quả thật: over/under-engineering, copy architecture big-tech sai context, premature optimization, không nghĩ đến failure case, không nghĩ đến vận hành production, database khó migrate, bỏ qua security.
6. **Dấu hiệu thiết kế hiện tại không còn phù hợp** — tín hiệu từ performance, team velocity, bug production, cost, vận hành, business requirement.
7. **Checklist quyết định** — các câu hỏi Senior phải trả lời được trước khi chốt phương án; viết dạng câu hỏi có/không, kèm ngưỡng cụ thể khi có thể.
8. **Học sâu hơn** — 3–7 tài liệu đáng đọc nhất (official docs, RFC, engineering blog, book, postmortem, open source codebase), kèm một dòng lý do đọc. Không liệt kê tràn lan.

## Tiêu chí chất lượng — áp dụng cho mọi file

- Ưu tiên **quyết định** hơn mô tả: đoạn nào không giúp Agent chọn được hướng đi thì cắt.
- Cụ thể hơn tổng quát: nêu con số, ngưỡng, failure mode thật — không viết "cần cân nhắc kỹ lưỡng".
- Luôn nêu trade-off. Không có lựa chọn nào là "best practice" vô điều kiện.
- Ghi rõ khi nào nên **đơn giản hóa**, khi nào nên **thiết kế dài hạn**, và khi nào Agent nên **dừng lại hỏi người dùng** thay vì tự quyết.
- Quyết định kiến trúc lớn → ghi ADR vào `{{DECISION_RECORDS_ROOT}}`.
- File dài quá ~200 dòng → tách nhỏ, link chéo qua `related`.
