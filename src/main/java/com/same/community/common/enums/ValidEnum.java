package com.same.community.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zixuan.Yang
 * @date 2023/12/30 14:56
 */
@Getter
@AllArgsConstructor
public enum ValidEnum {

    /**
     * 无效
     */
    INVALID(0),

    /**
     * 有效
     */
    VALID(1);

    private Integer code;
}
