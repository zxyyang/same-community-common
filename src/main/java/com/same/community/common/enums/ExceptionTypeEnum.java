package com.same.community.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zixuan.Yang
 * @date 2024/7/3 20:10
 */
@Getter
@AllArgsConstructor
public enum ExceptionTypeEnum {

    /**
     * SameException
     */
    SameException(600, "SameException")

    /**
     * GlobalException
     */
    ,GlobalException(500, "GlobalException")
    ;

    private Integer code;

    private String message;

    public static ExceptionTypeEnum getExceptionTypeEnumByCode(int code) {
        for (ExceptionTypeEnum exceptionTypeEnum : ExceptionTypeEnum.values()) {
            if (exceptionTypeEnum.getCode() == code) {
                return exceptionTypeEnum;

            }

        }
        return null;
    }

}
