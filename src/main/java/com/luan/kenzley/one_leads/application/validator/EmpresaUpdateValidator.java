package com.luan.kenzley.one_leads.application.validator;


import com.luan.kenzley.one_leads.interfaces.dto.empresa.EmpresaUpdateDTO;

public interface EmpresaUpdateValidator {
    void validar(Long id, EmpresaUpdateDTO dto);
}
