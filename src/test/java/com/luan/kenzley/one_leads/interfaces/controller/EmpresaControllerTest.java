package com.luan.kenzley.one_leads.interfaces.controller;

import com.luan.kenzley.one_leads.config.ControllerTestBase;
import com.luan.kenzley.one_leads.domain.exception.BusinessException;
import com.luan.kenzley.one_leads.domain.exception.EmpresaError;
import com.luan.kenzley.one_leads.domain.model.Empresa;
import com.luan.kenzley.one_leads.interfaces.dto.empresa.EmpresaUpdateDTO;
import com.luan.kenzley.one_leads.interfaces.dto.empresa.VinculoContatoEmpresaDTO;
import com.luan.kenzley.one_leads.utils.ContatoHelper;
import com.luan.kenzley.one_leads.utils.EmpresaHelper;
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

@WebMvcTest(EmpresaController.class)
@ContextConfiguration(classes = EmpresaController.class)
class EmpresaControllerTest extends ControllerTestBase {

    @Nested
    class CriarEmpresa {

        @Test
        void deveCriarEmpresaComSucesso() throws Exception {
            var dto = EmpresaHelper.criarEmpresaDTO();

            when(empresaService.criarEmpresa(any())).thenReturn(EmpresaHelper.criarEmpresaResponseDTO());

            mockMvc.perform(post("/empresas")
                            .contentType("application/json")
                            .content(toJson(dto)))
                    .andExpect(status().isOk());

            verify(empresaService, times(1)).criarEmpresa(any());
        }

        @Test
        void deveLancarExceptionQuandoCnpjInvalido() throws Exception {
            var dto = EmpresaHelper.criarEmpresaDTO();

            doThrow(new BusinessException(EmpresaError.CNPJ_INVALIDO))
                    .when(empresaService).criarEmpresa(any());

            mockMvc.perform(post("/empresas")
                            .contentType("application/json")
                            .content(toJson(dto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("CNPJ_INVALIDO"))
                    .andExpect(jsonPath("$.message").value("Cnpj invalido"));

            verify(empresaService, times(1)).criarEmpresa(any());
        }

        @Test
        void deveLancarExceptionQuandoCnpjJaCadastrado() throws Exception {
            var dto = EmpresaHelper.criarEmpresaDTO();

            doThrow(new BusinessException(EmpresaError.CNPJ_DUPLICADO))
                    .when(empresaService).criarEmpresa(any());

            mockMvc.perform(post("/empresas")
                            .contentType("application/json")
                            .content(toJson(dto)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.code").value("CNPJ_DUPLICADO"))
                    .andExpect(jsonPath("$.message").value("Cnpj duplicado"));

            verify(empresaService, times(1)).criarEmpresa(any());
        }
    }

    @Nested
    class AtualizarEmpresa {

        @Test
        void deveAtualizarEmpresaComSucesso() throws Exception {
            var dto = EmpresaHelper.criarEmpresaUpdateDTO();

            when(empresaService.atualizarEmpresa(any(Long.class), any(EmpresaUpdateDTO.class)))
                    .thenReturn(EmpresaHelper.criarEmpresaEntity());

            mockMvc.perform(put("/empresas/{id}", 1L)
                            .contentType("application/json")
                            .content(toJson(dto)))
                    .andExpect(status().isOk());

            verify(empresaService, times(1)).atualizarEmpresa(any(), any());
        }

        @Test
        void deveLancarExceptionQuandoEmpresaNaoCadastrada() throws Exception {
            var dto = EmpresaHelper.criarEmpresaUpdateDTO();

            doThrow(new BusinessException(EmpresaError.EMPRESA_NAO_ENCONTRADA))
                    .when(empresaService).atualizarEmpresa(any(), any());

            mockMvc.perform(put("/empresas/{id}", 1L)
                            .param("id", "1")
                            .contentType("application/json")
                            .content(toJson(dto)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("EMPRESA_NAO_ENCONTRADA"))
                    .andExpect(jsonPath("$.message").value("Empresa não encontrada"));

            verify(empresaService, times(1)).atualizarEmpresa(any(), any());
        }

        @Test
        void deveLancarExceptionQuandoCnpjJaCadastrado() throws Exception {
            var dto = EmpresaHelper.criarEmpresaUpdateDTO();

            doThrow(new BusinessException(EmpresaError.CNPJ_DUPLICADO))
                    .when(empresaService).atualizarEmpresa(any(), any());

            mockMvc.perform(put("/empresas/{id}", 1L)
                            .param("id", "1")
                            .contentType("application/json")
                            .content(toJson(dto)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.code").value("CNPJ_DUPLICADO"))
                    .andExpect(jsonPath("$.message").value("Cnpj duplicado"));

            verify(empresaService, times(1)).atualizarEmpresa(any(), any());
        }
    }

    @Nested
    class BuscarEmpresa {

        @Test
        void deveBuscarEmpresaPorNomeComSucesso() throws Exception {
            when(empresaService.buscarEmpresa(any(String.class), any(String.class)))
                    .thenReturn(List.of(EmpresaHelper.criarEmpresaResponseDTO()));

            mockMvc.perform(get("/empresas/buscar")
                            .param("nome", "Teste"))
                    .andExpect(status().isOk());

            verify(empresaService, times(1)).buscarEmpresa(any(), any());
        }

        @Test
        void deveBuscarEmpresaPorCnpjComSucesso() throws Exception {
            when(empresaService.buscarEmpresa(any(String.class), any(String.class)))
                    .thenReturn(List.of(EmpresaHelper.criarEmpresaResponseDTO()));

            mockMvc.perform(get("/empresas/buscar")
                            .param("cnpj", "12345678910101"))
                    .andExpect(status().isOk());

            verify(empresaService, times(1)).buscarEmpresa(any(), any());
        }

        @Test
        void deveRetornarConflictParaParametrosAusentes() throws Exception {
            doThrow(new BusinessException(EmpresaError.CNPJ_DUPLICADO))
                    .when(empresaService).buscarEmpresa(any(), any());

            mockMvc.perform(get("/empresas/buscar"))
                    .andExpect(status().isConflict());

            verify(empresaService, times(1)).buscarEmpresa(any(), any());
        }
    }

    @Nested
    class ListarEmpresas {

        @Test
        void deveListarEmpresasComPrimeiroContatoComSucesso() throws Exception {
            when(empresaService.listarEmpresasComPrimeiroContato())
                    .thenReturn(List.of(EmpresaHelper.criarEmpresaResumoDTO()));

            mockMvc.perform(get("/empresas"))
                    .andExpect(status().isOk());

            verify(empresaService, times(1)).listarEmpresasComPrimeiroContato();
        }

        @Test
        void deveListarContatosDeUmaEmpresa() throws Exception {
            when(empresaService.listarContatosPorEmpresa(any(Long.class)))
                    .thenReturn(List.of(ContatoHelper.criarContatoResponseDTO()));

            mockMvc.perform(get("/empresas/{id}/contatos", 1L))
                    .andExpect(status().isOk());

            verify(empresaService, times(1)).listarContatosPorEmpresa(any());
        }

        @Test
        void deveRetornarNotFoundParaEmpresaNaoCadastrada() throws Exception {
            doThrow(new BusinessException(EmpresaError.EMPRESA_NAO_ENCONTRADA))
                    .when(empresaService).listarContatosPorEmpresa(any());

            mockMvc.perform(get("/empresas/{id}/contatos", 1L))
                    .andExpect(status().isNotFound());

            verify(empresaService, times(1)).listarContatosPorEmpresa(any());
        }
    }

    @Nested
    class ExcluirEmpresa {

        @Test
        void deveExcluirEmpresaComSucesso() throws Exception {
            doNothing().when(empresaService).excluirEmpresa(any(Long.class));

            mockMvc.perform(delete("/empresas/{id}", 1L))
                    .andExpect(status().isNoContent());

            verify(empresaService, times(1)).excluirEmpresa(any());
        }

        @Test
        void deveLancarExceptionQuandoEmpresaNaoEncontrada() throws Exception {
            doThrow(new BusinessException(EmpresaError.EMPRESA_NAO_ENCONTRADA))
                    .when(empresaService).excluirEmpresa(any());

            mockMvc.perform(delete("/empresas/{id}", 1L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("EMPRESA_NAO_ENCONTRADA"))
                    .andExpect(jsonPath("$.message").value("Empresa não encontrada"));

            verify(empresaService, times(1)).excluirEmpresa(any());
        }
    }

    @Nested
    class VincularContato {

        @Test
        void deveVincularContatoComSucesso() throws Exception {
            var dto = new VinculoContatoEmpresaDTO(1L, 2L);

            doNothing().when(empresaService).vincularContato(anyLong(), anyLong());

            mockMvc.perform(post("/empresas/vincular-contato")
                            .contentType("application/json")
                            .content(toJson(dto)))
                    .andExpect(status().isNoContent());

            verify(empresaService, times(1)).vincularContato(anyLong(), anyLong());
        }

        @Test
        void deveLancarExceptionQuandoEmpresaOuContatoNaoEncontrado() throws Exception {
            var dto = EmpresaHelper.criarVinculoContatoEmpresaDTO();

            doThrow(new BusinessException(EmpresaError.EMPRESA_NAO_ENCONTRADA))
                    .when(empresaService).vincularContato(anyLong(), anyLong());

            mockMvc.perform(post("/empresas/vincular-contato")
                            .contentType("application/json")
                            .content(toJson(dto)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("EMPRESA_NAO_ENCONTRADA"))
                    .andExpect(jsonPath("$.message").value("Empresa não encontrada"));

            verify(empresaService, times(1)).vincularContato(anyLong(), anyLong());
        }

        @Test
        void deveLancarExceptionQuandoContatoJaVinculado() throws Exception {
            var dto = EmpresaHelper.criarVinculoContatoEmpresaDTO();

            doThrow(new BusinessException(EmpresaError.CONTATO_JA_VINCULADO))
                    .when(empresaService).vincularContato(anyLong(), anyLong());

            mockMvc.perform(post("/empresas/vincular-contato")
                            .contentType("application/json")
                            .content(toJson(dto)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.code").value("CONTATO_JA_VINCULADO"))
                    .andExpect(jsonPath("$.message").value("Contato já vinculado"));

            verify(empresaService, times(1)).vincularContato(anyLong(), anyLong());
        }
    }
}