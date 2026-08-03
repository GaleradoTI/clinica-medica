# API de Receitas e Atestados

# Receitas

## 1. Listar receitas da consulta

```http
GET /api/consultas/{consultaId}/receitas
```

### Perfil

```text
MEDICO autorizado
```

---

## 2. Emitir receita

```http
POST /api/consultas/{consultaId}/receitas
```

### Requisição

```json
{
  "orientacoesGerais": "Seguir as orientações médicas.",
  "validadeAte": "2026-09-10",
  "itens": [
    {
      "medicamento": "Medicamento de exemplo",
      "dosagem": "500 mg",
      "viaAdministracao": "Oral",
      "frequencia": "A cada 8 horas",
      "duracao": "5 dias",
      "quantidade": "15 unidades",
      "orientacoes": "Administrar após alimentação.",
      "ordem": 1
    }
  ]
}
```

### Regras

- Somente médico responsável;
- consulta deve existir;
- consulta deve estar em atendimento ou finalizada, conforme regra definida;
- pelo menos um item;
- ordens positivas e não duplicadas;
- registrar conteúdo imutável após emissão.

Para o MVP, será permitido emitir durante a consulta em atendimento e após a finalização pelo médico responsável.

### Resposta — `201 Created`

```json
{
  "id": 30,
  "consultaId": 20,
  "pacienteId": 15,
  "medicoId": 4,
  "orientacoesGerais": "Seguir as orientações médicas.",
  "emitidaEm": "2026-08-10T09:25:00-03:00",
  "validadeAte": "2026-09-10",
  "status": "EMITIDA",
  "itens": [
    {
      "id": 50,
      "medicamento": "Medicamento de exemplo",
      "dosagem": "500 mg",
      "viaAdministracao": "Oral",
      "frequencia": "A cada 8 horas",
      "duracao": "5 dias",
      "quantidade": "15 unidades",
      "orientacoes": "Administrar após alimentação.",
      "ordem": 1
    }
  ]
}
```

---

## 3. Buscar receita

```http
GET /api/receitas/{id}
```

---

## 4. Cancelar receita

```http
PATCH /api/receitas/{id}/cancelar
```

### Requisição

```json
{
  "motivo": "Receita emitida com informação incorreta."
}
```

### Regras

- Somente médico responsável;
- receita deve estar emitida;
- manter registro;
- não alterar os itens;
- nova receita poderá ser emitida.

---

## 5. Visualização para impressão

```http
GET /api/receitas/{id}/impressao
```

O endpoint poderá retornar:

```text
application/pdf
```

A geração do PDF poderá ser implementada em etapa futura do MVP.

---

# Atestados

## 6. Listar atestados da consulta

```http
GET /api/consultas/{consultaId}/atestados
```

---

## 7. Emitir atestado

```http
POST /api/consultas/{consultaId}/atestados
```

### Requisição

```json
{
  "dataInicio": "2026-08-10",
  "quantidadeDias": 2,
  "texto": "O paciente necessita de afastamento por dois dias.",
  "cid": null,
  "autorizacaoCid": false
}
```

### Regras

- Somente médico responsável;
- quantidade de dias maior que zero;
- texto obrigatório;
- CID opcional;
- CID somente será exibido com autorização;
- documento ficará imutável após emissão.

### Resposta — `201 Created`

```json
{
  "id": 40,
  "consultaId": 20,
  "pacienteId": 15,
  "medicoId": 4,
  "dataInicio": "2026-08-10",
  "quantidadeDias": 2,
  "texto": "O paciente necessita de afastamento por dois dias.",
  "cid": null,
  "autorizacaoCid": false,
  "emitidoEm": "2026-08-10T09:30:00-03:00",
  "status": "EMITIDO"
}
```

---

## 8. Buscar atestado

```http
GET /api/atestados/{id}
```

---

## 9. Cancelar atestado

```http
PATCH /api/atestados/{id}/cancelar
```

### Requisição

```json
{
  "motivo": "Documento emitido com informação incorreta."
}
```

---

## 10. Visualização para impressão

```http
GET /api/atestados/{id}/impressao
```

Poderá retornar:

```text
application/pdf
```