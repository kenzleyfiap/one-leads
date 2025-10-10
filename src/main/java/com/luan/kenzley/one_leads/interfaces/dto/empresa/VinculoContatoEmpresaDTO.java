package com.luan.kenzley.one_leads.interfaces.dto.empresa;

import io.swagger.v3.oas.annotations.media.Schema;

public record VinculoContatoEmpresaDTO(
        @Schema(description = "ID da empresa", example = "1")
        Long empresaId,
        @Schema(description = "ID do contato", example = "2")
        Long contatoId
) {}
