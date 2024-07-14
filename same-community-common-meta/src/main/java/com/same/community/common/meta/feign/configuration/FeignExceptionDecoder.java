package com.same.community.common.meta.feign.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.same.community.common.meta.bean.ResponseBean;
import com.same.community.common.meta.enums.ExceptionTypeEnum;
import com.same.community.common.meta.exception.GlobalException;
import com.same.community.common.meta.exception.SameException;
import feign.RequestInterceptor;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
public class FeignExceptionDecoder implements ErrorDecoder {

    public FeignExceptionDecoder() {
        log.info("异常处理器添加注入成功！");
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> requestTemplate.header("X-Feign-Client", "true");
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            if (response.body() == null) {
                return new RuntimeException("服务内部错误");
            }

            String content = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
            ObjectMapper objectMapper = new ObjectMapper();
            ResponseBean responseBean = objectMapper.readValue(content, ResponseBean.class);
            String exceptionType = responseBean.getExceptionType();
            ExceptionTypeEnum exceptionTypeEnumByCode = ExceptionTypeEnum.getExceptionTypeEnumByMessage(exceptionType);

            if (exceptionTypeEnumByCode == null) {
                return new RuntimeException("服务内部错误");
            }

            switch (exceptionTypeEnumByCode) {
                case SameException:
                    return new SameException(responseBean.getMsg(), responseBean.getCode());
                case GlobalException:
                    return new GlobalException(responseBean.getMsg(), responseBean.getCode());
                default:
                    return new RuntimeException("接口" + methodKey + "执行出错，错误代码：" + responseBean.getCode() + "错误信息:" + responseBean.getMsg());
            }
        } catch (IOException ex) {
            log.error("feign调用-解析异常失败，错误信息：{}", ex.getMessage(), ex);
            return new RuntimeException("服务内部错误");
        }
    }
}
