-- =====================================================================
-- Baseline migration cho ElectroStore (EPIC-00 / ECM-004).
--
-- Quy ước migration (chi tiết trong README.md):
--   - Tên file: VyyyyMMddNNNN__mo_ta_ngan.sql (NNNN = số thứ tự trong ngày, bắt đầu 0001)
--   - Migration đã merge KHÔNG được sửa; muốn đổi schema → tạo migration mới
--   - Mỗi PR thay đổi schema phải kèm migration + entity + test tương ứng
-- =====================================================================

-- Chưa tạo business table ở giai đoạn base.
-- Schema nghiệp vụ được thêm theo từng EPIC (bắt đầu từ ECM-012: users, roles...).

COMMENT ON SCHEMA public IS 'ElectroStore - ecommerce backend schema';
