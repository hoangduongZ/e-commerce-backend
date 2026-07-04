package com.electrostore.common.response;

import java.util.List;
import org.springframework.data.domain.Page;

/**
 * Kết quả phân trang (page 0-based theo chuẩn Spring Pageable). Controller trả về qua {@link
 * ApiResponse#ofPage(PageResponse)} để đúng envelope api-conventions.
 */
public record PageResponse<T>(List<T> content, int page, int size, long totalElements, int totalPages) {

    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
    }

    public Meta meta() {
        return new Meta(page, size, totalElements, totalPages);
    }

    /** Metadata phân trang đặt trong {@code meta} của envelope. */
    public record Meta(int page, int size, long totalElements, int totalPages) {}
}
