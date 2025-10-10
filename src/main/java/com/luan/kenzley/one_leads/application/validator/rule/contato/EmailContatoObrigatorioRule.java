package com.luan.kenzley.one_leads.application.validator.rule.contato;

import com.luan.kenzley.one_leads.domain.exception.BusinessException;
import com.luan.kenzley.one_leads.domain.exception.ContatoError;
import com.luan.kenzley.one_leads.interfaces.dto.contato.ContatoDTO;
import org.springframework.stereotype.Component;

@Component
public class EmailContatoObrigatorioRule implements ContatoValidationRule {
    @Override
    public void validate(ContatoDTO dto) {
        if (dto.email() == null || dto.email().trim().isEmpty()) {
            throw new BusinessException(ContatoError.EMAIL_OBRIGATORIO);
        }
    }
}

