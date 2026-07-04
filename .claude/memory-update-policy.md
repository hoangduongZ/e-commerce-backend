# Memory Update Policy — quyền ghi và approval

## Ma trận quyền

| File / thư mục | Quyền của agent |
|---|---|
| `21-task-history/`, `22-lessons-learned/`, `23-debugging/` | Tự ghi, liệt kê trong báo cáo cuối task |
| `00-index/README.md` | Tự cập nhật, bắt buộc cùng lúc với mọi file mới |
| `19-decision-records/` (ADR) | Tự tạo với `status: proposed`; chuyển `accepted` phải được người dùng duyệt |
| Knowledge nội dung (`02`–`17`) | Sửa file có sẵn: tự làm nếu là bổ sung, trình diff nếu là thay đổi kết luận |
| `.claude/context-routing.md` | Chỉ đề xuất diff, chờ duyệt |
| `.claude/rules/`, `CLAUDE.md` | Bắt buộc hỏi trước — không bao giờ tự sửa |
| `.claude/memory-update-policy.md` | Không bao giờ tự sửa |

Commit knowledge tách khỏi commit code: `docs(knowledge): <nội dung>`. Team nhiều người: knowledge **thay đổi kết luận** (không phải chỉ bổ sung) đi qua PR review như code.

## An toàn dữ liệu (cứng — không có ngoại lệ)

Không bao giờ ghi secret, token, credential, PII hay dữ liệu production vào knowledge. Payload, log, stacktrace trong debugging notes phải ẩn danh hóa trước khi ghi — các file này được commit và chia sẻ cho cả team.

## Vệ sinh tri thức — hệ thống phải biết trừ, không chỉ biết cộng

- Mọi file knowledge phải trỏ đến ít nhất một đường dẫn code thật (frontmatter `refs` hoặc trong thân) — code đổi thì mới phát hiện được knowledge đã lệch.
- Đang làm task mà phát hiện lesson/knowledge sai → sửa hoặc xóa ngay trong wrap-up (agent tự làm được), nêu lý do trong commit message.
- Audit định kỳ bằng `/task-wrapup audit` (mỗi ~20 task hoặc mỗi tháng, tùy cái đến trước): lesson có `seen_count` không tăng qua 2 kỳ audit, ADR bị code hiện tại vượt qua, `refs` trỏ đến file không còn tồn tại → trình danh sách đề xuất xóa/cập nhật cho người dùng duyệt.

## Ngưỡng promote — Task note → Lesson → Rule → CLAUDE.md

| Cấp | Điều kiện lên cấp này | Ai quyết |
|---|---|---|
| Task note (trong task history) | Điều bất ngờ trong 1 task, chưa chắc lặp lại | Agent |
| Lesson learned | Gặp ≥ 2 lần, hoặc 1 lần nhưng gây mất > 30 phút; viết được thành "lần sau làm X thay vì Y" | Agent |
| Project rule (`.claude/rules/`) | Lesson đã áp dụng đúng ≥ 3 lần, hoặc vi phạm sẽ gây lỗi production; nén được ≤ 3 dòng | Người dùng duyệt |
| `CLAUDE.md` | Rule mà *mọi* task đều cần biết, không chỉ một nhóm task | Người dùng duyệt |

## Khi nào KHÔNG promote (chống nhiễu context)

- Chỉ đúng một lần, phụ thuộc context task cụ thể.
- Không nén được xuống ≤ 3 dòng mà vẫn đúng.
- Đã hết hạn (thư viện đã nâng cấp, module đã xóa) → xóa lesson thay vì giữ.
- `CLAUDE.md` có budget cứng ~30 dòng: muốn thêm 1 dòng phải chỉ ra dòng nào bỏ đi hoặc chứng minh còn chỗ.
