
# Contas API

Uma API RESTful para gerenciar contas financeiras, incluindo funcionalidades como criação, atualização, exclusão e importação de contas via arquivo CSV.

## Índice

- [Visão Geral](#visão-geral)
- [Funcionalidades](#funcionalidades)
- [Começando](#começando)
- [Configuração](#configuração)
- [Construção e Execução](#construção-e-execução)
- [Endpoints da API](#endpoints-da-api)
- [Importação de Contas](#importação-de-contas)
- [Testes](#testes)
- [Deploy com Docker](#deploy-com-docker)
- [Licença](#licença)

## Visão Geral

A Contas API é projetada para gerenciar contas financeiras, fornecendo endpoints para criar, ler, atualizar, excluir e importar contas de um arquivo CSV. Utiliza o Spring Boot como framework backend e integra-se com um banco de dados PostgreSQL.

## Funcionalidades

- **Operações CRUD** para contas.
- **Funcionalidade de importação CSV** para upload em massa de contas.
- **Suporte à paginação** para listagem de contas.
- **Swagger UI** para documentação e teste da API.
- **Testes unitários e de integração** para garantir a confiabilidade.

## Começando

### Pré-requisitos

- Java 17
- Gradle
- Docker (para deploy containerizado)

### Clonar o Repositório

```bash
git clone https://github.com/marcusjunior/contas.git
cd contas
```

## Configuração

A configuração da aplicação pode ser definida no arquivo `application.properties` localizado no diretório `src/main/resources`.

```properties
spring.application.name=contas
spring.datasource.url=jdbc:postgresql://localhost:5432/contas_db
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.security.user.name=admin
spring.security.user.password=admin
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=2MB
spring.servlet.multipart.max-request-size=2MB
```

## Construção e Execução

### Construir o Projeto

```bash
./gradlew build
```

### Executar a Aplicação

```bash
./gradlew bootRun
```

### Acessar o Swagger UI

Abra seu navegador e navegue até `http://localhost:8080/swagger-ui.html` para acessar o Swagger UI para documentação e teste da API.

## Endpoints da API

### Endpoints de Conta

- **Criar Conta**
  - `POST /api/contas`
- **Obter Conta por ID**
  - `GET /api/contas/{id}`
- **Atualizar Conta**
  - `PUT /api/contas/{id}`
- **Atualizar Situação Conta**
  - `PUT /api/contas/{id}/situacao`
- **Excluir Conta**
  - `DELETE /api/contas/{id}`
- **Listar Contas**
  - `GET /api/contas`
  
### Importação de CSV

- **Importar Contas**
  - `POST /api/contas/import`
  - **Descrição:** Importa contas de um arquivo CSV.

## Importação de Contas

Para importar contas de um arquivo CSV, use o endpoint `/api/contas/import`. O arquivo CSV deve ter as seguintes colunas:

- dataVencimento
- dataPagamento
- valor
- valorPago
- descricao
- situacao

Exemplo de comando CURL:

```bash
curl -X POST "http://localhost:8080/api/contas/import" -H "accept: */*" -H "Content-Type: multipart/form-data" -F "file=@contas_a_pagar.csv"
```

## Testes

### Testes Unitários

Execute os testes unitários usando o seguinte comando:

```bash
./gradlew test
```

## Deploy com Docker

Um `Dockerfile` e `docker-compose.yml` são fornecidos para deploy containerizado.

### Construir Imagem Docker

```bash
docker build -t contas-api .
```

### Executar com Docker Compose

```bash
docker-compose up
```
