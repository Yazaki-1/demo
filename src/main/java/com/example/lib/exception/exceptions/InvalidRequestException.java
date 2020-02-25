package com.example.lib.exception.exceptions;

import org.springframework.validation.Errors;

/**
 * 用于 - 请求参数 - 表单校验
 * 参数无效请求异常
 */
//@SuppressWarnings("serial")
public class InvalidRequestException extends BaseException {
    private final Errors errors;

    public InvalidRequestException(Errors errors) {
        super("");
        this.errors = errors;
    }

    public Errors getErrors() {
        return errors;
    }
}
