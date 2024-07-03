package com.same.community.common.exception;

import com.same.community.common.enums.ExceptionTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static com.same.community.common.constants.SameGlobalConst.EXCEPTION_TYPE_KEY;

/**
 * 异常处理器
 * @author Zixuan.Yang
 * @date 2024/7/3 11:59
 */
@RestControllerAdvice
@Slf4j
public class SameExceptionHandler {

    /**
     * 处理自定义业务异常
     */
    @ExceptionHandler(SameException.class)
    public ResponseEntity<Map<String, Object>> handleException(SameException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("code", ex.getCode());
        response.put(EXCEPTION_TYPE_KEY, ExceptionTypeEnum.SameException.getCode());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 自定义抛出的异常
     */
    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<Map<String, Object>> handleException(GlobalException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("code", ex.getCode());
        response.put(EXCEPTION_TYPE_KEY, ExceptionTypeEnum.GlobalException.getCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getCode()));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put(EXCEPTION_TYPE_KEY, ExceptionTypeEnum.GlobalException.getCode());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
