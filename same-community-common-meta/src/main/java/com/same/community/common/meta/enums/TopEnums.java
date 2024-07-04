package com.same.community.common.meta.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zixuan.Yang
 * @date 2024/2/3 11:19
 */
@Getter
@AllArgsConstructor
public enum TopEnums {
    TOP(1, "置顶"),
    UN_TOP(0, "未置顶");

    private final Integer code;
    private final String desc;
}
