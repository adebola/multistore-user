package io.factorialsystems.msscstore21users.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class TenantIdCaptureFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("TenantIdCaptureFilter===================");

        String tenantId = request.getHeader("X-TenantID");

        if (tenantId == null || tenantId.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("{\"error\": \"No organisation supplied\"}");
            response.getWriter().flush();
            return;
        }

        TenantContext.setContext(tenantId);
        filterChain.doFilter(request, response);
    }
}