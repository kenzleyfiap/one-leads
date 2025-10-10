package com.luan.kenzley.one_leads.domain.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

@Schema(description = "Códigos de erro relacionados ao contato")
public enum ContatoError {

    CONTATO_NAO_ENCONTRADO("CONTATO_NAO_ENCONTRADO", "Contato não encontrado", HttpStatus.NOT_FOUND),
    CONTATO_UNICO("CONTATO_UNICO", "E-mail deve ser único", HttpStatus.BAD_REQUEST),
    FILTRO_OBRIGATORIO("FILTRO_OBRIGATORIO", "Informe nome ou e-mail para buscar", HttpStatus.BAD_REQUEST),
    EMAIL_DUPLICADO("EMAIL_DUPLICADO", "E-mail não pode ser duplicado", HttpStatus.CONFLICT),
    EMAIL_OBRIGATORIO("EMAIL_OBRIGATORIO", "E-mail é obrigatório", HttpStatus.BAD_REQUEST),
    NOME_OBRIGATORIO("NOME_OBRIGATORIO", "Nome é obrigatório", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;

    ContatoError(String code, String message, HttpStatus status) {
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