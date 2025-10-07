package com.luan.kenzley.one_leads.validator.impl;

import com.luan.kenzley.one_leads.dto.EmpresaDTO;
import com.luan.kenzley.one_leads.validator.EmpresaValidator;
import com.luan.kenzley.one_leads.validator.rule.empresa.EmpresaValidationRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmpresaValidatorImpl implements EmpresaValidator {

    private final List<EmpresaValidationRule> regras;

    @Autowired
    public EmpresaValidatorImpl(List<EmpresaValidationRule> regras) {
        this.regras = regras;
    }

    @Override
    public void validar(EmpresaDTO dto) {
        regras.forEach(regra -> regra.validate(dto));
    }

}