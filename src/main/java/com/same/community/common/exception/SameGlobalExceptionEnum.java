package com.same.community.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author Zixuan.Yang
 * @date 2024/7/3 11:50
 */
@Getter
@AllArgsConstructor
public enum SameGlobalExceptionEnum {

    /**
     * 未登录
     */
    NOT_LOGIN(HttpStatus.UNAUTHORIZED.value(), "未登录"),

    /**
     * 无权限
     */
    NO_PERMISSION(HttpStatus.FORBIDDEN.value(), "无权限"),

    /**
     * 参数错误
     */
    PARAM_ERROR(HttpStatus.BAD_REQUEST.value(), "参数错误"),

    /**
     * NOT_FOUND
     */
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "NOT_FOUND"),


    ;
    private Integer code;

    private String message;
}
