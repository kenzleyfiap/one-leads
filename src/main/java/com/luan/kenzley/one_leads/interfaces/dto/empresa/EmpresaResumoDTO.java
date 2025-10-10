package com.luan.kenzley.one_leads.interfaces.dto.empresa;

import com.luan.kenzley.one_leads.interfaces.dto.contato.ContatoResponseDTO;

public record EmpresaResumoDTO(
        Long id,
        String nome,
        String cnpj,
        ContatoResponseDTO primeiroContato
) {}