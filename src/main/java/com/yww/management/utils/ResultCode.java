package com.yww.management.utils;

/**
 * <p>
 *      结果状态码枚举
 * </p>
 *
 * @EnumName ResultCode
 * @Author yww
 * @Date 2022/10/12 21:05
 */
public enum ResultCode {

    /**
     * 成功
     */
    SUCCESS(200, "成功"),

    /**
     * 服务器错误
     */
    FAILED(500, "服务器发生错误");

    private final int status;
    private final String message;

    ResultCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }

}