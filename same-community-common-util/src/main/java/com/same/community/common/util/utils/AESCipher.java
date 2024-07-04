package com.same.community.common.util.utils;

import cn.hutool.core.exceptions.ExceptionUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

/**
 * @author Zixuan.Yang
 * @date 2023/12/8 16:44
 */

@Component
@Slf4j
public class AESCipher {

    private static final String ALGORITHM = "AES";


    private static String KEY;

    @Value("${same.aes.key:}")
    public void setKEY(String key) {
        AESCipher.KEY = key;
    }

    @SneakyThrows
    public static String encrypt(String value) {
        if (Objects.isNull(value)){
            return null;
        }
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            byte[] encryptedValue = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedValue);
        } catch (Exception e) {
            log.error("加密失败：{}", ExceptionUtil.stacktraceToString(e));
            throw new Exception("加密失败");
        }


    }

    @SneakyThrows
    public static String decrypt(String encryptedValue) {
        if (Objects.isNull(encryptedValue)){
            return null;
        }
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

            byte[] originalValue = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));
            return new String(originalValue, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("解密失败：{}", ExceptionUtil.stacktraceToString(e));
            throw new Exception("解密失败");
        }

    }
}