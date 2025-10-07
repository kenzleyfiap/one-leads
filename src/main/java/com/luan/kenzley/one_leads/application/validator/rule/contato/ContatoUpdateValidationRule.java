package com.luan.kenzley.one_leads.application.validator.rule.contato;

import com.luan.kenzley.one_leads.dto.ContatoDTO;

public interface ContatoUpdateValidationRule {
    void validate(Long id, ContatoDTO dto);
}
