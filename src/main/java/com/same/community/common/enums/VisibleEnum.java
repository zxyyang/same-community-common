package com.same.community.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zixuan.Yang
 * @date 2023/12/12 16:14
 */
@Getter
@AllArgsConstructor
public enum VisibleEnum {

    /**
     * 不可见
     */
    INVISIBLE(0),

    /**
     * 可见
     */

    VISIBLE(1);

    private Integer code;
}
