package com.luan.kenzley.one_leads.domain.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

@Schema(description = "Códigos de erro relacionados a empresa")
public enum EmpresaError {

    EMPRESA_NAO_ENCONTRADA("EMPRESA_NAO_ENCONTRADA", "Empresa não encontrada", HttpStatus.NOT_FOUND),
    CNPJ_DUPLICADO("CNPJ_DUPLICADO", "Cnpj duplicado", HttpStatus.CONFLICT),
    CNPJ_INVALIDO("CNPJ_INVALIDO", "Cnpj invalido", HttpStatus.BAD_REQUEST),
    FILTRO_OBRIGATORIO("FILTRO_OBRIGATORIO", "Informe nome ou CNPJ para buscar", HttpStatus.BAD_REQUEST),
    EMPRESA_SEM_CONTATO_VINCULADO("EMPRESA_SEM_CONTATO_VINCULADO", "Empresa precisa de ao menos um contato vinculado", HttpStatus.BAD_REQUEST),
    CONTATO_JA_VINCULADO("CONTATO_JA_VINCULADO", "Contato já vinculado" , HttpStatus.CONFLICT),
    CONTATO_UNICO_REMOVER("CONTATO_UNICO_REMOVER", "Empresa com único contato vinculado, não é possível remover" , HttpStatus.CONFLICT),;

    private final String code;
    private final String message;
    private final HttpStatus status;

    EmpresaError(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}