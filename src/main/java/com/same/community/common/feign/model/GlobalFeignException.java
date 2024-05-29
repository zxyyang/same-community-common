package com.same.community.common.feign.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalFeignException extends RuntimeException {
    
    private static final long serialVersionUID = -4568012221472522014L;
    
    private Integer httpStatus = 500;

    private String method;

    private Integer errorCode;

    private String errorMsg;

    public GlobalFeignException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

}