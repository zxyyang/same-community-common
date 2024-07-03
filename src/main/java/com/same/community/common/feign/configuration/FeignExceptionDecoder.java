package com.same.community.common.feign.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.same.community.common.enums.ExceptionTypeEnum;
import com.same.community.common.exception.GlobalException;
import com.same.community.common.exception.SameException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.same.community.common.constants.SameGlobalConst.EXCEPTION_TYPE_KEY;

@Slf4j
@Configuration
public class FeignExceptionDecoder extends ErrorDecoder.Default {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            String content = response.body() == null ? null : Util.toString(response.body().asReader(StandardCharsets.UTF_8));
            Map<String, Object> map = objectMapper.readValue(content, Map.class);
            Integer exceptionType = (Integer) map.get(EXCEPTION_TYPE_KEY);
            String message = (String) map.get("message");
            int code = (Integer) map.get("code");
            ExceptionTypeEnum exceptionTypeEnumByCode = ExceptionTypeEnum.getExceptionTypeEnumByCode(exceptionType);
            if (exceptionTypeEnumByCode == null){
                return new RuntimeException("服务内部错误");
            }
            switch (exceptionTypeEnumByCode) {
                case SameException:
                    return new SameException(message, code);
                case GlobalException:
                    return new GlobalException(message, code);
                default:
                    return super.decode(methodKey, response);
            }

        } catch (Exception ex) {
            log.error("feign调用-解析异常失败，错误信息：{}", ex.getMessage(), ex);
            return new RuntimeException("服务内部错误");
        }
    }
}
