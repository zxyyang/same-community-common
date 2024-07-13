package com.same.community.common.meta.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Zixuan.Yang
 * @date 2024/3/10 19:57
 */
@Data
public class ResponseBean<T> implements Serializable {

    private static final long serialVersionUID = -2482451601325334949L;

    private Integer code;

    private String msg;

    private T data;

    public ResponseBean(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseBean(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 状态码 + 成功提示信息
     */
    public static <T> ResponseBean<T> Success() {
        return new ResponseBean<>(200, "成功");
    }

    /**
     * 状态码 + 成功提示信息 + 数据
     */
    public static <T> ResponseBean<T> Success(T data) {
        return new ResponseBean<>(200, "成功", data);
    }


    /**
     * 状态码 + 错误信息
     */
    public static <T> ResponseBean<T> Error() {
        return new ResponseBean<>(500, "失败");
    }

    /**
     * 状态码 + 错误信息(自定义)
     */
    public static <T> ResponseBean<T> Error(String msg) {
        return new ResponseBean<>(500, msg);
    }


    /**
     * 状态码（自定义） + 错误信息(自定义)
     */
    public static <T> ResponseBean<T> Error(Integer code, String msg) {
        return new ResponseBean<>(code, msg);
    }

}
