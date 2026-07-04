---
id: adr-001-spring-boot-3-5-thay-vi-4-x
status: proposed
date: 2026-07-04
---
# ADR-001 — Dùng Spring Boot 3.5.x thay vì 4.1.0 cho base

> Phạm vi: ADR triển khai backend (khác với ADR hệ thống ADR-01..08 ở `e-commerce-docs/docs/backend-plan/01-kien-truc-tech-stack.md`). Xem mục "Hệ quả" về vấn đề nơi lưu ADR.

**Bối cảnh:** `spring init` tạo project với parent 4.1.0 (GA mới nhất). Tài liệu thiết kế ghi "Spring Boot 3.3+". Cần chốt version cho toàn bộ base trước khi xây nghiệp vụ.

**Quyết định:** Hạ về Spring Boot 3.5.3. Đồng thời đổi package root `com.electronic_store` → `com.electrostore` (đúng docs, idiomatic, không underscore) và main class → `EcommerceApplication`.

**Phương án đã loại & lý do:** Giữ 4.1.0 — hệ sinh thái (springdoc và một số lib) chưa đồng bộ ổn định với 4.x tại thời điểm dựng base; ưu tiên nền vững để làm nghiệp vụ, không debug tương thích ở giai đoạn foundation.

**Hệ quả:** (1) Spring Boot 3.5 đã hết OSS support ~06/2026 → **phải lên kế hoạch upgrade 4.x** trong vòng đời dự án; đặt lịch xem lại khi bắt đầu hardening/pre-prod. (2) springdoc pin 2.8.6 (dòng tương thích 3.x). (3) Tồn tại **hai nơi lưu ADR** — `e-commerce-docs/backend-plan` (ADR hệ thống) và backend `docs/knowledge/19-decision-records/` (learning loop) — cần người dùng chốt một nơi canonical cho ADR triển khai, tránh trùng (giống bài học routing canonical trong review bộ prompt).
