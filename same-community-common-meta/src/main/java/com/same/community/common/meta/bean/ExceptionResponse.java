package com.same.community.common.meta.bean;

import lombok.Data;

/**
 * @author Zixuan.Yang
 * @date 2024/7/14 13:31
 */
@Data
public class ExceptionResponse {
    /**
     * 异常信息
     */
    private String msg;

    private String cause;

    private StackTraceElement stackTrace;

    public ExceptionResponse(Exception e) {
        this.cause = e.toString();
        this.msg = e.getMessage();
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            if (stackTraceElement.getClassName().contains("same")) {
                this.stackTrace = stackTraceElement;
                return;
            }
        }
      /*  this.errorDetail = stackTrace.getClassName() + "." + stackTrace.getMethodName() + "(" +
                stackTrace.getFileName() + ":" + stackTrace.getLineNumber() + ")";*/
    }
}
