package com.same.community.common.util.utils.WordFilter;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

/**
 * 词库上下文环境
 * <p>
 * 初始化敏感词库，将敏感词加入到HashMap中，构建DFA算法模型
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class WordContext {

    /**
     * 敏感词字典
     * -- GETTER --
     *  获取初始化的敏感词列表
     *
     * @return 敏感词列表

     */
    @Getter
    private final Map wordMap = new HashMap(1024);

    /**
     * 是否已初始化
     */
    private boolean init;
    /**
     * 黑名单列表
     */
    private final List<String> blackList;
    /**
     * 白名单列表
     */
    private final String whiteList;

    public WordContext() {
        List<String> blackListFile = new ArrayList<>();
        blackListFile.add("/word-filter/advertising.txt");
        blackListFile.add("/word-filter/politics.txt");
        blackListFile.add("/word-filter/gun-and-explosion-related-illegal.txt");
        blackListFile.add("/word-filter/web-url.txt");
        blackListFile.add("/word-filter/erotica.txt");

        this.blackList = blackListFile;
        this.whiteList = "/word-filter/whitelist.txt";
        initKeyWord();
    }

    public WordContext(List<String> blackList, String whiteList) {
        this.blackList = blackList;
        this.whiteList = whiteList;
        initKeyWord();
    }

    /**
     * 初始化
     */
    private synchronized void initKeyWord() {
        try {
            if (!init) {
                // 将敏感词库加入到HashMap中
                for (String string : blackList) {
                    addWord(readWordFile(string), WordType.BLACK);
                }
                // 将非敏感词库也加入到HashMap中
                addWord(readWordFile(whiteList), WordType.WHITE);
            }
            init = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型：<br>
     * 中 = { isEnd = 0 国 = {<br>
     * isEnd = 1 人 = {isEnd = 0 民 = {isEnd = 1} } 男 = { isEnd = 0 人 = { isEnd = 1 }
     * } } } 五 = { isEnd = 0 星 = { isEnd = 0 红 = { isEnd = 0 旗 = { isEnd = 1 } } } }
     */
    public void addWord(Iterable<String> wordList, WordType wordType) {
        Map nowMap;
        Map<String, String> newWorMap;
        // 迭代keyWordSet
        for (String key : wordList) {
            nowMap = wordMap;
            for (int i = 0; i < key.length(); i++) {
                // 转换成char型
                char keyChar = key.charAt(i);
                // 获取
                Object wordMap = nowMap.get(keyChar);
                // 如果存在该key，直接赋值
                if (wordMap != null) {
                    nowMap = (Map) wordMap;
                } else {
                    // 不存在则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                    newWorMap = new HashMap<>(4);
                    // 不是最后一个
                    newWorMap.put("isEnd", String.valueOf(EndType.HAS_NEXT.ordinal()));
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }

                if (i == key.length() - 1) {
                    // 最后一个
                    nowMap.put("isEnd", String.valueOf(EndType.IS_END.ordinal()));
                    nowMap.put("isWhiteWord", String.valueOf(wordType.ordinal()));
                }
            }
        }
    }

    /**
     * 在线删除敏感词
     *
     * @param wordList 敏感词列表
     * @param wordType 黑名单 BLACk，白名单WHITE
     */
    public void removeWord(Iterable<String> wordList, WordType wordType) {
        Map nowMap;
        for (String key : wordList) {
            List<Map> cacheList = new ArrayList<>();
            nowMap = wordMap;
            for (int i = 0; i < key.length(); i++) {
                char keyChar = key.charAt(i);

                Object map = nowMap.get(keyChar);
                if (map != null) {
                    nowMap = (Map) map;
                    cacheList.add(nowMap);
                } else {
                    return;
                }

                if (i == key.length() - 1) {
                    char[] keys = key.toCharArray();
                    boolean cleanable = false;
                    char lastChar = 0;
                    for (int j = cacheList.size() - 1; j >= 0; j--) {
                        Map cacheMap = cacheList.get(j);
                        if (j == cacheList.size() - 1) {
                            if (String.valueOf(WordType.BLACK.ordinal()).equals(cacheMap.get("isWhiteWord"))) {
                                if (wordType == WordType.WHITE) {
                                    return;
                                }
                            }
                            if (String.valueOf(WordType.WHITE.ordinal()).equals(cacheMap.get("isWhiteWord"))) {
                                if (wordType == WordType.BLACK) {
                                    return;
                                }
                            }
                            cacheMap.remove("isWhiteWord");
                            cacheMap.remove("isEnd");
                            if (cacheMap.size() == 0) {
                                cleanable = true;
                                continue;
                            }
                        }
                        if (cleanable) {
                            Object isEnd = cacheMap.get("isEnd");
                            if (String.valueOf(EndType.IS_END.ordinal()).equals(isEnd)) {
                                cleanable = false;
                            }
                            cacheMap.remove(lastChar);
                        }
                        lastChar = keys[j];
                    }

                    if (cleanable) {
                        wordMap.remove(lastChar);
                    }
                }
            }
        }
    }

    /**
     * 读取敏感词库中的内容，将内容添加到set集合中
     */
    private Set<String> readWordFile(String file) throws Exception {
        Set<String> set;
        // 字符编码
        String encoding = "UTF-8";
        URL resourceUrl = this.getClass().getResource(file);
        if (resourceUrl == null) {
            throw new RuntimeException("无法找到资源文件：" + file);
        }
        InputStream resourceAsStream = this.getClass().getResourceAsStream(file);
        try (InputStreamReader read = new InputStreamReader(
                Objects.requireNonNull(resourceAsStream), encoding)) {
            set = new HashSet<>();
            BufferedReader bufferedReader = new BufferedReader(read);
            String txt;
            // 读取文件，将文件内容放入到set中
            while ((txt = bufferedReader.readLine()) != null) {
                set.add(txt);
            }
        }
        // 关闭文件流
        return set;
    }
}
