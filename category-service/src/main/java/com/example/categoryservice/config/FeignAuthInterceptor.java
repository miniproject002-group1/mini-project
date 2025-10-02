package com.example.categoryservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class FeignAuthInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if (requestAttributes == null) {
            return; // No HTTP request context (background job, etc.)
        }

        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        // Grab the Authorization header from the current request
        String authorizationHeader = request.getHeader("Authorization");

        System.out.println("Authorization header in interceptor: " + authorizationHeader);

        if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
            requestTemplate.header("Authorization", authorizationHeader);
        }
    }
}
