package com.electrostore.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Bộ error code nền tảng. Giá trị enum name chính là {@code error.code} trả về cho client — không
 * đổi tên code đã public. Code nghiệp vụ chi tiết (PRODUCT_NOT_FOUND, COUPON_INVALID...) thêm dần
 * theo từng EPIC.
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống, vui lòng thử lại sau"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Yêu cầu không hợp lệ"),
    VALIDATION_ERROR(HttpStatus.UNPROCESSABLE_ENTITY, "Dữ liệu không hợp lệ"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Chưa xác thực"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "Không có quyền truy cập"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy tài nguyên"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "Phương thức không được hỗ trợ"),
    CONFLICT(HttpStatus.CONFLICT, "Xung đột dữ liệu"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy tài nguyên"),
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "Tài nguyên đã tồn tại"),
    INSUFFICIENT_STOCK(HttpStatus.CONFLICT, "Không đủ hàng tồn kho"),
    BUSINESS_RULE_VIOLATION(HttpStatus.UNPROCESSABLE_ENTITY, "Vi phạm quy tắc nghiệp vụ");

    private final HttpStatus status;
    private final String defaultMessage;
}
