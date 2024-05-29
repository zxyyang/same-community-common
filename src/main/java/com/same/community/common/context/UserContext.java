package com.same.community.common.context;


import com.same.community.common.model.SameUser;

public class UserContext {

    private static final ThreadLocal<SameUser> userHolder = new ThreadLocal<>();

    public static void setUser(SameUser user) {
        userHolder.set(user);
    }

    public static SameUser getUser() {
        return userHolder.get();
    }

    public static void clear() {
        userHolder.remove();
    }

}