package com.henheang.commonapi.components;

import com.kosign.wepoint.common.service.log.WePointAdminLoggingService;
import com.kosign.wepoint.common.service.log.WePointOpenApiLoggingService;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class CustomInterceptor implements HandlerInterceptor {
    private final WePointAdminLoggingService weBillLoggingService;
    private final WePointOpenApiLoggingService wePointOpenApiLoggingService;

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        if (DispatcherType.REQUEST.name().equals(request.getDispatcherType().name())
                && request.getMethod().equals(HttpMethod.GET.name())) {
            if(request.getRequestURI().contains("/api/wpc/**")) {
                weBillLoggingService.logRequest(request, null);
            } else if (request.getRequestURI().contains("/api/client/**")) {
                wePointOpenApiLoggingService.logRequest(request, null);
            }
        } else if (DispatcherType.REQUEST.name().equals(request.getDispatcherType().name())
                && request.getMethod().equals(HttpMethod.POST.name())) {
        }
        return true;
    }
}