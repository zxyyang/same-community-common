package com.same.community.common.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Zixuan.Yang
 * @date 2023/12/8 00:09
 */
@Data
public class SameUserInfo implements Serializable {

    private static final long serialVersionUID = 8633532439444849456L;

    /**
     * 用户id
     */
    private Long uid;



}
