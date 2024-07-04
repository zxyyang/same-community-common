package com.same.community.common.meta.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author Zixuan.Yang
 * @date 2024/7/3 20:03
 */
@Getter
@AllArgsConstructor
public class GlobalException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1601445855874246635L;

    private String msg;
    private int code ;


    public GlobalException(SameGlobalExceptionEnum sameExceptionEnum){
        super(sameExceptionEnum.getMessage());
        this.msg = sameExceptionEnum.getMessage();
        this.code = sameExceptionEnum.getCode();
    }
}
