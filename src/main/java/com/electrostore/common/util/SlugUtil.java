package com.electrostore.common.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

/** Sinh slug URL-safe từ tiếng Việt có dấu, vd "Điện thoại iPhone 15" → "dien-thoai-iphone-15". */
public final class SlugUtil {

    private static final Pattern DIACRITICS = Pattern.compile("\\p{M}+");
    private static final Pattern NON_ALNUM = Pattern.compile("[^a-z0-9]+");
    private static final Pattern EDGE_DASHES = Pattern.compile("^-+|-+$");

    private SlugUtil() {}

    public static String toSlug(String input) {
        if (input == null || input.isBlank()) {
            return "";
        }
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String noMarks = DIACRITICS.matcher(normalized).replaceAll("");
        String ascii = noMarks.replace('đ', 'd').replace('Đ', 'D');
        String slug = NON_ALNUM.matcher(ascii.toLowerCase(Locale.ROOT)).replaceAll("-");
        return EDGE_DASHES.matcher(slug).replaceAll("");
    }
}
