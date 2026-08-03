# API de Consultas e Prontuários

# Consultas

## 1. Iniciar consulta

```http
POST /api/agendamentos/{agendamentoId}/consulta
```

### Perfil

```text
MEDICO responsável
```

### Regras

- Médico autenticado deve ser o responsável;
- agendamento deve existir;
- paciente deve estar presente;
- status deve ser `PACIENTE_PRESENTE`;
- não poderá existir outra consulta para o agendamento.

### Resposta — `201 Created`

```json
{
  "id": 20,
  "agendamentoId": 40,
  "pacienteId": 15,
  "medicoId": 4,
  "iniciadaEm": "2026-08-10T09:05:00-03:00",
  "finalizadaEm": null,
  "status": "EM_ATENDIMENTO"
}
```

O agendamento será atualizado para:

```text
EM_ATENDIMENTO
```

---

## 2. Buscar consulta

```http
GET /api/consultas/{id}
```

### Perfis

```text
MEDICO autorizado
```

### Resposta

```json
{
  "id": 20,
  "agendamentoId": 40,
  "paciente": {
    "id": 15,
    "nomeCompleto": "Maria da Silva",
    "dataNascimento": "1990-05-10"
  },
  "medico": {
    "id": 4,
    "nomeCompleto": "Dra. Ana Souza"
  },
  "iniciadaEm": "2026-08-10T09:05:00-03:00",
  "finalizadaEm": null,
  "status": "EM_ATENDIMENTO",
  "queixaPrincipal": null,
  "historiaDoencaAtual": null,
  "avaliacao": null,
  "diagnostico": null,
  "conduta": null,
  "observacoes": null
}
```

---

## 3. Atualizar atendimento em andamento

```http
PUT /api/consultas/{id}
```

### Requisição

```json
{
  "queixaPrincipal": "Dor no peito durante esforço.",
  "historiaDoencaAtual": "Sintomas iniciados há aproximadamente uma semana.",
  "avaliacao": "Paciente estável durante a avaliação.",
  "diagnostico": "Diagnóstico clínico em avaliação.",
  "conduta": "Solicitação de exames complementares.",
  "observacoes": null
}
```

### Regras

- Consulta deve estar `EM_ATENDIMENTO`;
- somente médico responsável;
- campos clínicos não poderão ser atualizados pela recepção;
- alterações poderão ser salvas durante o atendimento.

---

## 4. Finalizar consulta

```http
PATCH /api/consultas/{id}/finalizar
```

### Requisição

```json
{
  "queixaPrincipal": "Dor no peito durante esforço.",
  "historiaDoencaAtual": "Sintomas iniciados há aproximadamente uma semana.",
  "avaliacao": "Paciente estável durante a avaliação.",
  "diagnostico": "Diagnóstico clínico em avaliação.",
  "conduta": "Solicitação de exames complementares.",
  "observacoes": null
}
```

### Regras

- Consulta deve estar em atendimento;
- somente médico responsável;
- dados mínimos obrigatórios deverão ser definidos;
- registrar `finalizadaEm`;
- alterar consulta para `FINALIZADA`;
- alterar agendamento para `CONCLUIDO`;
- impedir edição direta depois da finalização.

### Resposta

```http
200 OK
```

---

## 5. Consultas do médico autenticado

```http
GET /api/consultas/minhas
```

### Filtros

```http
GET /api/consultas/minhas?dataInicio=2026-08-01&dataFim=2026-08-31&status=FINALIZADA
```

---

# Prontuários

## 6. Consultar prontuário por paciente

```http
GET /api/pacientes/{pacienteId}/prontuario
```

### Perfil

```text
MEDICO autorizado
```

### Resposta

```json
{
  "id": 10,
  "pacienteId": 15,
  "tipoSanguineo": "O+",
  "historicoFamiliar": "Histórico familiar de hipertensão.",
  "doencasPreexistentes": "Hipertensão arterial.",
  "medicamentosEmUso": "Medicamento informado pelo paciente.",
  "observacoesGerais": null,
  "alergias": [
    {
      "id": 8,
      "substancia": "Dipirona",
      "reacao": "Reação alérgica informada.",
      "gravidade": "MODERADA",
      "ativo": true
    }
  ],
  "atualizadoEm": "2026-08-10T09:20:00-03:00"
}
```

---

## 7. Criar ou atualizar prontuário

```http
PUT /api/pacientes/{pacienteId}/prontuario
```

### Requisição

```json
{
  "tipoSanguineo": "O+",
  "historicoFamiliar": "Histórico familiar de hipertensão.",
  "doencasPreexistentes": "Hipertensão arterial.",
  "medicamentosEmUso": "Medicamento informado pelo paciente.",
  "observacoesGerais": null
}
```

### Regras

- Somente médico;
- criar prontuário quando ainda não existir;
- atualizar quando já existir;
- registrar auditoria;
- impedir acesso da recepção.

---

# Alergias

## 8. Adicionar alergia

```http
POST /api/pacientes/{pacienteId}/prontuario/alergias
```

### Requisição

```json
{
  "substancia": "Dipirona",
  "reacao": "Reação alérgica informada.",
  "gravidade": "MODERADA",
  "observacoes": null
}
```

### Resposta

```http
201 Created
```

---

## 9. Atualizar alergia

```http
PUT /api/pacientes/{pacienteId}/prontuario/alergias/{alergiaId}
```

---

## 10. Inativar alergia

```http
PATCH /api/pacientes/{pacienteId}/prontuario/alergias/{alergiaId}/desativar
```

### Regras

- Preservar histórico;
- não apagar fisicamente;
- registrar médico responsável.