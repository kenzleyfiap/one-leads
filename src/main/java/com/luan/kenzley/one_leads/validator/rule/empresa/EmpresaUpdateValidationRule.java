package com.luan.kenzley.one_leads.validator.rule.empresa;

import com.luan.kenzley.one_leads.dto.EmpresaUpdateDTO;

public interface EmpresaUpdateValidationRule {
    void validate(Long id, EmpresaUpdateDTO dto);
}

