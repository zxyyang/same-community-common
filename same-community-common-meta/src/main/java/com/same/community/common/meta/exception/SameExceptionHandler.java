package com.same.community.common.meta.exception;

import com.same.community.common.meta.bean.ResponseBean;
import com.same.community.common.meta.enums.ExceptionTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice(annotations = {RestController.class})
@Slf4j
public class SameExceptionHandler {

    private boolean isFeignClientRequest(HttpServletRequest request) {
        return request.getHeader("X-Feign-Client") != null;
    }

    @ExceptionHandler(SameException.class)
    public ResponseBean handleSameException(SameException ex, HttpServletRequest request, HttpServletResponse response) {
        if (isFeignClientRequest(request)) {
            response.setStatus(900);
        } else {
            response.setStatus(HttpStatus.OK.value());
        }
        log.error("业务异常，错误代码：{}, 错误信息：{}", ex.getCode(), ex.getMessage(), ex);
        return new ResponseBean<>(ex.getCode(), ex.getMsg(), ExceptionTypeEnum.SameException.getMessage());
    }

    @ExceptionHandler(GlobalException.class)
    public ResponseBean handleGlobalException(GlobalException ex, HttpServletResponse response) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        log.error("全局异常，错误代码：{}, 错误信息：{}", ex.getCode(), ex.getMessage(), ex);
        return new ResponseBean<>(ex.getCode(), ex.getMsg(), ExceptionTypeEnum.GlobalException.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseBean handleAllExceptions(Exception ex, HttpServletResponse response) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        log.error("未知异常，错误信息：{}", ex.getMessage(), ex);
        return new ResponseBean<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器内部错误", ExceptionTypeEnum.GlobalException.getMessage());
    }
}

