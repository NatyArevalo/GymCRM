package com.gymcrm.gymcrm.Configurations;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@WebFilter("/*")
public class Configuration implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;

            String transactionId = UUID.randomUUID().toString();
            MDC.put("transactionId", transactionId);

            try {
                logRequest(httpRequest, transactionId);

                chain.doFilter(request, response);

                logResponse(httpResponse, transactionId);
            } finally {
                MDC.remove("transactionId");
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private void logRequest(HttpServletRequest request, String transactionId) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        System.out.println(String.format("Transaction ID: %s | Incoming Request: method=%s, uri=%s", transactionId, method, uri));
    }

    private void logResponse(HttpServletResponse response, String transactionId) {
        int status = response.getStatus();
        System.out.println(String.format("Transaction ID: %s | Outgoing Response: status=%d", transactionId, status));
    }

    @Override
    public void destroy() {
        
    }
}
