package com.luan.kenzley.one_leads.application.service;

import com.luan.kenzley.one_leads.application.mapper.ContatoMapper;
import com.luan.kenzley.one_leads.application.validator.ContatoUpdateValidator;
import com.luan.kenzley.one_leads.application.validator.ContatoValidator;
import com.luan.kenzley.one_leads.domain.exception.BusinessException;
import com.luan.kenzley.one_leads.domain.exception.ContatoError;
import com.luan.kenzley.one_leads.domain.exception.EmpresaError;
import com.luan.kenzley.one_leads.domain.model.Contato;
import com.luan.kenzley.one_leads.domain.model.Empresa;
import com.luan.kenzley.one_leads.infrastructure.repository.ContatoRepository;
import com.luan.kenzley.one_leads.infrastructure.repository.EmpresaRepository;
import com.luan.kenzley.one_leads.interfaces.dto.contato.ContatoDTO;
import com.luan.kenzley.one_leads.interfaces.dto.contato.ContatoResponseDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContatoService {

    private final ContatoRepository contatoRepo;
    private final ContatoValidator contatoValidator;
    private final ContatoUpdateValidator updateValidator;
    private final EmpresaRepository empresaRepo;
    private final ContatoMapper mapper;
    private final EmpresaContatoService empresaContatoService;

    public ContatoService(ContatoRepository contatoRepo,
                          ContatoValidator contatoValidator,
                          ContatoUpdateValidator updateValidator,
                          EmpresaRepository empresaRepo,
                          ContatoMapper mapper,
                          EmpresaContatoService empresaContatoService) {
        this.contatoRepo = contatoRepo;
        this.contatoValidator = contatoValidator;
        this.updateValidator = updateValidator;
        this.empresaRepo = empresaRepo;
        this.mapper = mapper;
        this.empresaContatoService = empresaContatoService;
    }

    @Transactional
    public ContatoResponseDTO criarContato(ContatoDTO dto) {
        contatoValidator.validar(dto);
        Contato contato = contatoRepo.save(mapper.toEntity(dto));
        return mapper.toResponse(contato);
    }

    @Transactional
    public Contato atualizarContato(Long id, ContatoDTO dto) {
        updateValidator.validar(id, dto);

        Contato contato = contatoRepo.findById(id).orElseThrow();
        contato.setNome(dto.nome());
        contato.setEmail(dto.email());
        contato.setTelefone(dto.telefone());
        contato.setObservacoes(dto.observacoes());

        return contatoRepo.save(contato);
    }


    public List<ContatoResponseDTO> buscarContato(String nome, String email) {
        List<Contato> contatos = buscarPorFiltro(nome, email);

        if (contatos.isEmpty()) {
            throw new BusinessException(ContatoError.CONTATO_NAO_ENCONTRADO);
        }

        return contatos.stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public void removerVinculoContato(Long empresaId, Long contatoId) {
        Empresa empresa = empresaRepo.findById(empresaId)
                .orElseThrow(() -> new BusinessException(EmpresaError.EMPRESA_NAO_ENCONTRADA));

        Contato contato = contatoRepo.findById(contatoId)
                .orElseThrow(() -> new BusinessException(ContatoError.CONTATO_NAO_ENCONTRADO));

        empresaContatoService.removerVinculo(empresa, contato);
        empresaRepo.save(empresa);
        contatoRepo.save(contato);
    }

    public List<ContatoResponseDTO> listarContatosSemEmpresa() {
        List<Contato> contatos = contatoRepo.findByEmpresasIsEmpty();

        if(contatos.isEmpty()){
            throw new BusinessException(ContatoError.CONTATO_NAO_ENCONTRADO);
        }

        return contatos.stream()
                .map(mapper::toResponse)
                .toList();
    }

    private List<Contato> buscarPorFiltro(String nome, String email) {
        if (nome != null && !nome.isBlank()) {
            return contatoRepo.findByNomeContainingIgnoreCase(nome);
        } else if (email != null && !email.isBlank()) {
            return contatoRepo.findByEmailContainingIgnoreCase(email);
        } else {
            throw new BusinessException(ContatoError.FILTRO_OBRIGATORIO);
        }
    }
}
