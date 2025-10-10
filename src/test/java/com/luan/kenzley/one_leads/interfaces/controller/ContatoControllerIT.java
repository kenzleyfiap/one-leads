package com.luan.kenzley.one_leads.interfaces.controller;

import com.luan.kenzley.one_leads.config.BaseIntegrationTest;
import com.luan.kenzley.one_leads.interfaces.dto.contato.ContatoDTO;
import com.luan.kenzley.one_leads.utils.ContatoHelper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;


class ContatoControllerIT extends BaseIntegrationTest {

    @LocalServerPort
    private int port;

    private Long contatoId;

    @BeforeAll
    void prepararDados() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        var dto = ContatoHelper.criarContatoDTO();

        contatoId = Long.valueOf(
                given()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(dto)
                        .when()
                        .post("/contatos")
                        .then()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .path("id")
                        .toString()
        );
    }

    @Nested
    class CriarContato {

        @Test
        void deveRetornarBadRequestQuandoEmailInvalido() {
            var dto = new ContatoDTO("Contato Teste", "000000000000","","");

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(dto)
                    .when()
                    .post("/contatos")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        void deveRetornarConflictQuandoEmailDuplicado() {
            var dto = new ContatoDTO("teste", "teste2@mail.com", "", "");

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(dto)
                    .when()
                    .post("/contatos")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    class AtualizarContato {

        @Test
        void deveAtualizarComSucesso() {
            var dto = new ContatoDTO("Contato Teste", "000000000000","","");

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(dto)
                    .when()
                    .put("/contatos/{id}", contatoId)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        void deveRetornarBadRequestQuandoContatoNaoEncontrado() {
            var dto = new ContatoDTO("Contato Teste", "000000000000","","");

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(dto)
                    .when()
                    .put("/contatos/{id}", 900000)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }


    }

    @Nested
    class BuscarContato {

        @Test
        void deveBuscarPorNomeComSucesso() {
            given()
                    .queryParam("nome", "Contato Teste")
                    .when()
                    .get("/contatos/buscar")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", greaterThan(0))
                    .body("[0].nome", equalTo("Contato Teste"));
        }

        @Test
        void deveBuscarPorEmailComSucesso() {
            given()
                    .queryParam("email", "contato@mail.com")
                    .when()
                    .get("/contatos/buscar")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", greaterThan(0))
                    .body("[0].nome", equalTo("Contato Teste"));
        }

        @Test
        void deveRetornarNotFoundAoBuscarPorEmailNaoCadastrado() {
            given()
                    .queryParam("email", "contatonaocadastrado@mail.com")
                    .when()
                    .get("/contatos/buscar")
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }


        @Test
        void deveRetornarNotFoundAoBuscarPorNomeNaoCadastrado() {
            given()
                    .queryParam("nome", "Nome nao cadastrado")
                    .when()
                    .get("/contatos/buscar")
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }
    }


    @Nested
    class RemoverVinculo {

        @Test
        void deveRemoverComSucesso() {
            given()
                    .pathParam("empresaId", 50)
                    .pathParam("contatoId", 1)
                    .when()
                    .delete("/contatos/{empresaId}/contatos/{contatoId}")
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

        }

        @Test
        void deveRetornarNotFoundAoRemoverEmpresaInexistente() {
            given()
                    .pathParam("empresaId", 123456789)
                    .pathParam("contatoId", 1)
                    .when()
                    .delete("/contatos/{empresaId}/contatos/{contatoId}")
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());

        }

        @Test
        void deveRetornarNotFoundAoRemoverContatoInexistente() {
            given()
                    .pathParam("empresaId", 50)
                    .pathParam("contatoId", 123456789)
                    .when()
                    .delete("/contatos/{empresaId}/contatos/{contatoId}")
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());

        }

    }

    @Nested
    class SemEmpresa {
        @Test
        void deveListarComSucesso() {
            given()
                    .when()
                    .get("/contatos/sem-empresa")
                    .then()
                    .statusCode(HttpStatus.OK.value());

        }

    }


}