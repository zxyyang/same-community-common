package com.same.community.common.meta.exception;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.same.community.common.meta.bean.ResponseBean;
import com.same.community.common.meta.enums.ExceptionTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static com.same.community.common.meta.constants.SameGlobalConst.EXCEPTION_TYPE_KEY;

/**
 * 异常处理器
 * @author Zixuan.Yang
 * @date 2024/7/3 11:59
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@Slf4j
public class SameExceptionHandler {

    private boolean isFeignClientRequest(HttpServletRequest request) {
        return request.getHeader("X-Feign-Client") != null;
    }

    /**
     * 处理自定义业务异常
     */
    @ExceptionHandler(SameException.class)
    public ResponseBean handleSameException(SameException ex,HttpServletRequest request, HttpServletResponse response) {
        if (isFeignClientRequest(request)){
            response.setStatus(900);
        }else {
            response.setStatus(HttpStatus.OK.value());
        }
        log.error("业务异常，错误代码：{},错误信息：{}",ex.getCode(), ex.getMessage(), ex);
        return  new ResponseBean<>(ex.getCode(),ex.getMsg(),ExceptionTypeEnum.SameException.getMessage());
    }

    /**
     * 自定义抛出的异常
     */
    @ExceptionHandler(GlobalException.class)
    public ResponseBean handleGlobalException(GlobalException ex,HttpServletResponse response) {
        response.setStatus(ex.getCode());
        log.error("全局异常，错误代码：{},错误信息：{}",ex.getCode(), ex.getMessage(), ex);
        return  new ResponseBean<>(ex.getCode(),ex.getMsg(),ExceptionTypeEnum.GlobalException.getMessage());
    }




}
