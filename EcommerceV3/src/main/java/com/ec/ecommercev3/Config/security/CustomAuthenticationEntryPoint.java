package com.ec.ecommercev3.Config.security;

import com.ec.ecommercev3.Exception.StandardError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        HttpStatus status = HttpStatus.UNAUTHORIZED;

        StandardError errorDetails = new StandardError();
        errorDetails.setStatus(status.value());
        errorDetails.setError("Unauthorized");
        errorDetails.setMessage("Email ou senha incorretos");
        errorDetails.setPath(request.getRequestURI());

        response.setStatus(status.value());
        response.setContentType("application/json");

        response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
    }
}