package com.same.community.common.meta.exception;

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





    ;
    private Integer code;

    private String message;
}
