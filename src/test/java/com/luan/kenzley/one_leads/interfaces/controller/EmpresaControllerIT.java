package com.luan.kenzley.one_leads.interfaces.controller;

import com.luan.kenzley.one_leads.config.BaseIntegrationTest;
import com.luan.kenzley.one_leads.interfaces.dto.empresa.EmpresaDTO;
import com.luan.kenzley.one_leads.interfaces.dto.empresa.EmpresaUpdateDTO;
import com.luan.kenzley.one_leads.interfaces.dto.empresa.VinculoContatoEmpresaDTO;
import com.luan.kenzley.one_leads.utils.EmpresaHelper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class EmpresaControllerIT extends BaseIntegrationTest {

    @LocalServerPort
    private int port;

    private Long empresaId;

    @BeforeAll
    void prepararDados() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        var dto = EmpresaHelper.criarEmpresaDTO();

        empresaId = Long.valueOf(
                given()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(dto)
                        .when()
                        .post("/empresas")
                        .then()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .path("id")
                        .toString()
        );
    }

    @Nested
    class CriarEmpresa {

        @Test
        void deveRetornarBadRequestQuandoCNPJInvalido() {
            var dto = new EmpresaDTO("Empresa Teste", "000000000000", List.of(1L));

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(dto)
                    .when()
                    .post("/empresas")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        void deveRetornarConflictQuandoCNPJDuplicado() {
            var dto = EmpresaHelper.criarEmpresaDTO();

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(dto)
                    .when()
                    .post("/empresas")
                    .then()
                    .statusCode(HttpStatus.CONFLICT.value());
        }
    }

    @Nested
    class AtualizarEmpresa {

        @Test
        void deveAtualizarComSucesso() {
            var dto = EmpresaHelper.criarEmpresaUpdateDTO();

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(dto)
                    .when()
                    .put("/empresas/" + empresaId)
                    .then()
                    .statusCode(HttpStatus.OK.value());
        }

        @Test
        void deveRetornarBadRequestQuandoEmpresaNaoEncontrada() {
            var dto = new EmpresaUpdateDTO("Empresa Teste", "000000000000");

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(dto)
                    .when()
                    .put("/empresas/" + 999999)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        void deveRetornarConflictQuandoCNPJDuplicado() {
            var dto = new EmpresaUpdateDTO("teste", "35444131000142");

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(dto)
                    .when()
                    .put("/empresas/" + empresaId)
                    .then()
                    .statusCode(HttpStatus.CONFLICT.value());
        }
    }

    @Nested
    class BuscarEmpresa {

        @Test
        void deveBuscarEmpresaPorNome() {
            given()
                    .queryParam("nome", "Empresa Teste")
                    .when()
                    .get("/empresas/buscar")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", greaterThan(0))
                    .body("[0].nome", equalTo("Empresa Teste"));
        }

        @Test
        void deveBuscarEmpresaPorCnpj() {
            given()
                    .queryParam("cnpj", "20496329000100")
                    .when()
                    .get("/empresas/buscar")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", greaterThan(0))
                    .body("[0].cnpj", equalTo("20496329000100"));
        }

        @Test
        void deveRetornarBadRequestQuandoNaoEncontrar() {
            given()
                    .queryParam("nome", "Inexistente")
                    .when()
                    .get("/empresas/buscar")
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }

        @Test
        void deveRetornarBadRequestAoBuscarSemParametros() {
            given()
                    .when()
                    .get("/empresas/buscar")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("size()", greaterThanOrEqualTo(1));
        }

    }

    @Nested
    class ListarContatoEmpresa {

        @Test
        void deveListarContatosDaEmpresa() {
            given()
                    .pathParam("id", empresaId)
                    .when()
                    .get("/empresas/{id}/contatos")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", greaterThan(0));
        }

        @Test
        void deveRetornar404QuandoEmpresaNaoExiste() {
            given()
                    .pathParam("id", 999999L)
                    .when()
                    .get("/empresas/{id}/contatos")
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }
    }

    @Nested
    class ExcluirEmpresa {

        @Test
        void deveRetornarConflictAoExcluirEmpresaComApenasUmContato() {
            given()
                    .pathParam("id", 50)
                    .when()
                    .delete("/empresas/{id}")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        void deveRetornar404QuandoEmpresaNaoExiste() {
            given()
                    .pathParam("id", 999999L)
                    .when()
                    .delete("/empresas/{id}")
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }

    }

    @Nested
    class VincularContrato {

        @Test
        void deveVincularContatoComSucesso() {
            var vinculoDTO = new VinculoContatoEmpresaDTO(empresaId, 2L);
            given()
                    .contentType(ContentType.JSON)
                    .body(vinculoDTO)
                    .when()
                    .post("/empresas/vincular-contato")
                    .then()
                    .statusCode(204);
        }



    }
}