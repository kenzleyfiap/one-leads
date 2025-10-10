# one-leads

🧱 **Arquitetura Geral**
- **Backend:** Java 17 + Spring Boot
- **Persistência:** Spring Data JPA + PostgreSQL
- **Documentação:** Swagger (Springdoc OpenAPI)
- **Containerização:** Docker + Docker Compose

---

## 🚀 Como executar a aplicação localmente

### Pré-requisitos
- **Java 17**+
- **Maven**
- **PostgreSQL** rodando localmente (ou banco configurado via `application.yml`)

### Passos
1. **Clone o repositório**:
   ```bash
   git clone https://github.com/kenzleyfiap/one-leads.git
   cd one-leads

2. **Configure o banco de dados em src/main/resources/application.yml:**
     ````
    spring:
      datasource:
        url: jdbc:postgresql://oneleads-db:5432/leads
        username: user
        password: pass
        driver-class-name: org.postgresql.Driver
    ````
3. **Execute o projeto**:
    ```bash
    ./mvnw spring-boot:run

## 🐳 Como executar com Docker e Docker Compose

#### **1. Usando imagem publicada no Docker Hub**

 * **Certifique-se de que o arquivo <b>docker-compose.yml<b> está configurado assim:**
    ````
    version: '3.8'
    
    services:
      oneleads-api:
        image: luankenzley/oneleads:1.0
        ports:
          - "8080:8080"
        depends_on:
          - oneleads-db
        environment:
          SPRING_DATASOURCE_URL: jdbc:postgresql://oneleads-db:5432/leads
          SPRING_DATASOURCE_USERNAME: user
          SPRING_DATASOURCE_PASSWORD: pass
          SPRING_JPA_HIBERNATE_DDL_AUTO: none
    
      oneleads-db:
        image: postgres
        environment:
          POSTGRES_DB: leads
          POSTGRES_USER: user
          POSTGRES_PASSWORD: pass
        ports:
          - "5432:5432"
    ````
   
#### 2. **Subindo os containers**

````bash
docker-compose up -d
````

## 📘 **Acessando a aplicação**

1. API estará disponível em: http://localhost:8080
2. A documentação Swagger pode ser acessada em: http://localhost:8080/ ou  http://localhost:8080/swagger-ui.html
