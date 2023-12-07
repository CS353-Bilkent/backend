package com.backend.artbase.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.backend.artbase.entities.ApiResponse;
import com.backend.artbase.entities.User;
import com.backend.artbase.errors.AuthRuntimeException;
import com.backend.artbase.services.UserService;
import com.backend.artbase.utils.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.header}")
    private String header;

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if (!hasAuthorizationBearer(request))
                throw new AuthRuntimeException("No authorization header is present", HttpStatus.UNAUTHORIZED);

            String accessToken = getAccessToken(request);

            if (!jwtTokenUtil.validateToken(accessToken))
                throw new AuthRuntimeException("Unauthorized", HttpStatus.UNAUTHORIZED);

            String user_id = jwtTokenUtil.getSubject(accessToken);

            User user = userService.getUserWithId(Integer.valueOf(user_id));

            request.setAttribute("user", user);
            request.setAttribute("accessToken", accessToken);
            filterChain.doFilter(request, response);
        } catch (AuthRuntimeException exception) {
            this.handleException(response, exception);
        }
    }

    // dursun bakarız
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(header);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private boolean hasAuthorizationBearer(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        return !(ObjectUtils.isEmpty(header) || !header.startsWith("Bearer"));
    }

    private String getAccessToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        String[] res = header.split(" ");

        if (res.length == 1)
            return "";

        return header.split(" ")[1].trim();
    }

    private void handleException(HttpServletResponse response, AuthRuntimeException exception) throws IOException {
        response.setContentType("application/json");
        response.setStatus(exception.getHttpStatus().value());
        response.getWriter()
                .write(objectMapper.writeValueAsString(ApiResponse.builder().operationResult(ApiResponse.OperationResult.builder()
                        .returnCode(Integer.toString(exception.getHttpStatus().value())).returnMessage(exception.getMessage()).build())
                        .build()));
    }

}
