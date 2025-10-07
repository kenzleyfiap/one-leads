package com.luan.kenzley.one_leads.validator.rule.contato;

import com.luan.kenzley.one_leads.dto.ContatoDTO;
import com.luan.kenzley.one_leads.exception.BusinessException;
import org.springframework.stereotype.Component;

@Component
public class NomeContatoObrigatorioRule implements ContatoValidationRule {
    @Override
    public void validate(ContatoDTO dto) {
        if (dto.nome() == null || dto.nome().trim().isEmpty()) {
            throw new BusinessException("Nome obrigatório!");
        }
    }
}
