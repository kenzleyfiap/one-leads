package com.luan.kenzley.one_leads.application.validator.rule.empresa;

import com.luan.kenzley.one_leads.dto.EmpresaDTO;
import com.luan.kenzley.one_leads.domain.exception.BusinessException;
import org.springframework.stereotype.Component;

@Component
public class EmpresaSemContatosRule implements EmpresaValidationRule {

    @Override
    public void validate(EmpresaDTO dto) {
        if (dto.contatosIds() == null || dto.contatosIds().isEmpty()) {
            throw new BusinessException("Empresa precisa de ao menos um contato vinculado");
        }
    }
}