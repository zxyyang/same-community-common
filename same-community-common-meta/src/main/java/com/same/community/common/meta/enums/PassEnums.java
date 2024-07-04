package com.same.community.common.meta.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zixuan.Yang
 * @date 2024/2/3 13:43
 */
@Getter
@AllArgsConstructor
public enum PassEnums {

    /**
     * 通过
     */
    PASS(1),

    /**
     * 拒绝
     */
    REFUSE(2);

    private final Integer code;

    public static PassEnums getEnumByCode(Integer code) {
        for (PassEnums value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
