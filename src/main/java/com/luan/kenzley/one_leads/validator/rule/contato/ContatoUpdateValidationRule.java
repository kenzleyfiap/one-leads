package com.luan.kenzley.one_leads.validator.rule.contato;

import com.luan.kenzley.one_leads.dto.ContatoDTO;

public interface ContatoUpdateValidationRule {
    void validate(Long id, ContatoDTO dto);
}
