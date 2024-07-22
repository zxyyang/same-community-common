package com.same.community.common.util.aop;

import java.lang.annotation.*;

/**
 * @author Zixuan.Yang
 * @date 2024/7/22 18:22
 */
@Target(ElementType.METHOD) // 表示该注解只能用于方法上
@Retention(RetentionPolicy.RUNTIME) // 在运行时保持有效
public @interface CheckUserContext {
}
