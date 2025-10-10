package com.luan.kenzley.one_leads.interfaces.dto.empresa;

import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.br.CNPJ;

import java.util.List;

public record EmpresaDTO(
        @Schema(description = "Nome da empresa", example = "Empresa LTDA")
        String nome,

        @Schema(description = "Cnpj da empresa", example = "20496329000100")
        @CNPJ(message = "CNPJ inválido")
        String cnpj,
        @Schema(description = "IDs dos contatos vinculados à empresa", example = "[1, 2, 3]")
        List<Long> contatosIds
) {}
