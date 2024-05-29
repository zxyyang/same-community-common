package com.same.community.common.constants;

public class RedisKey {

    // 笔记浏览
    public static final String NOTE_VIEW_RECORD = "note:view:record";

    public static final String NOTE_VIEW_COUNT = "note:view:count:";
    public static final String UPDATE_NOTE_VIEW_TASK = "note:view:task";

    public static final String SUBJECT_VIEW_COUNT = "subject:view:count";

    public static final String IM_CHANNEL_LIST = "im:channel:list:";

    public static final String IM_SERVER_WEIGHT ="im:server:weight";

    private final static String CLIENT_TOKEN_PREFIX = "client:token:";

    private final static String CLIENT_USER_PREFIX = "client:user:";

    private final static String IM_TOKEN_PREFIX = "im:token:";

    private final static String IM_CHANNEL_USER_PREFIX = "im:channel:user:";

    public static String clientTokenKey(String uid) {
        return CLIENT_TOKEN_PREFIX + uid;
    }

    public static String clientUserKey(String uid) {
        return CLIENT_USER_PREFIX + uid;
    }

    public static String imTokenKey(String token) {
        return IM_TOKEN_PREFIX + token;
    }

    public static String imChannelUserKey(String uid) {
        return IM_CHANNEL_USER_PREFIX + uid;
    }

}
