package com.same.community.common.meta.exception;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.same.community.common.meta.enums.ExceptionTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 处理自定义业务异常
     */
    @ExceptionHandler(SameException.class)
    public ResponseEntity<Map<String, Object>> handleSameException(SameException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("code", ex.getCode());
        response.put(EXCEPTION_TYPE_KEY, ExceptionTypeEnum.SameException.getMessage());
        log.error("业务异常，错误代码：{},错误信息：{}",ex.getCode(), ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 自定义抛出的异常
     */
    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(GlobalException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("code", ex.getCode());
        response.put(EXCEPTION_TYPE_KEY, ExceptionTypeEnum.GlobalException.getMessage());
        log.error("全局异常，错误代码：{},错误信息：{}",ex.getCode(), ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getCode()));
    }



    @ExceptionHandler({Exception.class,RuntimeException.class,Throwable.class})
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message",ex.getMessage());
        response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put(EXCEPTION_TYPE_KEY, ExceptionTypeEnum.Exception.getMessage());
        log.error("异常，错误代码：{},错误信息：{}",HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }




}
