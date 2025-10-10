package com.luan.kenzley.one_leads.interfaces.dto.empresa;

import org.hibernate.validator.constraints.br.CNPJ;

public record EmpresaUpdateDTO(
        String nome,
        @CNPJ(message = "Cnpj inválido")
        String cnpj
) {}