package com.same.community.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zixuan.Yang
 * @date 2023/12/30 15:04
 */
@Getter
@AllArgsConstructor
public enum UserStatusEnum {
    //状态 0：可用 -1：封号 1：异常
    /**
     * 正常
     */
    NORMAL(0),

    /**
     * 封号
     */
    BAN(-1),

    /**
     * 异常
     */
    ABNORMAL(1);

    private Integer code;

}
