package com.luan.kenzley.one_leads.interfaces.dto.contato;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ContatoDTO(
        @NotBlank
        @Schema(description = "Nome completo do contato", example = "Maria Silva")
        String nome,
        @Email
        @Schema(description = "E-mail do contato", example = "maria.silva@email.com")
        String email,
        @Schema(description = "Telefone do contato", example = "+55 61 91234-5678")
        String telefone,
        @Schema(description = "Observações adicionais sobre o contato", example = "Cliente interessado em planos premium")
        String observacoes) {}
