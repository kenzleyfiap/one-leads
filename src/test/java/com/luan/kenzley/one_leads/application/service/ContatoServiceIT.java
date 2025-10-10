package com.luan.kenzley.one_leads.application.service;

import com.luan.kenzley.one_leads.domain.exception.BusinessException;
import com.luan.kenzley.one_leads.domain.model.Contato;
import com.luan.kenzley.one_leads.domain.model.Empresa;
import com.luan.kenzley.one_leads.infrastructure.repository.ContatoRepository;
import com.luan.kenzley.one_leads.infrastructure.repository.EmpresaRepository;
import com.luan.kenzley.one_leads.interfaces.dto.contato.ContatoDTO;
import com.luan.kenzley.one_leads.utils.ContatoHelper;
import com.luan.kenzley.one_leads.utils.EmpresaHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ContatoServiceIT {

    @Autowired
    private ContatoService contatoService;

    @Autowired
    private ContatoRepository contatoRepo;

    @Autowired
    private EmpresaRepository empresaRepo;

    private Contato contato;
    private Empresa empresa;

    @BeforeEach
    void setup() {
        contato = contatoRepo.save(ContatoHelper.criarContatoEntity());
        empresa = empresaRepo.save(EmpresaHelper.criarEmpresaEntity());
    }

    @Nested
    class CriarContato {

        @Test
        void deveCriarContatoComSucesso() {
            var dto = new ContatoDTO("novo", "novo@mail.com", "", "");
            var result = contatoService.criarContato(dto);

            assertThat(result).isNotNull();
            assertThat(result.getEmail()).isEqualTo(dto.email());
        }
    }

    @Nested
    class AtualizarContato {

        @Test
        void deveAtualizarContatoComSucesso() {
            var dto = new ContatoDTO("Novo Nome", "novo@email.com", "999999999", "Atualizado");
            var result = contatoService.atualizarContato(contato.getId(), dto);

            assertThat(result.getNome()).isEqualTo("Novo Nome");
            assertThat(result.getEmail()).isEqualTo("novo@email.com");
        }

        @Test
        void deveLancarExceptionQuandoContatoNaoExiste() {
            var dto = ContatoHelper.criarContatoDTO();

            assertThatThrownBy(() -> contatoService.atualizarContato(999L, dto))
                    .isInstanceOf(BusinessException.class);
        }
    }

    @Nested
    class BuscarContato {

        @Test
        void deveBuscarPorNome() {
            var result = contatoService.buscarContato(contato.getNome(), null);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getEmail()).isEqualTo(contato.getEmail());
        }

        @Test
        void deveBuscarPorEmail() {
            var result = contatoService.buscarContato(null, contato.getEmail());

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getNome()).isEqualTo(contato.getNome());
        }

        @Test
        void deveLancarExceptionQuandoFiltroAusente() {
            assertThatThrownBy(() -> contatoService.buscarContato(null, null))
                    .isInstanceOf(BusinessException.class);
        }

        @Test
        void deveLancarExceptionQuandoNenhumContatoEncontrado() {
            assertThatThrownBy(() -> contatoService.buscarContato("Inexistente", null))
                    .isInstanceOf(BusinessException.class);
        }
    }

    @Nested
    class RemoverVinculo {

        @Test
        void deveRemoverVinculoComSucesso() {
            // Cria segundo contato
            Contato segundoContato = new Contato();
            segundoContato.setNome("Maria");
            segundoContato.setEmail("maria@email.com");
            segundoContato.setTelefone("61988888888");
            segundoContato.setObservacoes("Contato secundário");
            segundoContato = contatoRepo.save(segundoContato);

            // Vincula ambos os contatos à empresa
            empresa.getContatos().add(contato);
            empresa.getContatos().add(segundoContato);
            contato.getEmpresas().add(empresa);
            segundoContato.getEmpresas().add(empresa);

            empresaRepo.saveAndFlush(empresa);
            contatoRepo.saveAndFlush(contato);
            contatoRepo.saveAndFlush(segundoContato);

            // Remove vínculo do primeiro contato
            contatoService.removerVinculoContato(empresa.getId(), contato.getId());

            var empresaAtualizada = empresaRepo.findById(empresa.getId()).orElseThrow();
            assertThat(empresaAtualizada.getContatos()).doesNotContain(contato);
            assertThat(empresaAtualizada.getContatos()).contains(segundoContato);
        }

        @Test
        void deveLancarExceptionQuandoEmpresaNaoExiste() {
            assertThatThrownBy(() -> contatoService.removerVinculoContato(999L, contato.getId()))
                    .isInstanceOf(BusinessException.class);
        }

        @Test
        void deveLancarExceptionQuandoContatoNaoExiste() {
            assertThatThrownBy(() -> contatoService.removerVinculoContato(empresa.getId(), 999L))
                    .isInstanceOf(BusinessException.class);
        }
    }

    @Nested
    class ListarContatosSemEmpresa {

        @Test
        void deveListarContatosSemEmpresa() {
            var result = contatoService.listarContatosSemEmpresa();

            assertThat(result).extracting("id").contains(contato.getId());
        }

        @Test
        void deveLancarExceptionQuandoNaoExistemContatosSemEmpresa() {
            // Vincula o contato à empresa
            empresa.getContatos().add(contato);
            contato.getEmpresas().add(empresa);

            empresaRepo.saveAndFlush(empresa);
            contatoRepo.saveAndFlush(contato);

            // Remove qualquer outro contato sem vínculo que possa ter sido criado em setup
            var contatosSemEmpresa = contatoRepo.findByEmpresasIsEmpty();
            contatosSemEmpresa.forEach(c -> {
                c.getEmpresas().add(empresa);
                empresa.getContatos().add(c);
                contatoRepo.save(c);
            });
            empresaRepo.save(empresa);

            assertThatThrownBy(() -> contatoService.listarContatosSemEmpresa())
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Contato não encontrado");
        }
    }
}