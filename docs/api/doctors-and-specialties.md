# API de Médicos e Especialidades

# Médicos

## Perfis administrativos

```text
ADMIN
```

A recepcionista poderá consultar médicos ativos para agendamentos.

---

## 1. Listar médicos

```http
GET /api/medicos
```

### Perfis

```text
ADMIN
RECEPCIONISTA
MEDICO
```

### Filtros

```http
GET /api/medicos?nome=Ana&crm=12345&uf=SP&especialidadeId=2&ativo=true
```

### Resposta

```json
{
  "content": [
    {
      "id": 4,
      "nomeCompleto": "Dra. Ana Souza",
      "crmNumero": "12345",
      "crmUf": "SP",
      "duracaoConsultaMinutos": 30,
      "especialidades": [
        {
          "id": 2,
          "nome": "Cardiologia",
          "principal": true
        }
      ],
      "ativo": true
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1,
  "totalPages": 1
}
```

---

## 2. Buscar médico

```http
GET /api/medicos/{id}
```

### Resposta

```json
{
  "id": 4,
  "usuarioId": 8,
  "nomeCompleto": "Dra. Ana Souza",
  "crmNumero": "12345",
  "crmUf": "SP",
  "telefone": "11988887777",
  "email": "ana@clinica.com",
  "duracaoConsultaMinutos": 30,
  "especialidades": [
    {
      "id": 2,
      "nome": "Cardiologia",
      "principal": true
    }
  ],
  "ativo": true
}
```

---

## 3. Cadastrar médico

```http
POST /api/medicos
```

### Perfil

```text
ADMIN
```

### Requisição

```json
{
  "usuarioId": 8,
  "nomeCompleto": "Dra. Ana Souza",
  "crmNumero": "12345",
  "crmUf": "SP",
  "telefone": "11988887777",
  "email": "ana@clinica.com",
  "duracaoConsultaMinutos": 30,
  "especialidades": [
    {
      "especialidadeId": 2,
      "principal": true
    }
  ]
}
```

### Regras

- Usuário deve existir;
- usuário deve possuir perfil `MEDICO`;
- usuário não poderá estar vinculado a outro médico;
- CRM e UF deverão ser únicos;
- pelo menos uma especialidade;
- apenas uma especialidade principal;
- duração deverá ser maior que zero.

### Resposta — `201 Created`

Retorna o médico criado.

---

## 4. Atualizar médico

```http
PUT /api/medicos/{id}
```

### Perfil

```text
ADMIN
```

### Resposta

```http
200 OK
```

---

## 5. Desativar médico

```http
PATCH /api/medicos/{id}/desativar
```

### Perfil

```text
ADMIN
```

### Regras

- Não aceitar novos agendamentos;
- preservar consultas antigas;
- não cancelar automaticamente agendamentos futuros;
- retornar `409` quando existirem agendamentos futuros ativos.

---

## 6. Ativar médico

```http
PATCH /api/medicos/{id}/ativar
```

### Resposta

```http
204 No Content
```

---

# Especialidades

## 7. Listar especialidades

```http
GET /api/especialidades
```

### Filtros

```http
GET /api/especialidades?nome=Cardio&ativo=true
```

### Resposta

```json
[
  {
    "id": 2,
    "nome": "Cardiologia",
    "descricao": "Especialidade relacionada ao sistema cardiovascular.",
    "ativo": true
  }
]
```

---

## 8. Cadastrar especialidade

```http
POST /api/especialidades
```

### Perfil

```text
ADMIN
```

### Requisição

```json
{
  "nome": "Cardiologia",
  "descricao": "Especialidade relacionada ao sistema cardiovascular."
}
```

### Regras

- Nome obrigatório;
- nome único sem diferenciação indevida de caixa;
- descrição opcional.

### Resposta

```http
201 Created
```

---

## 9. Atualizar especialidade

```http
PUT /api/especialidades/{id}
```

### Perfil

```text
ADMIN
```

---

## 10. Desativar especialidade

```http
PATCH /api/especialidades/{id}/desativar
```

### Regras

- Não permitir novos vínculos;
- preservar vínculos antigos;
- impedir desativação quando for necessário para agendamentos futuros ativos.

---

## 11. Ativar especialidade

```http
PATCH /api/especialidades/{id}/ativar
```