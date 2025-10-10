package com.luan.kenzley.one_leads.interfaces.controller;

import com.luan.kenzley.one_leads.config.ControllerTestBase;
import com.luan.kenzley.one_leads.domain.exception.BusinessException;
import com.luan.kenzley.one_leads.domain.exception.ContatoError;
import com.luan.kenzley.one_leads.interfaces.dto.contato.ContatoDTO;
import com.luan.kenzley.one_leads.utils.ContatoHelper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContatoController.class)
@ContextConfiguration(classes = ContatoController.class)
class ContatoControllerTest extends ControllerTestBase {

    @Nested
    class CriarContato {

        @Test
        void deveCriarContatoComSucesso() throws Exception {
            var dto = ContatoHelper.criarContatoDTO();

            when(contatoService.criarContato(any())).thenReturn(ContatoHelper.criarContatoResponseDTO());

            mockMvc.perform(post("/contatos")
                            .contentType("application/json")
                            .content(toJson(dto)))
                    .andExpect(status().isOk());

            verify(contatoService, times(1)).criarContato(any());
        }

        @Test
        void deveLancarExceptionQuandoEmailDuplicado() throws Exception {
            var dto = ContatoHelper.criarContatoDTO();

            doThrow(new BusinessException(ContatoError.EMAIL_DUPLICADO))
                    .when(contatoService).criarContato(any());

            mockMvc.perform(post("/contatos")
                            .contentType("application/json")
                            .content(toJson(dto)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.code").value("EMAIL_DUPLICADO"))
                    .andExpect(jsonPath("$.message").value("E-mail não pode ser duplicado"));

            verify(contatoService, times(1)).criarContato(any());
        }
    }

    @Nested
    class AtualizarContato {

        @Test
        void deveAtualizarContatoComSucesso() throws Exception {
            var dto = ContatoHelper.criarContatoDTO();

            when(contatoService.atualizarContato(anyLong(), any(ContatoDTO.class)))
                    .thenReturn(ContatoHelper.criarContatoEntity());

            mockMvc.perform(put("/contatos/{id}", 1L)
                            .contentType("application/json")
                            .content(toJson(dto)))
                    .andExpect(status().isOk());

            verify(contatoService, times(1)).atualizarContato(anyLong(), any());
        }

        @Test
        void deveLancarExceptionQuandoContatoNaoEncontrado() throws Exception {
            var dto = ContatoHelper.criarContatoDTO();

            doThrow(new BusinessException(ContatoError.CONTATO_NAO_ENCONTRADO))
                    .when(contatoService).atualizarContato(anyLong(), any());

            mockMvc.perform(put("/contatos/{id}", 1L)
                            .contentType("application/json")
                            .content(toJson(dto)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("CONTATO_NAO_ENCONTRADO"))
                    .andExpect(jsonPath("$.message").value("Contato não encontrado"));

            verify(contatoService, times(1)).atualizarContato(anyLong(), any());
        }
    }

    @Nested
    class BuscarContato {

        @Test
        void deveBuscarContatoPorNomeComSucesso() throws Exception {
            when(contatoService.buscarContato(any(), any()))
                    .thenReturn(List.of(ContatoHelper.criarContatoResponseDTO()));

            mockMvc.perform(get("/contatos/buscar")
                            .param("nome", "João"))
                    .andExpect(status().isOk());

            verify(contatoService, times(1)).buscarContato(any(), any());
        }

        @Test
        void deveBuscarContatoPorEmailComSucesso() throws Exception {
            when(contatoService.buscarContato(any(), any()))
                    .thenReturn(List.of(ContatoHelper.criarContatoResponseDTO()));

            mockMvc.perform(get("/contatos/buscar")
                            .param("email", "joao@email.com"))
                    .andExpect(status().isOk());

            verify(contatoService, times(1)).buscarContato(any(), any());
        }
    }

    @Nested
    class DesvincularContato {

        @Test
        void deveDesvincularContatoComSucesso() throws Exception {
            doNothing().when(contatoService).removerVinculoContato(anyLong(), anyLong());

            mockMvc.perform(delete("/contatos/{empresaId}/contatos/{contatoId}", 1L, 2L))
                    .andExpect(status().isNoContent());

            verify(contatoService, times(1)).removerVinculoContato(anyLong(), anyLong());
        }

        @Test
        void deveLancarExceptionQuandoContatoNaoEncontrado() throws Exception {
            doThrow(new BusinessException(ContatoError.CONTATO_NAO_ENCONTRADO))
                    .when(contatoService).removerVinculoContato(anyLong(), anyLong());

            mockMvc.perform(delete("/contatos/{empresaId}/contatos/{contatoId}", 1L, 2L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("CONTATO_NAO_ENCONTRADO"))
                    .andExpect(jsonPath("$.message").value("Contato não encontrado"));

            verify(contatoService, times(1)).removerVinculoContato(anyLong(), anyLong());
        }

        @Test
        void deveLancarExceptionQuandoContatoUnico() throws Exception {
            doThrow(new BusinessException(ContatoError.CONTATO_UNICO))
                    .when(contatoService).removerVinculoContato(anyLong(), anyLong());

            mockMvc.perform(delete("/contatos/{empresaId}/contatos/{contatoId}", 1L, 2L))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("CONTATO_UNICO"))
                    .andExpect(jsonPath("$.message").value("E-mail deve ser único"));

            verify(contatoService, times(1)).removerVinculoContato(anyLong(), anyLong());
        }
    }

    @Nested
    class ListarContatosSemEmpresa {

        @Test
        void deveListarContatosSemEmpresaComSucesso() throws Exception {
            when(contatoService.listarContatosSemEmpresa())
                    .thenReturn(List.of(ContatoHelper.criarContatoResponseDTO()));

            mockMvc.perform(get("/contatos/sem-empresa"))
                    .andExpect(status().isOk());

            verify(contatoService, times(1)).listarContatosSemEmpresa();
        }

        @Test
        void deveRetornarNotFoundQuandoNaoExistemContatosSemEmpresa() throws Exception {
            doThrow(new BusinessException(ContatoError.CONTATO_NAO_ENCONTRADO))
                    .when(contatoService).listarContatosSemEmpresa();

            mockMvc.perform(get("/contatos/sem-empresa"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("CONTATO_NAO_ENCONTRADO"))
                    .andExpect(jsonPath("$.message").value("Contato não encontrado"));

            verify(contatoService, times(1)).listarContatosSemEmpresa();
        }
    }
}