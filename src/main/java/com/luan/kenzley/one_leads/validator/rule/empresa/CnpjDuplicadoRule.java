package com.luan.kenzley.one_leads.validator.rule.empresa;

import com.luan.kenzley.one_leads.dto.EmpresaDTO;
import com.luan.kenzley.one_leads.exception.BusinessException;
import com.luan.kenzley.one_leads.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CnpjDuplicadoRule implements EmpresaValidationRule {

    @Autowired
    private EmpresaRepository empresaRepo;

    @Override
    public void validate(EmpresaDTO dto) {
        if (empresaRepo.findByCnpj(dto.cnpj()).isPresent()) {
            throw new BusinessException("CNPJ DUPLICADO: " + dto.cnpj());
        }
    }
}