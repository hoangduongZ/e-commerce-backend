package com.electrostore.common.support;

import com.electrostore.common.exception.NotFoundException;
import com.electrostore.common.response.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller CHỈ tồn tại trong test sources để verify envelope + GlobalExceptionHandler end-to-end
 * khi chưa có business endpoint. Không copy sang main sources.
 */
@RestController
@RequestMapping("/api/v1/test-support")
public class EnvelopeTestController {

    @GetMapping("/ok")
    public ApiResponse<Map<String, String>> ok() {
        return ApiResponse.ok(Map.of("ping", "pong"));
    }

    @GetMapping("/not-found")
    public ApiResponse<Void> notFound() {
        throw new NotFoundException("Product", 42);
    }

    @PostMapping("/validate")
    public ApiResponse<SampleRequest> validate(@Valid @RequestBody SampleRequest request) {
        return ApiResponse.ok(request);
    }

    public record SampleRequest(@NotBlank String name, @Min(1) int quantity) {}
}
