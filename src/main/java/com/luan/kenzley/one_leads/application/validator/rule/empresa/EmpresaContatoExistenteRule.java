package com.luan.kenzley.one_leads.application.validator.rule.empresa;

import com.luan.kenzley.one_leads.domain.exception.BusinessException;
import com.luan.kenzley.one_leads.domain.exception.ContatoError;
import com.luan.kenzley.one_leads.infrastructure.repository.ContatoRepository;
import com.luan.kenzley.one_leads.interfaces.dto.empresa.EmpresaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmpresaContatoExistenteRule implements EmpresaValidationRule {

    @Autowired
    private ContatoRepository contatoRepo;

    @Override
    public void validate(EmpresaDTO dto) {
        dto.contatosIds().forEach(id -> {
            if (!contatoRepo.existsById(id)) {
                throw new BusinessException(ContatoError.CONTATO_NAO_ENCONTRADO);
            }
        });
    }
}
