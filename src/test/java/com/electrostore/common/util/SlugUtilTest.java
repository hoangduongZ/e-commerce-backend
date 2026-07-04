package com.electrostore.common.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SlugUtilTest {

    @Test
    void convertsVietnameseDiacritics() {
        assertThat(SlugUtil.toSlug("Điện thoại iPhone 15")).isEqualTo("dien-thoai-iphone-15");
        assertThat(SlugUtil.toSlug("Tủ lạnh Samsung Inverter 236L")).isEqualTo("tu-lanh-samsung-inverter-236l");
    }

    @Test
    void collapsesSpecialCharactersIntoSingleDash() {
        assertThat(SlugUtil.toSlug("Laptop Dell XPS 13 (2024) - 16GB/512GB!"))
                .isEqualTo("laptop-dell-xps-13-2024-16gb-512gb");
    }

    @Test
    void trimsLeadingAndTrailingDashes() {
        assertThat(SlugUtil.toSlug("  --Đắk Lắk--  ")).isEqualTo("dak-lak");
    }

    @Test
    void returnsEmptyForNullOrBlank() {
        assertThat(SlugUtil.toSlug(null)).isEmpty();
        assertThat(SlugUtil.toSlug("   ")).isEmpty();
    }
}
