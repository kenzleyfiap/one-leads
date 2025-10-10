package com.luan.kenzley.one_leads.application.validator.rule.empresa;

import com.luan.kenzley.one_leads.domain.exception.EmpresaError;
import com.luan.kenzley.one_leads.domain.model.Empresa;
import com.luan.kenzley.one_leads.domain.exception.BusinessException;
import com.luan.kenzley.one_leads.infrastructure.repository.EmpresaRepository;
import com.luan.kenzley.one_leads.interfaces.dto.empresa.EmpresaUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CnpjNaoDuplicadoRule implements EmpresaUpdateValidationRule {

    @Autowired
    private EmpresaRepository empresaRepo;

    @Override
    public void validate(Long id, EmpresaUpdateDTO dto) {
        Empresa atual = empresaRepo.findById(id)
                .orElseThrow(() -> new BusinessException(EmpresaError.EMPRESA_NAO_ENCONTRADA));

        if (!atual.getCnpj().equals(dto.cnpj()) &&
                empresaRepo.findByCnpj(dto.cnpj()).isPresent()) {
            throw new BusinessException(EmpresaError.CNPJ_DUPLICADO);
        }
    }
}
