---
id: 2026-07-04-fix-ci-trivy-and-cves
type: task-history
area: [devops, security]
refs: [.github/workflows/ci.yml, pom.xml]
related: []
---
# Sửa lỗi CI pipeline và vá lỗ hổng bảo mật (CVEs)

**Đã làm:** 
- Sửa lỗi action Trivy không chạy được do sai tag version (`0.29.0` không tồn tại, đổi thành `v0.36.0`).
- Nâng cấp Spring Boot từ `3.5.3` lên `3.5.12` để vá lỗ hổng của `spring-boot-starter-actuator`.
- Override explicitly `postgresql.version` lên `42.7.11` trong `pom.xml` để vá lỗ hổng CVE-2026-42198 (vì Spring Boot 3.5.12 chỉ update lên 42.7.10).
- Bổ sung cấu hình checkstyle `config/checkstyle/checkstyle.xml` (trước đó chưa commit) để fix lỗi maven-checkstyle-plugin.
- Bổ sung `ErrorResponse.java` bị thiếu gây lỗi biên dịch trên CI.

**Quyết định & lý do:** 
- Chọn nâng version Spring Boot parent thay vì override dependency lẻ tẻ để đảm bảo tính đồng bộ của BOM (Bill of Materials), giúp fix cùng lúc lỗ hổng của Actuator và PostgreSQL JDBC driver.

**Bất ngờ / giả định sai:** 
- Action của aquasecurity/trivy-action yêu cầu prefix `v` trong tag name (VD: `v0.36.0` thay vì `0.36.0`). 
- Có nhiều file code và cấu hình tồn tại ở local nhưng chưa được add/commit, dẫn đến việc pass ở local nhưng CI fail (lỗi thiếu class và file cấu hình).

**Lần sau nên:** 
- Luôn kiểm tra kỹ `git status` để đảm bảo tất cả file mới (untracked) đã được staging trước khi push.
- Khi lấy version của GitHub Actions, cần check kỹ release tag trên repo đích.
