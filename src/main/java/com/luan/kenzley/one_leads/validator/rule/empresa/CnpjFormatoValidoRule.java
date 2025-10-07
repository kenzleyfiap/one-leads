package com.luan.kenzley.one_leads.validator.rule.empresa;

import com.luan.kenzley.one_leads.dto.EmpresaDTO;
import com.luan.kenzley.one_leads.exception.BusinessException;
import org.springframework.stereotype.Component;

@Component
public class CnpjFormatoValidoRule implements EmpresaValidationRule {

    @Override
    public void validate(EmpresaDTO dto) {
        if (!CnpjValidator.isValid(dto.cnpj())) {
            throw new BusinessException("CNPJ inválido: " + dto.cnpj());
        }
    }
}
