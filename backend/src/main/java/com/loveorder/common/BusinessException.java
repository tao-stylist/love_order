package com.loveorder.common;

import lombok.Getter;

/**
 * 业务异常类
 * 用于在 Service 层抛出业务相关的异常，由全局异常处理器统一捕获处理
 */
@Getter
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(String message) {
        super(message);
        this.code = ResultCode.ERROR.getCode();
    }

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
}
