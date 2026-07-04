package com.electrostore.common.response;

import java.util.List;

/**
 * Envelope phản hồi thống nhất cho mọi endpoint (docs/main/api-conventions.md §2).
 *
 * <p>Success: {@code {"success": true, "data": {...}, "error": null, "meta": {...}}}. Error:
 * {@code {"success": false, "data": null, "error": {...}, "meta": null}}. {@code meta} chỉ khác
 * null khi cần metadata (phân trang...).
 */
public record ApiResponse<T>(boolean success, T data, ErrorResponse error, Object meta) {

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null, null);
    }

    public static <T> ApiResponse<T> ok(T data, Object meta) {
        return new ApiResponse<>(true, data, null, meta);
    }

    /**
     * Phản hồi phân trang theo api-conventions §3: {@code data} là mảng item, thông tin trang nằm
     * trong {@code meta}.
     */
    public static <T> ApiResponse<List<T>> ofPage(PageResponse<T> page) {
        return new ApiResponse<>(true, page.content(), null, page.meta());
    }

    public static <T> ApiResponse<T> error(ErrorResponse error) {
        return new ApiResponse<>(false, null, error, null);
    }
}
