package com.same.community.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zixuan.Yang
 * @date 2024/1/9 01:43
 */
@Getter
@AllArgsConstructor
public enum IdEnum {
    /**
     * 用户ID前缀
     * 不要固定前缀
     */
//    USER_ID(11L),
    /**
     * 帖子ID前缀
     */
    POST_ID(12L),
    /**
     * 寻搭ID前缀
     */
    MATCH_ID(13L),
    /**
     * 通知ID前缀
     */
    NOTICE_ID(14L),

    CIRCLE_ID(15L),

    ;

    private final Long prefix;

}
