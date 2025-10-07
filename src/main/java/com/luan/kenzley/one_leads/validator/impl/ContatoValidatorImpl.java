package com.luan.kenzley.one_leads.validator.impl;

import com.luan.kenzley.one_leads.dto.ContatoDTO;
import com.luan.kenzley.one_leads.validator.ContatoValidator;
import com.luan.kenzley.one_leads.validator.rule.contato.ContatoValidationRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContatoValidatorImpl implements ContatoValidator {

    private final List<ContatoValidationRule> regras;

    @Autowired
    public ContatoValidatorImpl(List<ContatoValidationRule> regras) {
        this.regras = regras;
    }

    @Override
    public void validar(ContatoDTO dto) {
        regras.forEach(regra -> regra.validate(dto));
    }
}
