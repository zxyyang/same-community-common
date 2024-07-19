package com.same.community.common.meta.context;


import com.same.community.common.meta.constants.SameGlobalConst;
import com.same.community.common.meta.exception.GlobalException;
import com.same.community.common.meta.exception.SameGlobalExceptionEnum;
import com.same.community.common.meta.model.SameUserInfo;

public class UserContext {

    private static final ThreadLocal<SameUserInfo> userHolder = new ThreadLocal<>();

    public static void setUser(SameUserInfo user) {
        userHolder.set(user);
    }

    public static SameUserInfo getUser() {
        return userHolder.get();
    }

    public static void clear() {
        userHolder.remove();
    }

    public static boolean isLogin() {
        return userHolder.get() != null;
    }

    public static void checkLogin() {
        if (userHolder.get() == null){
            throw new GlobalException(SameGlobalExceptionEnum.NOT_LOGIN);
        }
    }


}