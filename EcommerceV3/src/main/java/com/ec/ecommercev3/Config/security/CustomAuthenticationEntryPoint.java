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

        // Define o status HTTP
        HttpStatus status = HttpStatus.NOT_FOUND;

        // Cria o objeto de erro personalizado
        StandardError errorDetails = new StandardError();
        errorDetails.setStatus(status.value());
        errorDetails.setError("Resource not found");
        errorDetails.setMessage("Email ou senha incorretos");
        errorDetails.setPath(request.getRequestURI());

        // Configura o cabeçalho e o tipo de conteúdo da resposta como JSON
        response.setStatus(status.value());
        response.setContentType("application/json");

        // Converte o objeto de erro para JSON e escreve na resposta
        response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
    }
}