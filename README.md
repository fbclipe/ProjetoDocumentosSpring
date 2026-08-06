# API de Gestão de Documentos

API REST em Java 17 e Spring Boot 3 para cadastro de usuários, upload de documentos e recuperação
dos arquivos em Base64. Desenvolvida como desafio técnico, com autenticação por token, camada de
persistência em PostgreSQL e documentação interativa via OpenAPI.

## Stack

| Camada | Tecnologia |
| --- | --- |
| Linguagem | Java 17 |
| Framework | Spring Boot 3.4.3 (Web, Data JPA) |
| Persistência | PostgreSQL (produção) e H2 (testes) |
| Documentação | springdoc-openapi 2.5 (Swagger UI) |
| Testes | JUnit 5, Mockito, JaCoCo |
| Build | Maven |

## Funcionalidades

- Cadastro e autenticação de usuários, com emissão de token portador e validação de expiração.
- Upload de documentos via `multipart/form-data`, com limite de 10 MB por arquivo.
- Listagem de todos os documentos e busca por identificador.
- Conversão do documento armazenado para Base64, retornando nome original e classificação.
- Tratamento centralizado de erros, devolvendo status HTTP e mensagem em formato consistente.

## Organização do código

```
challenge/src/main/java/com/extraidados/challenge
├── controller/   AuthController, ControllerUser, DocumentController
├── service/      regras de negócio, autenticação e manipulação de arquivos
├── repository/   interfaces Spring Data JPA
├── entity/       User e Documents (mapeamento JPA)
├── model/        DTOs de entrada (login, registro, Base64)
├── response/     objetos de resposta da API
└── exception/    ApiException e MessageException
```

A separação em controller, service e repository mantém as regras de negócio fora dos controllers,
que ficam responsáveis apenas por receber a requisição, delegar e montar a resposta HTTP.

## Como executar

Pré-requisitos: Java 17, Maven e uma instância do PostgreSQL.

Crie o banco:

```sql
CREATE DATABASE document;
```

Ajuste as credenciais em `challenge/src/main/resources/application.properties` e suba a aplicação:

```bash
cd challenge
mvn spring-boot:run
```

A API sobe em `http://localhost:8080`. O schema é criado automaticamente pelo Hibernate na
primeira execução.

## Documentação da API

Com a aplicação no ar, a Swagger UI fica disponível em:

```
http://localhost:8080/documentacao
```

A interface lista todos os endpoints, os parâmetros esperados e permite disparar requisições
direto do navegador. É a referência mais atualizada dos contratos.

## Testes

```bash
cd challenge
mvn test
```

A suíte cobre controllers, services e repositories, usando H2 em memória para os testes de
persistência e Mockito para isolar as dependências nos testes de unidade. O relatório de
cobertura do JaCoCo é gerado em `target/site/jacoco/index.html`.

## Autor

Felipe Barros de Carvalho — [LinkedIn](https://linkedin.com/in/felipe-barros-de-carvalho)
