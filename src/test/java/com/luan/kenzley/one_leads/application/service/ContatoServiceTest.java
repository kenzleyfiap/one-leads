package com.luan.kenzley.one_leads.application.service;

import com.luan.kenzley.one_leads.application.mapper.ContatoMapper;
import com.luan.kenzley.one_leads.application.validator.ContatoUpdateValidator;
import com.luan.kenzley.one_leads.application.validator.ContatoValidator;
import com.luan.kenzley.one_leads.domain.exception.BusinessException;
import com.luan.kenzley.one_leads.infrastructure.repository.ContatoRepository;
import com.luan.kenzley.one_leads.infrastructure.repository.EmpresaRepository;
import com.luan.kenzley.one_leads.utils.ContatoHelper;
import com.luan.kenzley.one_leads.utils.EmpresaHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ContatoServiceTest {

    @Mock
    private ContatoRepository contatoRepo;

    @Mock
    private EmpresaRepository empresaRepo;

    @Mock
    private ContatoValidator contatoValidator;

    @Mock
    private ContatoUpdateValidator updateValidator;

    @Mock
    private ContatoMapper mapper;

    @Mock
    private EmpresaContatoService empresaContatoService;

    @InjectMocks
    private ContatoService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class CriarContato {

        @Test
        void deveCriarContatoComSucesso() {
            var dto = ContatoHelper.criarContatoDTO();
            var entity = ContatoHelper.criarContatoEntity();
            var response = ContatoHelper.criarContatoResponseDTO();

            when(mapper.toEntity(dto)).thenReturn(entity);
            when(contatoRepo.save(entity)).thenReturn(entity);
            when(mapper.toResponse(entity)).thenReturn(response);

            var result = service.criarContato(dto);

            assertThat(result).isEqualTo(response);
            verify(contatoValidator).validar(dto);
            verify(contatoRepo).save(entity);
        }
    }

    @Nested
    class AtualizarContato {

        @Test
        void deveAtualizarContatoComSucesso() {
            var dto = ContatoHelper.criarContatoDTO();
            var entity = ContatoHelper.criarContatoEntity();

            when(contatoRepo.findById(1L)).thenReturn(Optional.of(entity));
            when(contatoRepo.save(entity)).thenReturn(entity);

            var result = service.atualizarContato(1L, dto);

            assertThat(result).isEqualTo(entity);
            verify(updateValidator).validar(1L, dto);
            verify(contatoRepo).save(entity);
        }

        @Test
        void deveLancarExceptionQuandoContatoNaoEncontrado() {
            var dto = ContatoHelper.criarContatoDTO();

            when(contatoRepo.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.atualizarContato(1L, dto))
                    .isInstanceOf(NoSuchElementException.class);
        }
    }

    @Nested
    class BuscarContato {

        @Test
        void deveBuscarContatoPorNome() {
            var entity = ContatoHelper.criarContatoEntity();
            var response = ContatoHelper.criarContatoResponseDTO();

            when(contatoRepo.findByNomeContainingIgnoreCase("João")).thenReturn(List.of(entity));
            when(mapper.toResponse(entity)).thenReturn(response);

            var result = service.buscarContato("João", null);

            assertThat(result).containsExactly(response);
        }

        @Test
        void deveBuscarContatoPorEmail() {
            var entity = ContatoHelper.criarContatoEntity();
            var response = ContatoHelper.criarContatoResponseDTO();

            when(contatoRepo.findByEmailContainingIgnoreCase("joao@email.com")).thenReturn(List.of(entity));
            when(mapper.toResponse(entity)).thenReturn(response);

            var result = service.buscarContato(null, "joao@email.com");

            assertThat(result).containsExactly(response);
        }

        @Test
        void deveLancarExceptionQuandoFiltroAusente() {
            assertThatThrownBy(() -> service.buscarContato(null, null))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Informe nome ou e-mail para buscar");
        }

        @Test
        void deveLancarExceptionQuandoNenhumContatoEncontrado() {
            when(contatoRepo.findByNomeContainingIgnoreCase("João")).thenReturn(List.of());

            assertThatThrownBy(() -> service.buscarContato("João", null))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Contato não encontrado");
        }
    }

    @Nested
    class RemoverVinculoContato {

        @Test
        void deveRemoverVinculoComSucesso() {
            var empresa = EmpresaHelper.criarEmpresaEntity();
            var contato = ContatoHelper.criarContatoEntity();

            when(empresaRepo.findById(1L)).thenReturn(Optional.of(empresa));
            when(contatoRepo.findById(2L)).thenReturn(Optional.of(contato));

            service.removerVinculoContato(1L, 2L);

            verify(empresaContatoService).removerVinculo(empresa, contato);
            verify(empresaRepo).save(empresa);
            verify(contatoRepo).save(contato);
        }

        @Test
        void deveLancarExceptionQuandoEmpresaNaoEncontrada() {
            when(empresaRepo.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.removerVinculoContato(1L, 2L))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Empresa não encontrada");
        }

        @Test
        void deveLancarExceptionQuandoContatoNaoEncontrado() {
            var empresa = EmpresaHelper.criarEmpresaEntity();
            when(empresaRepo.findById(1L)).thenReturn(Optional.of(empresa));
            when(contatoRepo.findById(2L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.removerVinculoContato(1L, 2L))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Contato não encontrado");
        }
    }

    @Nested
    class ListarContatosSemEmpresa {

        @Test
        void deveListarContatosSemEmpresaComSucesso() {
            var entity = ContatoHelper.criarContatoEntity();
            var response = ContatoHelper.criarContatoResponseDTO();

            when(contatoRepo.findByEmpresasIsEmpty()).thenReturn(List.of(entity));
            when(mapper.toResponse(entity)).thenReturn(response);

            var result = service.listarContatosSemEmpresa();

            assertThat(result).containsExactly(response);
        }

        @Test
        void deveLancarExceptionQuandoNaoExistemContatos() {
            when(contatoRepo.findByEmpresasIsEmpty()).thenReturn(List.of());

            assertThatThrownBy(() -> service.listarContatosSemEmpresa())
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Contato não encontrado");
        }
    }
}