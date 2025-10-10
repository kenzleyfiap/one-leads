package com.luan.kenzley.one_leads.application.service;

import com.luan.kenzley.one_leads.application.mapper.EmpresaMapper;
import com.luan.kenzley.one_leads.application.validator.EmpresaUpdateValidator;
import com.luan.kenzley.one_leads.application.validator.EmpresaValidator;
import com.luan.kenzley.one_leads.domain.exception.BusinessException;
import com.luan.kenzley.one_leads.domain.exception.ContatoError;
import com.luan.kenzley.one_leads.domain.exception.EmpresaError;
import com.luan.kenzley.one_leads.domain.model.Contato;
import com.luan.kenzley.one_leads.domain.model.Empresa;
import com.luan.kenzley.one_leads.infrastructure.repository.ContatoRepository;
import com.luan.kenzley.one_leads.infrastructure.repository.EmpresaRepository;
import com.luan.kenzley.one_leads.interfaces.dto.contato.ContatoResponseDTO;
import com.luan.kenzley.one_leads.interfaces.dto.empresa.EmpresaDTO;
import com.luan.kenzley.one_leads.interfaces.dto.empresa.EmpresaResponseDTO;
import com.luan.kenzley.one_leads.interfaces.dto.empresa.EmpresaResumoDTO;
import com.luan.kenzley.one_leads.interfaces.dto.empresa.EmpresaUpdateDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepo;
    private final ContatoRepository contatoRepo;
    private final EmpresaValidator empresaValidator;
    private final EmpresaUpdateValidator updateValidator;
    private final EmpresaMapper mapper;
    private final EmpresaContatoService empresaContatoService;

    public EmpresaService(EmpresaRepository empresaRepo,
                          ContatoRepository contatoRepo,
                          EmpresaValidator empresaValidator,
                          EmpresaUpdateValidator updateValidator,
                          EmpresaMapper mapper,
                          EmpresaContatoService empresaContatoService) {
        this.empresaRepo = empresaRepo;
        this.contatoRepo = contatoRepo;
        this.empresaValidator = empresaValidator;
        this.updateValidator = updateValidator;
        this.mapper = mapper;
        this.empresaContatoService = empresaContatoService;
    }

    @Transactional
    public EmpresaResponseDTO criarEmpresa(EmpresaDTO dto) {
        empresaValidator.validar(dto);
        Empresa empresa = mapper.toEntity(dto);

        Set<Contato> contatos = dto.contatosIds().stream()
                .map(id -> contatoRepo.findById(id).orElseThrow())
                .collect(Collectors.toSet());

        empresa.setContatos(contatos);
        return this.mapper.toResponse(empresaRepo.save(empresa));
    }


    public Empresa atualizarEmpresa(Long id, EmpresaUpdateDTO dto) {
        updateValidator.validar(id, dto);

        Empresa empresa = empresaRepo.findById(id).orElseThrow();
        empresa.setNome(dto.nome());
        empresa.setCnpj(dto.cnpj());

        return empresaRepo.save(empresa);
    }


    public List<EmpresaResponseDTO> buscarEmpresa(String nome, String cnpj) {
        List<Empresa> empresas = buscarPorFiltro(nome, cnpj);

        if(empresas.isEmpty()) {
            throw new BusinessException(EmpresaError.EMPRESA_NAO_ENCONTRADA);
        }
        return empresas.stream()
                .map(mapper::toResponse)
                .toList();
    }

    public List<EmpresaResumoDTO> listarEmpresasComPrimeiroContato() {
        return empresaRepo.findAll().stream()
                .map(empresa -> {
                    Contato primeiro = empresa.getContatos().stream().findFirst().orElse(null);
                    return new EmpresaResumoDTO(
                            empresa.getId(),
                            empresa.getNome(),
                            empresa.getCnpj(),
                            primeiro != null ? new ContatoResponseDTO(
                                    primeiro.getId(),
                                    primeiro.getNome(),
                                    primeiro.getEmail(),
                                    primeiro.getTelefone(),
                                    primeiro.getObservacoes()
                            ) : null
                    );
                }).toList();
    }

    public List<ContatoResponseDTO> listarContatosPorEmpresa(Long empresaId) {
        Empresa empresa = empresaRepo.findById(empresaId)
                .orElseThrow(() -> new BusinessException(EmpresaError.EMPRESA_NAO_ENCONTRADA));

        return empresa.getContatos().stream()
                .map(contato -> new ContatoResponseDTO(
                        contato.getId(),
                        contato.getNome(),
                        contato.getEmail(),
                        contato.getTelefone(),
                        contato.getObservacoes()
                )).toList();
    }

    public void excluirEmpresa(Long id) {
        Empresa empresa = empresaRepo.findById(id)
                .orElseThrow(() -> new BusinessException(EmpresaError.EMPRESA_NAO_ENCONTRADA));

        if (empresa.getContatos().size() == 1) {
            throw new BusinessException(EmpresaError.CONTATO_UNICO_REMOVER);
        }
        Set<Contato> contatos = new HashSet<>(empresa.getContatos());

        contatos.forEach(contato -> this.empresaContatoService.removerVinculo(empresa, contato));
        empresaRepo.delete(empresa);
    }

    @Transactional
    public void vincularContato(Long empresaId, Long contatoId) {
        Empresa empresa = empresaRepo.findById(empresaId)
                .orElseThrow(() -> new BusinessException(EmpresaError.EMPRESA_NAO_ENCONTRADA));

        Contato contato = contatoRepo.findById(contatoId)
                .orElseThrow(() -> new BusinessException(ContatoError.CONTATO_NAO_ENCONTRADO));

        empresaContatoService.vincular(empresa, contato);
        empresaRepo.save(empresa);
        contatoRepo.save(contato);
    }

    private List<Empresa> buscarPorFiltro(String nome, String cnpj) {
        if (nome != null && !nome.isBlank()) {
            return empresaRepo.findByNomeContainingIgnoreCase(nome);
        } else if (cnpj != null && !cnpj.isBlank()) {
            return empresaRepo.findByCnpjContaining(cnpj);
        } else {
            throw new BusinessException(EmpresaError.FILTRO_OBRIGATORIO);
        }
    }
}
