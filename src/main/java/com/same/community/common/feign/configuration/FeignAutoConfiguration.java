package com.same.community.common.feign.configuration;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Zixuan.Yang
 * @date 2024/7/3 20:50
 */
@Configuration
public class FeignAutoConfiguration {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignExceptionDecoder();
    }
}
