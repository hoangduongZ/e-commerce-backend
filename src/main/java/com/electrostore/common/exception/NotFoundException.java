package com.electrostore.common.exception;

/** Không tìm thấy tài nguyên — map ra 404 với code RESOURCE_NOT_FOUND. */
public class NotFoundException extends BusinessException {

    public NotFoundException(String message) {
        super(ErrorCode.RESOURCE_NOT_FOUND, message);
    }

    public NotFoundException(String resource, Object id) {
        super(ErrorCode.RESOURCE_NOT_FOUND, "%s không tồn tại: %s".formatted(resource, id));
    }
}
