# API de Pacientes

## Perfis autorizados

### Cadastro e dados administrativos

```text
ADMIN
RECEPCIONISTA
MEDICO
```

O médico poderá consultar dados necessários aos próprios atendimentos.

O acesso a informações clínicas será controlado por endpoints específicos.

---

## 1. Listar pacientes

```http
GET /api/pacientes
```

### Filtros

```http
GET /api/pacientes?nome=Maria&cpf=12345678901&ativo=true&page=0&size=20
```

| Parâmetro | Tipo |
|---|---|
| `nome` | Texto |
| `cpf` | Texto |
| `telefone` | Texto |
| `ativo` | Boolean |
| `page` | Número |
| `size` | Número |
| `sort` | Texto |

### Resposta

```json
{
  "content": [
    {
      "id": 15,
      "nomeCompleto": "Maria da Silva",
      "cpf": "12345678901",
      "dataNascimento": "1990-05-10",
      "telefone": "11999999999",
      "email": "maria@email.com",
      "ativo": true
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1,
  "totalPages": 1,
  "first": true,
  "last": true
}
```

A API poderá mascarar parcialmente o CPF em listagens, conforme decisão de segurança.

---

## 2. Buscar paciente por ID

```http
GET /api/pacientes/{id}
```

### Resposta

```json
{
  "id": 15,
  "nomeCompleto": "Maria da Silva",
  "cpf": "12345678901",
  "dataNascimento": "1990-05-10",
  "sexo": "FEMININO",
  "telefone": "11999999999",
  "telefoneSecundario": null,
  "email": "maria@email.com",
  "nomeMae": "Joana da Silva",
  "endereco": {
    "cep": "01001000",
    "logradouro": "Praça da Sé",
    "numero": "100",
    "complemento": null,
    "bairro": "Sé",
    "cidade": "São Paulo",
    "estado": "SP"
  },
  "observacoes": null,
  "ativo": true,
  "criadoEm": "2026-07-20T10:00:00-03:00"
}
```

---

## 3. Cadastrar paciente

```http
POST /api/pacientes
```

### Requisição

```json
{
  "nomeCompleto": "Maria da Silva",
  "cpf": "12345678901",
  "dataNascimento": "1990-05-10",
  "sexo": "FEMININO",
  "telefone": "11999999999",
  "telefoneSecundario": null,
  "email": "maria@email.com",
  "nomeMae": "Joana da Silva",
  "endereco": {
    "cep": "01001000",
    "logradouro": "Praça da Sé",
    "numero": "100",
    "complemento": null,
    "bairro": "Sé",
    "cidade": "São Paulo",
    "estado": "SP"
  },
  "observacoes": null
}
```

### Validações

- Nome obrigatório;
- CPF obrigatório, válido e único;
- data de nascimento obrigatória;
- data não pode ser futura;
- telefone obrigatório;
- e-mail válido quando informado;
- estado deve possuir duas letras;
- CPF e CEP deverão ser recebidos sem caracteres de formatação.

### Resposta — `201 Created`

Retorna o paciente cadastrado.

---

## 4. Atualizar paciente

```http
PUT /api/pacientes/{id}
```

Utiliza estrutura semelhante ao cadastro.

### Regras

- CPF não poderá pertencer a outro paciente;
- alterações deverão ser auditadas;
- conteúdo clínico não deverá ser alterado por este endpoint.

### Resposta — `200 OK`

Retorna o registro atualizado.

---

## 5. Desativar paciente

```http
PATCH /api/pacientes/{id}/desativar
```

### Resposta

```http
204 No Content
```

### Regras

- Preservar consultas e documentos;
- impedir novos agendamentos;
- não cancelar automaticamente agendamentos futuros;
- alertar ou impedir a desativação quando houver agendamentos ativos, conforme regra definida no Service.

Para o MVP, a existência de agendamentos futuros ativos deverá retornar:

```http
409 Conflict
```

---

## 6. Ativar paciente

```http
PATCH /api/pacientes/{id}/ativar
```

### Resposta

```http
204 No Content
```

---

## 7. Consultar histórico de consultas do paciente

```http
GET /api/pacientes/{id}/consultas
```

### Perfis

```text
MEDICO
ADMIN, somente quando autorizado
```

A recepcionista não terá acesso ao conteúdo clínico.

### Filtros

```http
GET /api/pacientes/15/consultas?dataInicio=2026-01-01&dataFim=2026-12-31&page=0&size=20
```

### Resposta resumida

```json
{
  "content": [
    {
      "id": 20,
      "medico": {
        "id": 4,
        "nomeCompleto": "Dra. Ana Souza"
      },
      "iniciadaEm": "2026-07-20T09:00:00-03:00",
      "finalizadaEm": "2026-07-20T09:25:00-03:00",
      "status": "FINALIZADA"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1,
  "totalPages": 1
}
```