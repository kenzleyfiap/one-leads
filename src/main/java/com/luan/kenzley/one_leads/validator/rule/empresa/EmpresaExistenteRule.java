package com.luan.kenzley.one_leads.validator.rule.empresa;

import com.luan.kenzley.one_leads.dto.EmpresaUpdateDTO;
import com.luan.kenzley.one_leads.exception.BusinessException;
import com.luan.kenzley.one_leads.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmpresaExistenteRule implements EmpresaUpdateValidationRule {

    @Autowired
    private EmpresaRepository empresaRepo;

    @Override
    public void validate(Long id, EmpresaUpdateDTO dto) {
        if (!empresaRepo.existsById(id)) {
            throw new BusinessException("EMPRESA_NAO_ENCONTRADA", "Empresa com ID " + id + " não encontrada");
        }
    }
}

