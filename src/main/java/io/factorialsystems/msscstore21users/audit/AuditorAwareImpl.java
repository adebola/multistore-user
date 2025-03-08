package io.factorialsystems.msscstore21users.audit;


import io.factorialsystems.msscstore21users.security.JwtTokenWrapper;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(JwtTokenWrapper.getUserName());
    }
}
