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
        String message = "Não autorizado";

        // Diferencia o tipo de erro baseado na exceção
        if (authException.getMessage() != null) {
            String exceptionMessage = authException.getMessage().toLowerCase();

            if (exceptionMessage.contains("bad credentials") ||
                    exceptionMessage.contains("email ou senha")) {
                message = "Email ou senha incorretos";
            } else if (exceptionMessage.contains("token") ||
                    exceptionMessage.contains("jwt")) {
                message = "Token inválido ou expirado";
            } else if (exceptionMessage.contains("access denied")) {
                message = "Acesso negado";
            } else {
                message = authException.getMessage();
            }
        }

        StandardError errorDetails = new StandardError();
        errorDetails.setStatus(status.value());
        errorDetails.setError("Unauthorized");
        errorDetails.setMessage(message);
        errorDetails.setPath(request.getRequestURI());

        response.setStatus(status.value());
        response.setContentType("application/json");

        response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
    }
}