# Arquitetura do Sistema

## Visão Geral

O sistema seguirá uma arquitetura em três camadas.

```text
React
↓
API REST
↓
Spring Boot
↓
PostgreSQL
```

## Frontend

Tecnologias:

- React
- TypeScript
- Vite
- React Router
- Axios
- React Hook Form
- Zod
- TanStack Query

---

## Backend

Tecnologias:

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Flyway
- JWT
- Swagger

---

## Banco

- PostgreSQL

---

## Comunicação

Frontend e Backend utilizarão JSON através de API REST.

---

## Arquitetura Backend

```text
Controller
↓
Service
↓
Repository
↓
Database
```

---

## Organização Frontend

```text
components/
pages/
features/
hooks/
services/
contexts/
layouts/
routes/
```

---

## Organização Backend

```text
auth/
usuarios/
pacientes/
medicos/
especialidades/
agenda/
agendamentos/
consultas/
prontuarios/
receitas/
atestados/
```

---

## Segurança

- JWT
- Spring Security
- BCrypt
- Controle de permissões
- Auditoria

---

## Banco

Será utilizado Flyway para controle das migrations.

---

## Testes

Backend

- JUnit
- Mockito
- Testcontainers

Frontend

- Vitest
- React Testing Library
- Cypress

---

## Ambientes

- Desenvolvimento
- Testes
- Produção

---

## Deploy

Frontend

- Vercel

Backend

- Render

Banco

- PostgreSQL