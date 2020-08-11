package com.example.apsdemo.admin.authority.security.handler;



import com.example.apsdemo.admin.common.api.CommonResult;
import com.example.apsdemo.admin.common.api.ResultCode;
import com.example.apsdemo.admin.common.exception.Asserts;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败操作
 *
 */
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        /*
        如下是判断登录失败的几种状态,返回相应的错误信息
         */
        if (e instanceof LockedException) {
            Asserts.fail(httpServletResponse, CommonResult.unauthorized(e.getMessage(), ResultCode.LOCKED_ACCOUNT));
        } else if (e instanceof CredentialsExpiredException) {
            Asserts.fail(httpServletResponse, CommonResult.unauthorized(e.getMessage(), ResultCode.CREDENTIALS_EXPIRED));
        } else if (e instanceof AccountExpiredException) {
            Asserts.fail(httpServletResponse, CommonResult.unauthorized(e.getMessage(), ResultCode.ACCOUNT_EXPIRED));
        } else if (e instanceof DisabledException) {
            Asserts.fail(httpServletResponse, CommonResult.unauthorized(e.getMessage(), ResultCode.DISABLED));
        } else if (e instanceof BadCredentialsException) {
            Asserts.fail(httpServletResponse, CommonResult.unauthorized(e.getMessage(), ResultCode.BAD_CREDENTIALS));
        } else {
            Asserts.fail(httpServletResponse, CommonResult.unauthorized(e.getMessage(), ResultCode.LOGIN_FAILED));
        }
    }
}