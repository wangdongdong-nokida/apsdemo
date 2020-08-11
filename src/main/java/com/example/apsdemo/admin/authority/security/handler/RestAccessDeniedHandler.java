package com.example.apsdemo.admin.authority.security.handler;

import com.example.apsdemo.admin.common.api.CommonResult;
import com.example.apsdemo.admin.common.exception.Asserts;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限校验处理器
 */
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        Asserts.fail(response, CommonResult.forbidden(accessDeniedException.getMessage()));
    }
}