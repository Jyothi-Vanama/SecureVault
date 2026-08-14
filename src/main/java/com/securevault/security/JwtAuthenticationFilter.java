package com.securevault.security;

import java.io.IOException;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.securevault.entity.User;
import com.securevault.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger =
            LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public JwtAuthenticationFilter(
            JwtUtil jwtUtil,
            UserService userService) {

        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        logger.debug(
                "Incoming request: {} {}",
                request.getMethod(),
                request.getRequestURI()
        );

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            logger.debug("No valid Bearer token found");

            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);

        String email = jwtUtil.extractUsername(jwt);

        logger.debug("JWT username extracted successfully");

        if (email != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            User user = userService.findByEmail(email);

            if (user != null &&
                    jwtUtil.validateToken(jwt, user.getEmail())) {

                logger.debug(
                        "JWT authentication successful for user: {}",
                        email
                );

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                Collections.emptyList()
                        );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);

                logger.debug("Security context authentication established");
            } else {
                logger.warn("JWT authentication failed");
            }
        }

        filterChain.doFilter(request, response);
    }
}