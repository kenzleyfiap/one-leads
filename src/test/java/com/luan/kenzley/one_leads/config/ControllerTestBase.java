package com.luan.kenzley.one_leads.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luan.kenzley.one_leads.application.mapper.ContatoMapper;
import com.luan.kenzley.one_leads.application.mapper.EmpresaMapper;
import com.luan.kenzley.one_leads.application.service.ContatoService;
import com.luan.kenzley.one_leads.application.service.EmpresaService;
import com.luan.kenzley.one_leads.domain.model.Empresa;
import com.luan.kenzley.one_leads.infrastructure.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@Import({ControllerTestBase.MockBeans.class, GlobalExceptionHandler.class})
public abstract class ControllerTestBase {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected EmpresaService empresaService;

    @Autowired
    protected EmpresaMapper empresaMapper;

    @Autowired
    protected ContatoService contatoService;

    @Autowired
    protected ContatoMapper contatoMapper;

    private AutoCloseable mocks;

    @BeforeEach
    void openMocks() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeMocks() throws Exception {
        Mockito.reset(empresaService, empresaMapper, contatoService, contatoMapper);
        mocks.close();
    }

    @TestConfiguration
    static class MockBeans {
        @Bean
        public EmpresaService empresaService() {
            return Mockito.mock(EmpresaService.class);
        }

        @Bean
        public EmpresaMapper empresaMapper() {
            return Mockito.mock(EmpresaMapper.class);
        }

        @Bean
        public ObjectMapper objectMapper() {
            return Jackson2ObjectMapperBuilder.json().build();
        }

        @Bean
        public ContatoService contatoService() {
            return Mockito.mock(ContatoService.class);
        }

        @Bean
        public ContatoMapper contatoMapper() {
            return Mockito.mock(ContatoMapper.class);
        }
    }

    protected String toJson(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }
}