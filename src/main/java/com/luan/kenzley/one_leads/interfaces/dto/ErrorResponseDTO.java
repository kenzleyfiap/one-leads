package com.luan.kenzley.one_leads.interfaces.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Modelo de erro de negócio")
public record ErrorResponseDTO(
        @Schema(description = "Data e hora do erro", example = "2025-10-08T13:30:00")
        LocalDateTime timestamp,
        @Schema(description = "Código HTTP", example = "400")
        int status,
        @Schema(description = "Descrição genérica do erro", example = "Regra de negócio violada")
        String error,
        @Schema(description = "Código interno do erro", example = "FILTRO_OBRIGATORIO")
        String code,
        @Schema(description = "Mensagem detalhada", example = "Informe nome ou e-mail para buscar")
        String message
) {}
