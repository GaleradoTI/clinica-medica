# Padrão de Respostas de Erro

## 1. Estrutura padrão

```json
{
  "timestamp": "2026-07-31T14:30:00-03:00",
  "status": 400,
  "error": "VALIDATION_ERROR",
  "message": "Existem campos inválidos.",
  "path": "/api/pacientes",
  "traceId": "bf18b716-2647-4ac6-bc10-a5799b6cbf80",
  "fieldErrors": []
}
```

---

## 2. Campos

| Campo | Descrição |
|---|---|
| `timestamp` | Data e horário |
| `status` | Código HTTP |
| `error` | Código interno |
| `message` | Mensagem compreensível |
| `path` | Endpoint chamado |
| `traceId` | Identificador para rastreamento |
| `fieldErrors` | Erros específicos de campos |
| `details` | Detalhes adicionais permitidos |

---

## 3. Erro de validação

```http
400 Bad Request
```

```json
{
  "timestamp": "2026-07-31T14:30:00-03:00",
  "status": 400,
  "error": "VALIDATION_ERROR",
  "message": "Existem campos inválidos.",
  "path": "/api/pacientes",
  "traceId": "bf18b716-2647-4ac6-bc10-a5799b6cbf80",
  "fieldErrors": [
    {
      "field": "nomeCompleto",
      "message": "O nome completo é obrigatório."
    },
    {
      "field": "email",
      "message": "O e-mail informado é inválido."
    }
  ]
}
```

---

## 4. Não autenticado

```http
401 Unauthorized
```

```json
{
  "timestamp": "2026-07-31T14:30:00-03:00",
  "status": 401,
  "error": "UNAUTHORIZED",
  "message": "É necessário realizar autenticação.",
  "path": "/api/pacientes",
  "traceId": "a67aeb37-f579-4e85-a5fc-fc733dd30415"
}
```

---

## 5. Sem permissão

```http
403 Forbidden
```

```json
{
  "timestamp": "2026-07-31T14:30:00-03:00",
  "status": 403,
  "error": "ACCESS_DENIED",
  "message": "Você não possui permissão para realizar esta operação.",
  "path": "/api/auditorias",
  "traceId": "22ddc972-498a-4459-990f-7f341d8f07cc"
}
```

---

## 6. Registro não encontrado

```http
404 Not Found
```

```json
{
  "timestamp": "2026-07-31T14:30:00-03:00",
  "status": 404,
  "error": "RESOURCE_NOT_FOUND",
  "message": "Paciente não encontrado.",
  "path": "/api/pacientes/999",
  "traceId": "8ef808ba-7c2c-47ef-9ff3-66d24903f770"
}
```

---

## 7. Registro duplicado

```http
409 Conflict
```

```json
{
  "timestamp": "2026-07-31T14:30:00-03:00",
  "status": 409,
  "error": "RESOURCE_CONFLICT",
  "message": "Já existe um paciente cadastrado com esse CPF.",
  "path": "/api/pacientes",
  "traceId": "8db77bda-2a56-41d0-a085-0c81cb7587f9"
}
```

---

## 8. Conflito de agendamento

```http
409 Conflict
```

```json
{
  "timestamp": "2026-07-31T14:30:00-03:00",
  "status": 409,
  "error": "APPOINTMENT_TIME_CONFLICT",
  "message": "O médico já possui um agendamento no período informado.",
  "path": "/api/agendamentos",
  "traceId": "b53c7dcc-9301-49bc-bda6-0786627d1775"
}
```

---

## 9. Transição inválida

```http
422 Unprocessable Entity
```

```json
{
  "timestamp": "2026-07-31T14:30:00-03:00",
  "status": 422,
  "error": "INVALID_STATUS_TRANSITION",
  "message": "Não é possível confirmar um agendamento cancelado.",
  "path": "/api/agendamentos/40/confirmar",
  "traceId": "78038c66-110e-4846-a451-ef374d80956f",
  "details": {
    "statusAtual": "CANCELADO",
    "statusSolicitado": "CONFIRMADO"
  }
}
```

---

## 10. Erro interno

```http
500 Internal Server Error
```

```json
{
  "timestamp": "2026-07-31T14:30:00-03:00",
  "status": 500,
  "error": "INTERNAL_SERVER_ERROR",
  "message": "Ocorreu um erro inesperado.",
  "path": "/api/pacientes",
  "traceId": "a828814a-4798-4dcb-a9bf-a34ed6156cb0"
}
```

A resposta não deverá expor:

- Stack trace;
- SQL;
- senhas;
- tokens;
- nomes de classes internas;
- configuração do servidor;
- dados clínicos.

---

## 11. Códigos internos planejados

```text
VALIDATION_ERROR
UNAUTHORIZED
INVALID_CREDENTIALS
ACCESS_DENIED
RESOURCE_NOT_FOUND
RESOURCE_CONFLICT
INVALID_STATUS_TRANSITION
APPOINTMENT_TIME_CONFLICT
SCHEDULE_BLOCK_CONFLICT
INACTIVE_USER
INACTIVE_PATIENT
INACTIVE_DOCTOR
TOKEN_EXPIRED
TOKEN_REVOKED
INTERNAL_SERVER_ERROR
```