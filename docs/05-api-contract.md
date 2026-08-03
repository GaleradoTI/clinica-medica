# Contrato da API REST

## 1. Objetivo

Este documento define o contrato inicial da API REST do Sistema de Gestão para Clínica Médica.

A documentação servirá como referência para:

- Desenvolvimento do backend com Spring Boot;
- desenvolvimento do frontend com React;
- criação dos DTOs;
- implementação das validações;
- configuração do Spring Security;
- documentação com OpenAPI e Swagger;
- elaboração dos testes de API;
- automação de testes;
- integração entre frontend e backend.

---

## 2. URL base

Em ambiente local:

```text
http://localhost:8080/api
```

Exemplo:

```http
GET http://localhost:8080/api/pacientes
```

Em produção, a URL será configurada por variável de ambiente.

---

## 3. Versionamento

A primeira versão poderá utilizar:

```text
/api
```

Quando houver necessidade de alterações incompatíveis, será adotado:

```text
/api/v1
/api/v2
```

Para o MVP, será mantido o prefixo simples `/api`.

---

## 4. Formato dos dados

A API utilizará:

```text
application/json
```

Cabeçalho padrão:

```http
Content-Type: application/json
Accept: application/json
```

---

## 5. Autenticação

Endpoints protegidos deverão receber:

```http
Authorization: Bearer ACCESS_TOKEN
```

O access token será um JWT gerado pelo backend.

Endpoints públicos iniciais:

```http
POST /api/auth/login
POST /api/auth/refresh
```

Os demais endpoints exigirão autenticação.

---

## 6. Perfis

Perfis disponíveis:

```text
ADMIN
RECEPCIONISTA
MEDICO
```

### Administrador

Possui acesso a:

- Usuários;
- médicos;
- especialidades;
- auditoria;
- configurações administrativas;
- visualização geral dos agendamentos.

### Recepcionista

Possui acesso a:

- Pacientes;
- agenda;
- agendamentos;
- confirmação;
- presença;
- cancelamento;
- reagendamento.

Não terá acesso ao conteúdo clínico.

### Médico

Possui acesso a:

- Própria agenda;
- seus atendimentos;
- prontuários autorizados;
- alergias;
- receitas;
- atestados.

---

## 7. Métodos HTTP

| Método | Uso |
|---|---|
| `GET` | Consultar registros |
| `POST` | Criar registros ou executar ações |
| `PUT` | Atualizar um recurso completo |
| `PATCH` | Executar atualização parcial ou transição de status |
| `DELETE` | Excluir somente recursos sem histórico relevante |

Para pacientes, médicos, usuários e registros clínicos será utilizada desativação ou cancelamento no lugar da exclusão física.

---

## 8. Status HTTP

| Status | Significado |
|---:|---|
| `200` | Operação realizada com sucesso |
| `201` | Recurso criado |
| `204` | Operação concluída sem conteúdo |
| `400` | Requisição inválida |
| `401` | Usuário não autenticado |
| `403` | Usuário sem permissão |
| `404` | Recurso não encontrado |
| `409` | Conflito de dados ou regra |
| `422` | Operação não permitida para o estado atual |
| `500` | Erro interno inesperado |

---

## 9. Datas e horários

Datas utilizarão o padrão ISO 8601.

Data:

```json
"dataNascimento": "1995-10-25"
```

Data e horário:

```json
"inicioEm": "2026-08-10T09:00:00-03:00"
```

O backend deverá armazenar os instantes considerando o fuso horário.

---

## 10. Paginação

Listagens extensas serão paginadas.

Exemplo:

```http
GET /api/pacientes?page=0&size=20&sort=nomeCompleto,asc
```

Parâmetros:

| Parâmetro | Regra |
|---|---|
| `page` | Inicia em zero |
| `size` | Padrão 20 |
| `sort` | Campo e direção |
| `direction` | `asc` ou `desc`, quando utilizado separadamente |

Resposta:

```json
{
  "content": [],
  "page": 0,
  "size": 20,
  "totalElements": 0,
  "totalPages": 0,
  "first": true,
  "last": true
}
```

O tamanho máximo recomendado será:

```text
100 registros por página
```

---

## 11. Filtros

Filtros serão enviados por query parameters.

Exemplo:

```http
GET /api/pacientes?nome=Gabriel&ativo=true
```

Exemplo para agendamentos:

```http
GET /api/agendamentos?medicoId=10&dataInicio=2026-08-01&dataFim=2026-08-31
```

Filtros vazios deverão ser ignorados.

---

## 12. Identificadores

Os identificadores utilizarão números inteiros positivos.

Exemplo:

```http
GET /api/pacientes/15
```

Quando o ID não existir:

```http
404 Not Found
```

---

## 13. Exclusão lógica

Recursos com histórico serão desativados.

Exemplo:

```http
PATCH /api/pacientes/15/desativar
```

Reativação:

```http
PATCH /api/pacientes/15/ativar
```

---

## 14. Idempotência

Requisições de consulta serão idempotentes.

Operações como ativar ou desativar deverão produzir o mesmo estado mesmo que chamadas novamente.

Exemplo:

```http
PATCH /api/pacientes/15/desativar
```

Caso o paciente já esteja inativo, a API poderá:

- retornar `200` com o estado atual; ou
- retornar `422` indicando que a transição não é necessária.

Para o projeto, será adotado `422` para transições inválidas.

---

## 15. Respostas de sucesso

Cadastro:

```json
{
  "id": 15,
  "nomeCompleto": "Maria da Silva",
  "ativo": true,
  "criadoEm": "2026-08-01T10:30:00-03:00"
}
```

Operações sem necessidade de retorno poderão responder:

```http
204 No Content
```

---

## 16. Respostas de erro

Todos os erros utilizarão uma estrutura padronizada.

```json
{
  "timestamp": "2026-08-01T10:30:00-03:00",
  "status": 400,
  "error": "VALIDATION_ERROR",
  "message": "Existem campos inválidos.",
  "path": "/api/pacientes",
  "traceId": "bf18b716-2647-4ac6-bc10-a5799b6cbf80",
  "fieldErrors": [
    {
      "field": "nomeCompleto",
      "message": "O nome completo é obrigatório."
    }
  ]
}
```

Detalhes completos estão em:

```text
docs/api/error-responses.md
```

---

## 17. Segurança

A API deverá:

- Validar permissões no backend;
- impedir acesso por alteração manual da URL;
- não retornar senhas;
- não retornar tokens em logs;
- não expor dados clínicos para recepcionistas;
- limitar acesso de médicos aos atendimentos autorizados;
- validar o usuário em todas as operações protegidas;
- registrar operações críticas em auditoria.

---

## 18. Documentos relacionados

- [Autenticação](./api/authentication.md)
- [Usuários](./api/users.md)
- [Pacientes](./api/patients.md)
- [Médicos e especialidades](./api/doctors-and-specialties.md)
- [Agenda e agendamentos](./api/schedules-and-appointments.md)
- [Consultas e prontuários](./api/consultations-and-records.md)
- [Receitas e atestados](./api/prescriptions-and-certificates.md)
- [Respostas de erro](./api/error-responses.md)

---

## 19. Resultado esperado

Este contrato deverá orientar a criação de:

- Controllers;
- DTOs de entrada;
- DTOs de saída;
- Services;
- validações;
- regras de autorização;
- documentação OpenAPI;
- testes de integração;
- coleções do Postman;
- cenários automatizados.