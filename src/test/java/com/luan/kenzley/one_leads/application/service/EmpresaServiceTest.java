package com.luan.kenzley.one_leads.application.service;

import com.luan.kenzley.one_leads.application.mapper.EmpresaMapper;
import com.luan.kenzley.one_leads.application.validator.EmpresaUpdateValidator;
import com.luan.kenzley.one_leads.application.validator.EmpresaValidator;
import com.luan.kenzley.one_leads.domain.exception.BusinessException;
import com.luan.kenzley.one_leads.infrastructure.repository.ContatoRepository;
import com.luan.kenzley.one_leads.infrastructure.repository.EmpresaRepository;
import com.luan.kenzley.one_leads.interfaces.dto.empresa.EmpresaResumoDTO;
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
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class EmpresaServiceTest {

    @Mock
    private EmpresaRepository empresaRepo;

    @Mock
    private ContatoRepository contatoRepo;

    @Mock
    private EmpresaValidator empresaValidator;

    @Mock
    private EmpresaUpdateValidator updateValidator;

    @Mock
    private EmpresaMapper mapper;

    @Mock
    private EmpresaContatoService empresaContatoService;

    @InjectMocks
    private EmpresaService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class CriarEmpresa {

        @Test
        void deveCriarEmpresaComSucesso() {
            var dto = EmpresaHelper.criarEmpresaDTO();
            var entity = EmpresaHelper.criarEmpresaEntity();
            var response = EmpresaHelper.criarEmpresaResponseDTO();
            var contato = ContatoHelper.criarContatoEntity();

            when(mapper.toEntity(dto)).thenReturn(entity);
            when(contatoRepo.findById(anyLong())).thenReturn(Optional.of(contato));
            when(empresaRepo.save(entity)).thenReturn(entity);
            when(mapper.toResponse(entity)).thenReturn(response);

            var result = service.criarEmpresa(dto);

            assertThat(result).isEqualTo(response);
            verify(empresaValidator).validar(dto);
            verify(empresaRepo).save(entity);
        }
    }

    @Nested
    class AtualizarEmpresa {

        @Test
        void deveAtualizarEmpresaComSucesso() {
            var dto = EmpresaHelper.criarEmpresaUpdateDTO();
            var entity = EmpresaHelper.criarEmpresaEntity();

            when(empresaRepo.findById(1L)).thenReturn(Optional.of(entity));
            when(empresaRepo.save(entity)).thenReturn(entity);

            var result = service.atualizarEmpresa(1L, dto);

            assertThat(result).isEqualTo(entity);
            verify(updateValidator).validar(1L, dto);
            verify(empresaRepo).save(entity);
        }

        @Test
        void deveLancarExceptionQuandoEmpresaNaoEncontrada() {
            var dto = EmpresaHelper.criarEmpresaUpdateDTO();

            when(empresaRepo.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.atualizarEmpresa(1L, dto))
                    .isInstanceOf(NoSuchElementException.class);
        }
    }

    @Nested
    class BuscarEmpresa {

        @Test
        void deveBuscarEmpresaPorNome() {
            var entity = EmpresaHelper.criarEmpresaEntity();
            var response = EmpresaHelper.criarEmpresaResponseDTO();

            when(empresaRepo.findByNomeContainingIgnoreCase("Teste")).thenReturn(List.of(entity));
            when(mapper.toResponse(entity)).thenReturn(response);

            var result = service.buscarEmpresa("Teste", null);

            assertThat(result).containsExactly(response);
        }

        @Test
        void deveBuscarEmpresaPorCnpj() {
            var entity = EmpresaHelper.criarEmpresaEntity();
            var response = EmpresaHelper.criarEmpresaResponseDTO();

            when(empresaRepo.findByCnpjContaining("123456789")).thenReturn(List.of(entity));
            when(mapper.toResponse(entity)).thenReturn(response);

            var result = service.buscarEmpresa(null, "123456789");

            assertThat(result).containsExactly(response);
        }

        @Test
        void deveLancarExceptionQuandoFiltroAusente() {
            assertThatThrownBy(() -> service.buscarEmpresa(null, null))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Informe nome ou CNPJ para buscar");
        }

        @Test
        void deveLancarExceptionQuandoEmpresaNaoEncontrada() {
            when(empresaRepo.findByNomeContainingIgnoreCase("Teste")).thenReturn(List.of());

            assertThatThrownBy(() -> service.buscarEmpresa("Teste", null))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Empresa não encontrada");
        }
    }

    @Nested
    class ListarEmpresasComPrimeiroContato {

        @Test
        void deveListarEmpresasComPrimeiroContato() {
            var empresa = EmpresaHelper.criarEmpresaEntity();
            var contato = ContatoHelper.criarContatoEntity();
            empresa.setContatos(Set.of(contato));

            when(empresaRepo.findAll()).thenReturn(List.of(empresa));

            var result = service.listarEmpresasComPrimeiroContato();

            assertThat(result).hasSize(1);
            assertThat(result.get(0)).isInstanceOf(EmpresaResumoDTO.class);
        }
    }

    @Nested
    class ListarContatosPorEmpresa {

        @Test
        void deveListarContatosPorEmpresaComSucesso() {
            var empresa = EmpresaHelper.criarEmpresaEntity();
            var contato = ContatoHelper.criarContatoEntity();
            empresa.setContatos(Set.of(contato));

            when(empresaRepo.findById(1L)).thenReturn(Optional.of(empresa));

            var result = service.listarContatosPorEmpresa(1L);

            assertThat(result).hasSize(1);
        }

        @Test
        void deveLancarExceptionQuandoEmpresaNaoEncontrada() {
            when(empresaRepo.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.listarContatosPorEmpresa(1L))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Empresa não encontrada");
        }
    }

    @Nested
    class ExcluirEmpresa {

        @Test
        void deveExcluirEmpresaComSucesso() {
            var empresa = EmpresaHelper.criarEmpresaEntity();
            var contato = ContatoHelper.criarContatoEntity();
            var contato2 = ContatoHelper.criarContatoEntity();
            empresa.setContatos(Set.of(contato, contato2));

            when(empresaRepo.findById(1L)).thenReturn(Optional.of(empresa));

            service.excluirEmpresa(1L);

            verify(empresaContatoService).removerVinculo(empresa, contato);
            verify(empresaRepo).delete(empresa);
        }

        @Test
        void deveLancarExceptionQuandoEmpresaNaoEncontrada() {
            when(empresaRepo.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.excluirEmpresa(1L))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Empresa não encontrada");
        }
    }

    @Nested
    class VincularContato {

        @Test
        void deveVincularContatoComSucesso() {
            var empresa = EmpresaHelper.criarEmpresaEntity();
            var contato = ContatoHelper.criarContatoEntity();

            when(empresaRepo.findById(1L)).thenReturn(Optional.of(empresa));
            when(contatoRepo.findById(2L)).thenReturn(Optional.of(contato));

            service.vincularContato(1L, 2L);

            verify(empresaContatoService).vincular(empresa, contato);
            verify(empresaRepo).save(empresa);
            verify(contatoRepo).save(contato);
        }

        @Test
        void deveLancarExceptionQuandoEmpresaNaoEncontrada() {
            when(empresaRepo.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.vincularContato(1L, 2L))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Empresa não encontrada");
        }

        @Test
        void deveLancarExceptionQuandoContatoNaoEncontrado() {
            var empresa = EmpresaHelper.criarEmpresaEntity();
            when(empresaRepo.findById(1L)).thenReturn(Optional.of(empresa));
            when(contatoRepo.findById(2L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.vincularContato(1L, 2L))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Contato não encontrado");
        }
    }
}