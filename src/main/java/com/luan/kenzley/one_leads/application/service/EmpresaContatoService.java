package com.luan.kenzley.one_leads.application.service;

import com.luan.kenzley.one_leads.domain.exception.BusinessException;
import com.luan.kenzley.one_leads.domain.exception.EmpresaError;
import com.luan.kenzley.one_leads.domain.model.Contato;
import com.luan.kenzley.one_leads.domain.model.Empresa;
import org.springframework.stereotype.Service;

@Service
public class EmpresaContatoService {

    public void removerVinculo(Empresa empresa, Contato contato) {
        empresa.getContatos().remove(contato);
        contato.getEmpresas().remove(empresa);
    }

    public void vincular(Empresa empresa, Contato contato) {
        if (empresa.getContatos().contains(contato)) {
            throw new BusinessException(EmpresaError.CONTATO_JA_VINCULADO);
        }
        empresa.getContatos().add(contato);
        contato.getEmpresas().add(empresa);
    }

}
