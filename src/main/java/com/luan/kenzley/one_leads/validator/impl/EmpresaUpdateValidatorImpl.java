package com.luan.kenzley.one_leads.validator.impl;

import com.luan.kenzley.one_leads.dto.EmpresaUpdateDTO;
import com.luan.kenzley.one_leads.validator.EmpresaUpdateValidator;
import com.luan.kenzley.one_leads.validator.rule.empresa.EmpresaUpdateValidationRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmpresaUpdateValidatorImpl implements EmpresaUpdateValidator {

    private final List<EmpresaUpdateValidationRule> regras;

    @Autowired
    public EmpresaUpdateValidatorImpl(List<EmpresaUpdateValidationRule> regras) {
        this.regras = regras;
    }

    @Override
    public void validar(Long id, EmpresaUpdateDTO dto) {
        regras.forEach(regra -> regra.validate(id, dto));
    }
}
