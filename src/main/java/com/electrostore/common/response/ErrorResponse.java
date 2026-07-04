package com.electrostore.common.response;

import java.util.List;

/**
 * Phần {@code error} trong envelope phản hồi. {@code code} là hằng SCREAMING_SNAKE_CASE ổn định để
 * FE map thông điệp (xem docs/main/api-conventions.md §4).
 */
public record ErrorResponse(String code, String message, List<Detail> details) {

    public ErrorResponse {
        details = details == null ? List.of() : List.copyOf(details);
    }

    /** Chi tiết lỗi theo field, dùng cho lỗi validation. */
    public record Detail(String field, String message) {}
}
