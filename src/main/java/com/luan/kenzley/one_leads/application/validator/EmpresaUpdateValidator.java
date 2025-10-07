package com.luan.kenzley.one_leads.application.validator;

import com.luan.kenzley.one_leads.dto.EmpresaUpdateDTO;

public interface EmpresaUpdateValidator {
    void validar(Long id, EmpresaUpdateDTO dto);
}
