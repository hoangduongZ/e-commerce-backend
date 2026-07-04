package com.electrostore.common.audit;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;

/** Cung cấp giá trị cho createdBy/updatedBy. */
public class AuditorAwareImpl implements AuditorAware<String> {

    // TODO(ECM-015): khi có Spring Security + JWT, lấy username từ SecurityContextHolder;
    // request chưa xác thực thì fallback "system".
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("system");
    }
}
