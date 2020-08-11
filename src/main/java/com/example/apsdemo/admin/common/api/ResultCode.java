package com.example.apsdemo.admin.common.api;


/**
 * 枚举了一些常用API操作码
 * Created by SMY on 2020/8/6.
 */
public enum ResultCode implements IErrorCode {
    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功"),
    /**
     * 登录成功
     */
    LOGIN_SUCCESS(200, "登录成功"),

    /**
     * 操作失败
     */
    FAILED(500, "操作失败"),
    /**
     * 参数检验失败
     */
    VALIDATE_FAILED(404, "参数检验失败"),

    /**
     * 没有相关权限
     */
    FORBIDDEN(403, "没有相关权限"),

    /**
     * 账户被锁定，请联系管理员!
     */
    LOCKED_ACCOUNT(701, "账户被锁定，请联系管理员!"),

    /**
     * 密码过期，请联系管理员!
     */
    CREDENTIALS_EXPIRED(702, "密码过期，请联系管理员!"),

    /**
     * 账户过期，请联系管理员!
     */
    ACCOUNT_EXPIRED(703, "账户过期，请联系管理员!"),

    /**
     * 账户被禁用，请联系管理员!
     */
    DISABLED(704, "账户被禁用，请联系管理员!"),

    /**
     * 用户名或者密码输入错误，请重新输入!
     */
    BAD_CREDENTIALS(705, "用户名或者密码输入错误，请重新输入!"),

    /**
     * 未知状态，请联系管理员！
     */
    LOGIN_FAILED(706, "未知状态，请联系管理员！");


    private long code;
    private String message;

    private ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public long getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}