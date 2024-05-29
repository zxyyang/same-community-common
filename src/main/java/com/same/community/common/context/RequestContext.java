package com.same.community.common.context;

public class RequestContext {

    private static final ThreadLocal<String> remoteIpHolder = new ThreadLocal<>();

    public static void setRemoteIp(String remoteIp) {
        remoteIpHolder.set(remoteIp);
    }

    public static String getRemoteIp() {
        return remoteIpHolder.get();
    }

    public static void clear() {
        remoteIpHolder.remove();
    }


}
