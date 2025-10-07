package com.luan.kenzley.one_leads.dto;

import java.util.List;

public record EmpresaDTO(String nome, String cnpj, List<Long> contatosIds) {}
