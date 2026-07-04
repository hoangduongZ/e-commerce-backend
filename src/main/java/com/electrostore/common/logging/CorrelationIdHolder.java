package com.electrostore.common.logging;

import org.slf4j.MDC;

/** Truy cập correlation id của request hiện tại (lưu trong MDC, key {@code correlationId}). */
public final class CorrelationIdHolder {

    public static final String HEADER = "X-Correlation-ID";
    public static final String MDC_KEY = "correlationId";

    private CorrelationIdHolder() {}

    public static String get() {
        return MDC.get(MDC_KEY);
    }

    public static void set(String correlationId) {
        MDC.put(MDC_KEY, correlationId);
    }

    public static void clear() {
        MDC.remove(MDC_KEY);
    }
}
