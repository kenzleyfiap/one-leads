package com.luan.kenzley.one_leads.shared;

public class ContatoSwaggerExamples {


    public static final String EMAIL_OBRIGATORIO_EXAMPLE = """
        {
          "timestamp": "2025-10-08T13:30:00",
          "status": 400,
          "error": "Regra de negócio violada",
          "code": "EMAIL_OBRIGATORIO",
          "message": "E-mail é obrigatório"
        }
        """;

    public static final String EMAIL_DUPLICADO_EXAMPLE = """
        {
          "timestamp": "2025-10-08T13:30:00",
          "status": 409,
          "error": "Regra de negócio violada",
          "code": "EMAIL_DUPLICADO",
          "message": "E-mail não pode ser duplicado"
        }
        """;

    public static final String FILTRO_OBRIGATORIO_EXAMPLE = """
        {
          "timestamp": "2025-10-08T13:30:00",
          "status": 404,
          "error": "Regra de negócio violada",
          "code": "FILTRO_OBRIGATORIO",
          "message": "Informe nome ou email"
        }
        """;

    public static final String CONTATO_NAO_ENCONTRADO_EXAMPLE = """
        {
          "timestamp": "2025-10-08T13:30:00",
          "status": 404,
          "error": "Regra de negócio violada",
          "code": "CONTATO_NAO_ENCONTRADO",
          "message": "Contato não encontrado"
        }
        """;

    public static final String CONTATO_UNICO_EXAMPLE = """
        {
          "timestamp": "2025-10-08T13:30:00",
          "status": 404,
          "error": "Regra de negócio violada",
          "code": "CONTATO_UNICO",
          "message": "Empresa possui apenas um contato. Vínculo não pode ser removido."
        }
        """;



}
