package com.luan.kenzley.one_leads.application.validator.rule.empresa;

import com.luan.kenzley.one_leads.domain.exception.BusinessException;
import com.luan.kenzley.one_leads.domain.exception.EmpresaError;
import com.luan.kenzley.one_leads.interfaces.dto.empresa.EmpresaDTO;
import org.springframework.stereotype.Component;

@Component
public class EmpresaSemContatosRule implements EmpresaValidationRule {

    @Override
    public void validate(EmpresaDTO dto) {
        if (dto.contatosIds() == null || dto.contatosIds().isEmpty()) {
            throw new BusinessException(EmpresaError.EMPRESA_SEM_CONTATO_VINCULADO);
        }
    }
}