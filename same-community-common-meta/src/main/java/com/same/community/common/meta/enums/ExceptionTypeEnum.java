package com.same.community.common.meta.enums;

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
     * Exception
     */
    Exception( "Exception"),

    /**
     * SameException
     */
    SameException( "SameException")

    /**
     * GlobalException
     */
    ,GlobalException( "GlobalException")


    ;


    private String message;


    public static ExceptionTypeEnum getExceptionTypeEnumByMessage(String message) {
        for (ExceptionTypeEnum exceptionTypeEnum : ExceptionTypeEnum.values()) {
            if (exceptionTypeEnum.getMessage().equals(message)) {
                return exceptionTypeEnum;
            }
        }
        return null;
    }

}
