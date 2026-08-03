# API de Autenticação

## 1. Login

```http
POST /api/auth/login
```

### Acesso

Público.

### Requisição

```json
{
  "email": "admin@clinica.com",
  "senha": "SenhaSegura123"
}
```

### Validações

- E-mail obrigatório;
- formato de e-mail válido;
- senha obrigatória;
- usuário deve existir;
- usuário deve estar ativo;
- senha deve corresponder ao hash armazenado.

### Resposta — `200 OK`

```json
{
  "accessToken": "jwt-access-token",
  "tokenType": "Bearer",
  "expiresIn": 900,
  "usuario": {
    "id": 1,
    "nome": "Administrador",
    "email": "admin@clinica.com",
    "perfil": "ADMIN"
  }
}
```

O refresh token deverá ser enviado preferencialmente por cookie seguro:

```http
Set-Cookie: refreshToken=...; HttpOnly; Secure; SameSite=Strict
```

### Erros

| Status | Situação |
|---:|---|
| `400` | Campos inválidos |
| `401` | Credenciais inválidas |
| `403` | Usuário inativo |
| `429` | Muitas tentativas, quando implementado |

A API não deverá informar se o problema ocorreu no e-mail ou na senha.

---

## 2. Renovar sessão

```http
POST /api/auth/refresh
```

### Acesso

Refresh token válido.

### Requisição

O refresh token será recebido por cookie `HttpOnly`.

Não haverá corpo obrigatório.

### Resposta — `200 OK`

```json
{
  "accessToken": "novo-jwt-access-token",
  "tokenType": "Bearer",
  "expiresIn": 900
}
```

### Erros

| Status | Situação |
|---:|---|
| `401` | Token ausente, inválido ou expirado |
| `403` | Token revogado ou usuário inativo |

---

## 3. Logout

```http
POST /api/auth/logout
```

### Acesso

Usuário autenticado.

### Resposta

```http
204 No Content
```

### Regras

- Revogar o refresh token;
- remover o cookie;
- manter registro de auditoria;
- não exigir alteração do access token já emitido;
- o access token deixará de funcionar ao expirar.

---

## 4. Consultar usuário autenticado

```http
GET /api/auth/me
```

### Acesso

Usuário autenticado.

### Resposta — `200 OK`

```json
{
  "id": 1,
  "nome": "Administrador",
  "email": "admin@clinica.com",
  "perfil": "ADMIN",
  "ativo": true,
  "medicoId": null
}
```

Para médico:

```json
{
  "id": 8,
  "nome": "Dra. Ana Souza",
  "email": "ana@clinica.com",
  "perfil": "MEDICO",
  "ativo": true,
  "medicoId": 4
}
```

---

## 5. Alterar a própria senha

```http
PATCH /api/auth/change-password
```

### Acesso

Usuário autenticado.

### Requisição

```json
{
  "senhaAtual": "SenhaAtual123",
  "novaSenha": "NovaSenhaSegura456",
  "confirmacaoNovaSenha": "NovaSenhaSegura456"
}
```

### Regras

- A senha atual deverá estar correta;
- nova senha e confirmação deverão ser iguais;
- a nova senha deverá atender aos critérios mínimos;
- a nova senha não poderá ser igual à atual;
- refresh tokens anteriores poderão ser revogados.

### Resposta

```http
204 No Content
```

### Erros

| Status | Situação |
|---:|---|
| `400` | Campos inválidos |
| `401` | Senha atual incorreta |
| `422` | Nova senha não atende às regras |