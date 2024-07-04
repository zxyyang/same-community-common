package com.same.community.common.util.utils;

import com.same.community.common.meta.exception.SameException;
import org.apache.commons.lang3.StringUtils;

public class Preconditions {
    /**
     * 校验参数
     *
     * @param expression 判断条件
     * @param code 错误编号
     * @param message 错误信息
     */
    public static void checkArgument(boolean expression, int code, String message) {
        if (!expression) {
            throw new SameException(message, code);
        }
    }


    public static void checkArgument(boolean expression, String message) {
        if (!expression) {
            throw new SameException(message, 400);
        }
    }


    /**
     * 校验准入资格
     *
     * @param expression 判断条件
     * @param code 错误编号
     * @param message 错误信息
     */
    public static void checkState(boolean expression, int code, String message) {
        if (!expression) {
            throw new SameException(message, code);
        }
    }


    /**
     * 校验对象是否为空
     *
     * @param reference 对象
     * @param code 错误编号
     * @param message 错误信息
     */
    public static <T> T checkNotNull(T reference, int code, String message) {
        if (reference == null) {
            throw new SameException(message, code);
        }
        return reference;
    }


    public static <T> T checkNotNull(T reference, String message) {
        if (reference == null) {
            throw new SameException(message, 400);
        }
        return reference;
    }


    /**
     * 校验字符串是否为 StringUtils#isBlank
     *
     * @param reference 字符串
     * @param code 错误编号
     * @param message 错误信息
     */
    public static String checkNotBlank(String reference, int code, String message) {
        if (StringUtils.isBlank(reference)) {
            throw new SameException(message, code);
        }
        return reference;
    }

    public static String checkNotBlank(String reference, String message) {
        if (StringUtils.isBlank(reference)) {
            throw new SameException(message, 400);
        }
        return reference;
    }


}
