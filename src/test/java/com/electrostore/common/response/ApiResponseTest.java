package com.electrostore.common.response;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

class ApiResponseTest {

    @Test
    void okWrapsDataWithoutErrorOrMeta() {
        ApiResponse<String> response = ApiResponse.ok("payload");

        assertThat(response.success()).isTrue();
        assertThat(response.data()).isEqualTo("payload");
        assertThat(response.error()).isNull();
        assertThat(response.meta()).isNull();
    }

    @Test
    void errorWrapsErrorResponseWithoutData() {
        ErrorResponse error = new ErrorResponse("NOT_FOUND", "Không tìm thấy", null);

        ApiResponse<Void> response = ApiResponse.error(error);

        assertThat(response.success()).isFalse();
        assertThat(response.data()).isNull();
        assertThat(response.error().code()).isEqualTo("NOT_FOUND");
        assertThat(response.error().details()).isEmpty();
    }

    @Test
    void ofPagePutsItemsInDataAndPagingInMeta() {
        PageResponse<String> page = PageResponse.from(new PageImpl<>(List.of("a", "b"), PageRequest.of(0, 2), 5));

        ApiResponse<List<String>> response = ApiResponse.ofPage(page);

        assertThat(response.data()).containsExactly("a", "b");
        PageResponse.Meta meta = (PageResponse.Meta) response.meta();
        assertThat(meta.page()).isZero();
        assertThat(meta.size()).isEqualTo(2);
        assertThat(meta.totalElements()).isEqualTo(5);
        assertThat(meta.totalPages()).isEqualTo(3);
    }
}
