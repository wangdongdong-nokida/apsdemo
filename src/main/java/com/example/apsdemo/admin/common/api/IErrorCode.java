package com.example.apsdemo.admin.common.api;

/**
 * 封装API的错误码
 * Created by SMY on 2020/8/6.
 */
public interface IErrorCode {
    /**
     * 状态码
     * @return
     */
    long getCode();

    /**
     * 信息
     * @return
     */
    String getMessage();
}