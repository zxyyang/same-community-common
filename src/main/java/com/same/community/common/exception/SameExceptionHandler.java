package com.same.community.common.exception;

import com.same.community.common.bean.ResponseBean;
import lombok.extern.slf4j.Slf4j;
import com.same.community.common.feign.model.GlobalFeignException;
import com.same.community.common.constants.AppConst;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Arrays;
import java.util.List;

/**
 * 异常处理器
 * @author Zixuan.Yang
 * @date 2024/7/3 11:59
 */
@RestControllerAdvice
@Slf4j
public class SameExceptionHandler {

    private final static List<Integer> THROW_STATUS = Arrays.asList(HttpStatus.UNAUTHORIZED.value(), HttpStatus.FORBIDDEN.value(),HttpStatus.BAD_REQUEST.value(), HttpStatus.NOT_FOUND.value());

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(SameException.class)
    public ResponseEntity<ResponseBean<String>> handleException(SameException e) {
        if (THROW_STATUS.contains(e.getCode())) {
            throw e;
        }
        return ResponseEntity.ok(ResponseBean.Error(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(GlobalFeignException.class)
    public ResponseBean<String> handleException(GlobalFeignException e) {
        return AppConst.GLOBAL_EXCEPTION_STATUS == e.getHttpStatus()
                ? ResponseBean.Error(e.getErrorCode(), e.getMessage())
                : ResponseBean.Error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseBean<String> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseBean.Error(e.getMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseBean<String> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error(e.getMessage(), e);
        return ResponseBean.Error("文件大小超出限制");
    }
}
