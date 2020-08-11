package com.example.apsdemo.admin.common.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * description: LoginCommonResult <br>
 * date: 2020/8/6 22:54 <br>
 * author: 11752 <br>
 * version: 1.0 <br>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginCommonResult {

    private long code;
    private String message;
    private String token;
    private List<String> authoritys;


    /**
     * 成功返回结果
     */
    public static LoginCommonResult success(List<String> authoritys, String token) {
        return new LoginCommonResult(ResultCode.LOGIN_SUCCESS.getCode(), ResultCode.LOGIN_SUCCESS.getMessage(), token, authoritys);
    }

}
