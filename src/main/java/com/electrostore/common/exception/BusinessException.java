package com.electrostore.common.exception;

import com.electrostore.common.response.ErrorResponse;
import java.util.List;
import lombok.Getter;

/**
 * Exception nghiệp vụ chung. GlobalExceptionHandler map exception này ra envelope lỗi chuẩn với
 * HTTP status lấy từ {@link ErrorCode}.
 */
@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final transient List<ErrorResponse.Detail> details;

    public BusinessException(ErrorCode errorCode) {
        this(errorCode, errorCode.getDefaultMessage(), List.of());
    }

    public BusinessException(ErrorCode errorCode, String message) {
        this(errorCode, message, List.of());
    }

    public BusinessException(ErrorCode errorCode, String message, List<ErrorResponse.Detail> details) {
        super(message);
        this.errorCode = errorCode;
        this.details = details == null ? List.of() : List.copyOf(details);
    }
}
