package com.luan.kenzley.one_leads.validator.rule.contato;

import com.luan.kenzley.one_leads.dto.EmpresaDTO;
import com.luan.kenzley.one_leads.exception.BusinessException;
import com.luan.kenzley.one_leads.repository.ContatoRepository;
import com.luan.kenzley.one_leads.validator.rule.empresa.EmpresaValidationRule;
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
                throw new BusinessException("Contato com ID " + id + " não existe");
            }
        });
    }
}
