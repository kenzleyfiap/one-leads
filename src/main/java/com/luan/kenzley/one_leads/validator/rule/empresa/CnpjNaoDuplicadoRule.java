package com.luan.kenzley.one_leads.validator.rule.empresa;

import com.luan.kenzley.one_leads.domain.Empresa;
import com.luan.kenzley.one_leads.dto.EmpresaUpdateDTO;
import com.luan.kenzley.one_leads.exception.BusinessException;
import com.luan.kenzley.one_leads.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CnpjNaoDuplicadoRule implements EmpresaUpdateValidationRule {

    @Autowired
    private EmpresaRepository empresaRepo;

    @Override
    public void validate(Long id, EmpresaUpdateDTO dto) {
        Empresa atual = empresaRepo.findById(id).orElseThrow();
        if (!atual.getCnpj().equals(dto.cnpj()) &&
                empresaRepo.findByCnpj(dto.cnpj()).isPresent()) {
            throw new BusinessException("CNPJ_DUPLICADO", "CNPJ já cadastrado: " + dto.cnpj());
        }
    }
}
