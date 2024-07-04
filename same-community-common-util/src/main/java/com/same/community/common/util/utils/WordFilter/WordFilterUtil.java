package com.same.community.common.util.utils.WordFilter;

/**
 * @author Zixuan.Yang
 * @date 2024/4/17 19:08
 */
public class WordFilterUtil {
    private static final WordContext context = new WordContext();
    private static final WordFilter filter = new WordFilter(context);


    public static String replace(String text) {
        return filter.replace(text);
    }

    /**
     * 测试是否包含敏感词
     */
    public static boolean include(String text) {
        return filter.include(text);
    }

    public static void main(String[] args) {
        System.err.println(replace("QQ"));

    }
}
