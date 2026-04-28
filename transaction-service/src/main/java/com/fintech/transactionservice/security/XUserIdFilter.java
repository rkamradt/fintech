package com.fintech.transactionservice.security;

import com.fintech.transactionservice.client.UserServiceClient;
import com.fintech.transactionservice.dto.UserContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class XUserIdFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(XUserIdFilter.class);

    private final UserServiceClient userServiceClient;

    public XUserIdFilter(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String userId = request.getHeader("X-User-ID");

        if (userId != null && !userId.isBlank()) {
            try {
                UserContext ctx = userServiceClient.getUser(userId);
                if (ctx != null) {
                    String role = "ROLE_" + ctx.role().toUpperCase();
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            ctx,
                            null,
                            List.of(new SimpleGrantedAuthority(role))
                    );
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception e) {
                log.warn("User lookup failed for X-User-ID {}: {}", userId, e.getMessage());
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}
