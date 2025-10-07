package com.luan.kenzley.one_leads.application.validator;

import com.luan.kenzley.one_leads.dto.ContatoDTO;

public interface ContatoUpdateValidator {
    void validar(Long id, ContatoDTO dto);
}
