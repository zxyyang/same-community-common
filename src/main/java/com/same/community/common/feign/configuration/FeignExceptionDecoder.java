package com.same.community.common.feign.configuration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.same.community.common.constants.AppConst;
import com.same.community.common.exception.SameException;
import com.same.community.common.feign.model.GlobalFeignException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
public class FeignExceptionDecoder extends ErrorDecoder.Default {

    public static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {

        try {
            Response.Body body = response.body();

            // 自定义的异常
            int httpStatus = response.status();
            String content = body == null ? null : Util.toString(body.asReader(StandardCharsets.UTF_8));
            if (httpStatus == AppConst.GLOBAL_EXCEPTION_STATUS) {
                SameException sameException = objectMapper.readValue(content, SameException.class);
                GlobalFeignException feignException = new GlobalFeignException(sameException.getMsg());
                feignException.setHttpStatus(AppConst.GLOBAL_EXCEPTION_STATUS);
                feignException.setMethod(methodKey);
                feignException.setErrorCode(sameException.getCode());
                feignException.setStackTrace(sameException.getStackTrace());
                log.warn("feign调用-业务异常，方法:{}，异常信息：{}", methodKey, feignException.getErrorMsg(), feignException);
                return feignException;
            }

            String errorMsg;
            if (content == null) {
                errorMsg = response.reason();
            } else {
                try {
                    JSONObject jsonObject = JSON.parseObject(content);
                    errorMsg = jsonObject.getString("error");
                } catch (Exception e) {
                    errorMsg = content;
                }
            }

            GlobalFeignException feignException = new GlobalFeignException("服务内部错误");
            feignException.setHttpStatus(httpStatus);
            feignException.setMethod(methodKey);
            log.error("feign调用-服务异常，方法:{}，异常信息：{}", methodKey, errorMsg);

            return feignException;

        } catch (Exception ex) {
            log.error("feign调用-解析异常失败，错误信息：{}", ex.getMessage(), ex);
            GlobalFeignException feignException = new GlobalFeignException("服务内部错误");
            feignException.setHttpStatus(500);
            feignException.setMethod(methodKey);
            return feignException;
        }

    }

}
