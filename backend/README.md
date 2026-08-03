# Clínica Médica API

Backend do Sistema de Gestão para Clínica Médica.

## Tecnologias

- Java 21;
- Spring Boot;
- Maven;
- Spring Web;
- Spring Security;
- Spring Data JPA;
- PostgreSQL;
- Flyway;
- Bean Validation;
- Actuator;
- JUnit.

## Requisitos

- JDK 21;
- Docker;
- Docker Compose.

## Executar o PostgreSQL

Na raiz do projeto:

```bash
docker compose up -d postgres
```

## Executar o backend

No Windows:

```bash
cd backend
mvnw.cmd spring-boot:run
```

## Verificar a aplicação

```text
GET http://localhost:8080/api/health
```

```text
GET http://localhost:8080/actuator/health
```

## Executar os testes

```bash
cd backend
mvnw.cmd test
```

## Profiles

- `dev`: desenvolvimento local;
- `test`: testes automatizados;
- `prod`: produção.

O profile padrão é `dev`.

## Banco de dados

A estrutura será versionada por migrations localizadas em:

```text
src/main/resources/db/migration
```