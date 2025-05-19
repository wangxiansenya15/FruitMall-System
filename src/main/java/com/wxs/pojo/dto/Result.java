package com.wxs.pojo.dto;

import cn.hutool.http.HttpStatus;
import lombok.Data;

@Data
public class Result<T> {
    private int code;
    private String message;
    private T data;

    public Result() {
    }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 基础成功方法（带数据）
    public static <T> Result<T> success(T data) {
        return new Result<>(HttpStatus.HTTP_OK, "操作成功", data);
    }

    // 成功方法（可自定义消息）
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(HttpStatus.HTTP_OK, message, data);
    }

    // 成功无数据（用于删除等操作）
    public static Result<Void> success() {
        return new Result<>(HttpStatus.HTTP_OK, "操作成功", null);
    }

    // 通用失败方法
    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message, null);
    }

    // 快捷失败方法（常用错误码） 返回400 错误码
    public static Result<?> requestError(String message) {
        return new Result<>(HttpStatus.HTTP_BAD_REQUEST, message, null);
    }

    //返回401 未登录
    public static Result<Void> unauthorized(String message) {
        return new Result<>(HttpStatus.HTTP_UNAUTHORIZED, message, null);
    }

    // 返回403 访问被拒绝
    public static Result<?> forbidden(String message) {
        return new Result<>(HttpStatus.HTTP_FORBIDDEN, message, null);
    }

    // 返回404 资源不存在
    public static Result<?> notFound(String message) {
        return new Result<>(HttpStatus.HTTP_NOT_FOUND, message, null);
    }

    //  返回500 服务器错误
    public static Result<?> serverError(String message) {
        return new Result<>(HttpStatus.HTTP_INTERNAL_ERROR, message, null);
    }


}