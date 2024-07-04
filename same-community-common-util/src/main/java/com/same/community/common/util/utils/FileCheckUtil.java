package com.same.community.common.util.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class FileCheckUtil {

    @Value("${oss.image.maxSize:10}")
    private Long imageMaxSize;

    @Value("${oss.image.maxSize:100}")
    private Long videoMaxSize;

    @Value("#{'${oss.image.suffix:jpg,png,gif,jpeg,bmp}'.split(',')}")
    private List<String> imageSuffix;

    @Value("#{'${oss.video.suffix:mp4,avi,flv,rmvb,wmv}'.split(',')}")
    private List<String> videoSuffix;

    @Value("${oss.audio.maxSize:50}")
    private Long audioMaxSize;

    @Value("${oss.document.maxSize:20}")
    private Long documentMaxSize;


    /**
     * 检查传入的MIME类型是否符合要求，并且文件大小是否适当。
     * @param contentType 文件的MIME类型
     * @param size 文件大小（单位：字节）
     */
    public void check(String contentType, long size) {
        int len = 1024 * 1024; // 将MB转换为字节

        // 提取MIME类型后缀（即 general/specific 中的 specific）
        String type = contentType.contains("/") ? contentType.substring(contentType.indexOf('/') + 1) : contentType;

        if (imageSuffix.stream().anyMatch(suffix -> suffix.equalsIgnoreCase(type))) {
            if (size > (imageMaxSize * len)) {
                throw new RuntimeException("上传图片文件超出规定大小");
            }
        } else if (videoSuffix.stream().anyMatch(suffix -> suffix.equalsIgnoreCase(type))) {
            if (size > (videoMaxSize * len)) {
                throw new RuntimeException("上传视频文件超出规定大小");
            }
        } else {
            throw new RuntimeException("不支持的文件类型：" + contentType);
        }
    }

    /**
     * 检查传入的MIME类型是否符合允许的图片类型，并验证其大小。
     * @param contentType 文件的MIME类型
     * @param size 文件大小（字节单位）
     */
    public void checkImage(String contentType, long size) {
        int len = 1024 * 1024; // 将MB转换为字节
        String type = contentType.substring(contentType.indexOf('/') + 1);

        if (imageSuffix.contains(type)) {
            if (size > (imageMaxSize * len)) {
                throw new RuntimeException("上传图片文件超出规定大小");
            }
        } else {
            throw new RuntimeException("不支持的图片文件类型：" + contentType);
        }
    }

    /**
     * 检查传入的MIME类型是否符合允许的视频类型，并验证其大小。
     * @param contentType 文件的MIME类型
     * @param size 文件大小（字节单位）
     */
    public void checkVideo(String contentType, long size) {
        int len = 1024 * 1024; // 将MB转换为字节
        String type = contentType.substring(contentType.indexOf('/') + 1);

        if (videoSuffix.contains(type)) {
            if (size > (videoMaxSize * len)) {
                throw new RuntimeException("上传视频文件超出规定大小");
            }
        } else {
            throw new RuntimeException("不支持的视频文件类型：" + contentType);
        }
    }

    /**
     * 检查文件的MIME类型是否被允许，并且文件大小是否符合要求。
     * @param contentType 文件的MIME类型
     * @param size 文件大小（单位：字节）
     */
    public void checkAll(String contentType, long size) {
        int len = 1024 * 1024; // 将MB转换为字节计算

        if (contentType.startsWith("image/")) {
            validateSize(size, imageMaxSize * len, "图片");
        } else if (contentType.startsWith("video/")) {
            validateSize(size, videoMaxSize * len, "视频");
        } else if (contentType.startsWith("audio/")) {
            validateSize(size, audioMaxSize * len, "音频");
        } else if (isDocumentContentType(contentType)) {
            validateSize(size, documentMaxSize * len, "文档");
        } else {
            throw new RuntimeException("不支持的文件类型：" + contentType);
        }
    }

    private boolean isDocumentContentType(String contentType) {
        return contentType.equals("application/pdf") ||
                contentType.startsWith("application/msword") ||
                contentType.startsWith("application/vnd.openxmlformats-officedocument.wordprocessingml.document") ||
                contentType.startsWith("application/vnd.ms-excel") ||
                contentType.startsWith("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") ||
                contentType.startsWith("application/vnd.ms-powerpoint") ||
                contentType.startsWith("application/vnd.openxmlformats-officedocument.presentationml.presentation");
    }

    private void validateSize(long fileSize, long maxSize, String fileType) {
        if (fileSize > maxSize) {
            throw new RuntimeException("上传" + fileType + "文件超出规定大小");
        }
    }
}
