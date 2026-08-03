# API de Usuários

## Perfis autorizados

Somente:

```text
ADMIN
```

---

## 1. Listar usuários

```http
GET /api/usuarios
```

### Filtros

```http
GET /api/usuarios?nome=Ana&perfil=MEDICO&ativo=true&page=0&size=20
```

| Parâmetro | Tipo |
|---|---|
| `nome` | Texto |
| `email` | Texto |
| `perfil` | Enum |
| `ativo` | Boolean |
| `page` | Número |
| `size` | Número |
| `sort` | Texto |

### Resposta

```json
{
  "content": [
    {
      "id": 5,
      "nome": "Ana Souza",
      "email": "ana@clinica.com",
      "perfil": "MEDICO",
      "ativo": true,
      "ultimoLoginEm": "2026-07-31T08:30:00-03:00",
      "criadoEm": "2026-07-10T10:00:00-03:00"
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

---

## 2. Buscar usuário por ID

```http
GET /api/usuarios/{id}
```

### Resposta

```json
{
  "id": 5,
  "nome": "Ana Souza",
  "email": "ana@clinica.com",
  "perfil": "MEDICO",
  "ativo": true,
  "ultimoLoginEm": "2026-07-31T08:30:00-03:00",
  "criadoEm": "2026-07-10T10:00:00-03:00",
  "atualizadoEm": null
}
```

---

## 3. Cadastrar usuário

```http
POST /api/usuarios
```

### Requisição

```json
{
  "nome": "Carla Almeida",
  "email": "carla@clinica.com",
  "senhaTemporaria": "SenhaInicial123",
  "perfil": "RECEPCIONISTA"
}
```

### Validações

- Nome obrigatório;
- e-mail obrigatório e válido;
- e-mail único;
- senha temporária obrigatória;
- perfil válido;
- não permitir perfil não reconhecido.

### Resposta — `201 Created`

```json
{
  "id": 10,
  "nome": "Carla Almeida",
  "email": "carla@clinica.com",
  "perfil": "RECEPCIONISTA",
  "ativo": true,
  "criadoEm": "2026-07-31T14:00:00-03:00"
}
```

---

## 4. Atualizar usuário

```http
PUT /api/usuarios/{id}
```

### Requisição

```json
{
  "nome": "Carla de Almeida",
  "email": "carla.almeida@clinica.com",
  "perfil": "RECEPCIONISTA"
}
```

### Regras

- Não atualizar senha por este endpoint;
- e-mail deverá continuar único;
- não permitir alteração inválida de perfil;
- alterações deverão ser auditadas.

### Resposta — `200 OK`

Retorna o usuário atualizado.

---

## 5. Redefinir senha

```http
PATCH /api/usuarios/{id}/reset-password
```

### Requisição

```json
{
  "novaSenhaTemporaria": "NovaSenhaInicial123"
}
```

### Resposta

```http
204 No Content
```

---

## 6. Desativar usuário

```http
PATCH /api/usuarios/{id}/desativar
```

### Resposta

```http
204 No Content
```

### Regras

- Usuário já inativo deverá retornar `422`;
- tokens ativos deverão ser revogados;
- não apagar o histórico;
- o administrador não deverá desativar a própria conta;
- poderá ser impedida a desativação do último administrador ativo.

---

## 7. Ativar usuário

```http
PATCH /api/usuarios/{id}/ativar
```

### Resposta

```http
204 No Content
```

### Regras

- Usuário já ativo deverá retornar `422`;
- o e-mail deverá continuar válido e único.