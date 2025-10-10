package com.luan.kenzley.one_leads.application.service;

import com.luan.kenzley.one_leads.domain.exception.BusinessException;
import com.luan.kenzley.one_leads.domain.model.Contato;
import com.luan.kenzley.one_leads.domain.model.Empresa;
import com.luan.kenzley.one_leads.infrastructure.repository.ContatoRepository;
import com.luan.kenzley.one_leads.infrastructure.repository.EmpresaRepository;
import com.luan.kenzley.one_leads.interfaces.dto.empresa.EmpresaDTO;
import com.luan.kenzley.one_leads.interfaces.dto.empresa.EmpresaResumoDTO;
import com.luan.kenzley.one_leads.interfaces.dto.empresa.EmpresaUpdateDTO;
import com.luan.kenzley.one_leads.utils.ContatoHelper;
import com.luan.kenzley.one_leads.utils.EmpresaHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class EmpresaServiceIT {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private EmpresaRepository empresaRepo;

    @Autowired
    private ContatoRepository contatoRepo;

    private Empresa empresa;
    private Contato contato;

    @BeforeEach
    void setup() {
        empresa = empresaRepo.save(EmpresaHelper.criarEmpresaEntity());
        contato = contatoRepo.save(ContatoHelper.criarContatoEntity());
    }

    @Nested
    class CriarEmpresa {

        @Test
        void deveCriarEmpresaComSucesso() {
            var dto = new EmpresaDTO("Empresa Nova", "12345678901234", List.of(contato.getId()));
            var result = empresaService.criarEmpresa(dto);

            assertThat(result).isNotNull();
            assertThat(result.getNome()).isEqualTo("Empresa Nova");
            assertThat(result.getContatos()).hasSize(1);
        }
    }

    @Nested
    class AtualizarEmpresa {

        @Test
        void deveAtualizarEmpresaComSucesso() {
            var dto = new EmpresaUpdateDTO("Empresa Atualizada", "98765432100000");
            var result = empresaService.atualizarEmpresa(empresa.getId(), dto);

            assertThat(result.getNome()).isEqualTo("Empresa Atualizada");
            assertThat(result.getCnpj()).isEqualTo("98765432100000");
        }

        @Test
        void deveLancarExceptionQuandoEmpresaNaoExiste() {
            var dto = new EmpresaUpdateDTO("Empresa X", "00000000000000");

            assertThatThrownBy(() -> empresaService.atualizarEmpresa(999L, dto))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Empresa não encontrada");
        }
    }

    @Nested
    class BuscarEmpresa {

        @Test
        void deveBuscarEmpresaPorNome() {
            var result = empresaService.buscarEmpresa(empresa.getNome(), null);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getCnpj()).isEqualTo(empresa.getCnpj());
        }

        @Test
        void deveBuscarEmpresaPorCnpj() {
            var result = empresaService.buscarEmpresa(null, empresa.getCnpj());

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getNome()).isEqualTo(empresa.getNome());
        }

        @Test
        void deveLancarExceptionQuandoFiltroAusente() {
            assertThatThrownBy(() -> empresaService.buscarEmpresa(null, null))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Informe nome ou CNPJ para buscar");
        }

        @Test
        void deveLancarExceptionQuandoEmpresaNaoEncontrada() {
            assertThatThrownBy(() -> empresaService.buscarEmpresa("Inexistente", null))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Empresa não encontrada");
        }
    }

    @Nested
    class ListarEmpresasComPrimeiroContato {

        @Test
        void deveListarEmpresasComPrimeiroContato() {
            empresa.getContatos().add(contato);
            contato.getEmpresas().add(empresa);
            empresaRepo.saveAndFlush(empresa);
            contatoRepo.saveAndFlush(contato);

            var result = empresaService.listarEmpresasComPrimeiroContato()
                    .stream()
                    .filter(e -> e.id().equals(empresa.getId()))
                    .toList();

            assertThat(result).hasSize(1);
            assertThat(result.get(0).primeiroContato()).isNotNull();
            assertThat(result.get(0)).isInstanceOf(EmpresaResumoDTO.class);
        }
    }

    @Nested
    class ListarContatosPorEmpresa {

        @Test
        void deveListarContatosPorEmpresaComSucesso() {
            empresa.getContatos().add(contato);
            contato.getEmpresas().add(empresa);
            empresaRepo.saveAndFlush(empresa);
            contatoRepo.saveAndFlush(contato);

            var result = empresaService.listarContatosPorEmpresa(empresa.getId());

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getEmail()).isEqualTo(contato.getEmail());
        }

        @Test
        void deveLancarExceptionQuandoEmpresaNaoExiste() {
            assertThatThrownBy(() -> empresaService.listarContatosPorEmpresa(999L))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Empresa não encontrada");
        }
    }

    @Nested
    class ExcluirEmpresa {

        @Test
        void deveExcluirEmpresaComSucesso() {
            // Cria segundo contato
            Contato segundoContato = new Contato();
            segundoContato.setNome("Maria");
            segundoContato.setEmail("maria@email.com");
            segundoContato.setTelefone("61988888888");
            segundoContato.setObservacoes("Contato secundário");
            segundoContato = contatoRepo.saveAndFlush(segundoContato);

            // Vincula os dois contatos à empresa (bidirecional)
            empresa.getContatos().add(contato);
            empresa.getContatos().add(segundoContato);
            contato.getEmpresas().add(empresa);
            segundoContato.getEmpresas().add(empresa);

            // Salva os vínculos
            contatoRepo.saveAndFlush(contato);
            contatoRepo.saveAndFlush(segundoContato);
            empresaRepo.saveAndFlush(empresa);

            // Recarrega empresa para garantir que os vínculos estão persistidos
            Empresa empresaPersistida = empresaRepo.findById(empresa.getId()).orElseThrow();
            assertThat(empresaPersistida.getContatos()).hasSizeGreaterThan(1);

            // Executa exclusão
            empresaService.excluirEmpresa(empresa.getId());

            // Verifica que empresa foi removida
            assertThat(empresaRepo.findById(empresa.getId())).isEmpty();
        }

        @Test
        void deveLancarExceptionQuandoEmpresaNaoExiste() {
            assertThatThrownBy(() -> empresaService.excluirEmpresa(999L))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Empresa não encontrada");
        }
    }

    @Nested
    class VincularContato {

        @Test
        void deveVincularContatoComSucesso() {
            empresaService.vincularContato(empresa.getId(), contato.getId());

            var empresaAtualizada = empresaRepo.findById(empresa.getId()).orElseThrow();
            assertThat(empresaAtualizada.getContatos()).contains(contato);
        }

        @Test
        void deveLancarExceptionQuandoEmpresaNaoExiste() {
            assertThatThrownBy(() -> empresaService.vincularContato(999L, contato.getId()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Empresa não encontrada");
        }

        @Test
        void deveLancarExceptionQuandoContatoNaoExiste() {
            assertThatThrownBy(() -> empresaService.vincularContato(empresa.getId(), 999L))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Contato não encontrado");
        }
    }
}