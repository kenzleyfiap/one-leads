package com.luan.kenzley.one_leads.application.validator.rule.empresa;

import com.luan.kenzley.one_leads.domain.exception.BusinessException;
import com.luan.kenzley.one_leads.domain.exception.EmpresaError;
import com.luan.kenzley.one_leads.infrastructure.repository.EmpresaRepository;
import com.luan.kenzley.one_leads.interfaces.dto.empresa.EmpresaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CnpjDuplicadoRule implements EmpresaValidationRule {

    @Autowired
    private EmpresaRepository empresaRepo;

    @Override
    public void validate(EmpresaDTO dto) {
        if (empresaRepo.findByCnpj(dto.cnpj()).isPresent()) {
            throw new BusinessException(EmpresaError.CNPJ_DUPLICADO);
        }
    }
}