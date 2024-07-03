/**
 * -----------------------------------
 * 林风社交论坛开源版本请务必保留此注释头信息
 * 开源地址: https://gitee.com/virus010101/linfeng-community
 * 商业版详情查看: https://www.linfeng.tech
 * 商业版购买联系技术客服		QQ:  3582996245
 * 可正常分享和学习源码，不得转卖或非法牟利！
 * Copyright (c) 2021-2023 linfeng all rights reserved.
 * 版权所有 ，侵权必究！
 * -----------------------------------
 */
package com.same.community.common.exception;




import com.same.community.common.enums.ExceptionTypeEnum;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * 自定义异常
 */
@Data
public class SameException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1601445855844246635L;

    private String msg;
    private int code = ExceptionTypeEnum.SameException.getCode();

    public SameException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public SameException(SameExceptionApi exceptionCode) {
        super(exceptionCode.getMessage());
        this.msg = exceptionCode.getMessage();
        this.code = exceptionCode.getServer()+exceptionCode.getCode();
    }



    public SameException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public SameException(String message, Integer code)
    {
        this.msg = message;
        this.code = code;
    }

    public SameException(String message, HttpStatus code)
    {
        this.msg = message;
        this.code = code.value();
    }


    public SameException(String msg,int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }
    public SameException(int code,String msg) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public SameException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }


}
