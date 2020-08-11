package com.example.apsdemo.admin.authority.security.handler;

import com.example.apsdemo.admin.common.api.CommonResult;
import com.example.apsdemo.admin.common.exception.Asserts;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 身份校验失败处理器，如 token 错误
 */
@Component
public class EntryPointUnauthorizedHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        Asserts.fail(response, CommonResult.forbidden(authException.getMessage()));
    }
}