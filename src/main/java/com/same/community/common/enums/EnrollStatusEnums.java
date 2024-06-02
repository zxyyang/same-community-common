package com.same.community.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zixuan.Yang
 * @date 2024/2/3 13:24
 */
@Getter
@AllArgsConstructor
public enum EnrollStatusEnums {
    /**
     * 未参加
     */
    NOT_ENROLLED(0, "未参加"),
    /**
     * 已参加
     */
    ENROLLED(1, "已参加"),
    /**
     * 审核中
     */
    AUDITING(2, "审核中"),
    /**
     * 审核通过
     */
    AUDIT_PASS(3, "审核通过"),
    /**
     * 审核不通过
     */
    AUDIT_REFUSE(4, "审核不通过"),
    /**
     * 已踢出
     */
    KICKED(5, "已踢出"),

    /**
     * 已退出
     */
    QUIT(6, "已退出"),
    ;

    private final Integer code;
    private final String desc;
}
