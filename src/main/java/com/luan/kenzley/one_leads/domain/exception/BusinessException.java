package com.luan.kenzley.one_leads.domain.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {

    private final String code;
    private final HttpStatus status;

    public BusinessException(String code, String message, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
    }

    public BusinessException(ContatoError error) {
        this(error.getCode(), error.getMessage(), error.getStatus());
    }

    public BusinessException(EmpresaError error) {
        this(error.getCode(), error.getMessage(), error.getStatus());
    }

    public String getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }
}