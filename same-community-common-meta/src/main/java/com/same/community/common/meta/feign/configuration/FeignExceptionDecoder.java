package com.same.community.common.meta.feign.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.same.community.common.meta.enums.ExceptionTypeEnum;
import com.same.community.common.meta.exception.GlobalException;
import com.same.community.common.meta.exception.SameException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Map;

import static com.same.community.common.meta.constants.SameGlobalConst.EXCEPTION_TYPE_KEY;

@Slf4j
@Configuration
public class FeignExceptionDecoder implements ErrorDecoder {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    //添加注入提示
    FeignExceptionDecoder() {
        log.info("异常处理器添加注入成功！");
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            String content = response.body() == null ? null : Util.toString(response.body().asReader(StandardCharsets.UTF_8));
            Map<String, Object> map = objectMapper.readValue(content, Map.class);
            String exceptionType = (String) map.get(EXCEPTION_TYPE_KEY);
            String message = (String) map.get("message");
            int code = (Integer) map.get("code");
            ExceptionTypeEnum exceptionTypeEnumByCode = ExceptionTypeEnum.getExceptionTypeEnumByMessage(exceptionType);
            if (exceptionTypeEnumByCode == null){
                return new RuntimeException("服务内部错误");
            }
            switch (exceptionTypeEnumByCode) {
                case SameException:
                    // 如果HTTP状态码是200，但内容包含SameException，则抛出SameException
                    if (response.status() == 200) {
                        return new SameException(message, code);
                    }
                case GlobalException:
                    return new GlobalException(message, code);
                default:
                    return new RuntimeException("接口"+methodKey+"执行出错，错误代码："+code+"错误信息:"+message);
            }
        } catch (Exception ex) {
            log.error("feign调用-解析异常失败，错误信息：{}", ex.getMessage(), ex);
            return new RuntimeException("服务内部错误");
        }
    }


}
