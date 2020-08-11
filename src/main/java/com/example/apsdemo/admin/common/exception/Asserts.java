package com.example.apsdemo.admin.common.exception;

import cn.hutool.json.JSONUtil;
import com.example.apsdemo.admin.common.api.CommonResult;
import com.example.apsdemo.admin.common.api.IErrorCode;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 断言处理类，用于抛出各种API异常
 * Created by SMY on 2020/8/6.
 */
public class Asserts {
    public static void fail(String message) {
        throw new ApiException(message);
    }

    public static void fail(IErrorCode errorCode) {
        throw new ApiException(errorCode);
    }

    public static void fail(HttpServletResponse response, CommonResult<String > commonResult) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control","no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(JSONUtil.parse(commonResult));
        response.getWriter().flush();
    }

}