package com.same.community.common.util.utils;


import com.same.community.common.meta.context.UserContext;
import com.same.community.common.meta.exception.GlobalException;
import com.same.community.common.meta.exception.SameException;
import com.same.community.common.meta.exception.SameGlobalExceptionEnum;

/**
 * @author Zixuan.Yang
 * @date 2023/12/12 16:21
 */
public class UserAuthUtils {
    /**
     * 检查用户和token是否匹配，拥有
     *
     * @param uid
     */

    public static void checkUser(Long uid) {
        if (!UserContext.getUser().getUid().equals(uid)) {
            throw new GlobalException(SameGlobalExceptionEnum.NO_PERMISSION);
        }
    }

}
