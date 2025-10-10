package com.luan.kenzley.one_leads.application.validator.impl;

import com.luan.kenzley.one_leads.application.validator.ContatoUpdateValidator;
import com.luan.kenzley.one_leads.application.validator.rule.contato.ContatoUpdateValidationRule;
import com.luan.kenzley.one_leads.interfaces.dto.contato.ContatoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContatoUpdateValidatorImpl implements ContatoUpdateValidator {

    private final List<ContatoUpdateValidationRule> regras;

    @Autowired
    public ContatoUpdateValidatorImpl(List<ContatoUpdateValidationRule> regras) {
        this.regras = regras;
    }

    public void validar(Long id, ContatoDTO dto) {
        regras.forEach(regra -> regra.validate(id, dto));
    }

}
