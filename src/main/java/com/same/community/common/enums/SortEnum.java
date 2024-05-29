package com.same.community.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zixuan.Yang
 * @date 2024/1/12 21:59
 */
@Getter
@AllArgsConstructor
public enum SortEnum {
    /**
     * 倒序
     */
    DESC(0, "desc"),

    /**
     * 正序
     */
    ASC(1, "asc"),
    ;


    private Integer code;
    private String sort;

    public static String getSortByCode(Integer code) {
        if (code == null) {
            return DESC.getSort();
        }
        for (SortEnum sortEnum : SortEnum.values()) {
            if (sortEnum.getCode().equals(code)) {
                return sortEnum.getSort();
            }
        }
        return DESC.getSort();
    }
}
