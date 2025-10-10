package com.luan.kenzley.one_leads.application.validator.rule.empresa;


import com.luan.kenzley.one_leads.interfaces.dto.empresa.EmpresaUpdateDTO;

public interface EmpresaUpdateValidationRule {
    void validate(Long id, EmpresaUpdateDTO dto);
}

