# API de Agenda e Agendamentos

# Agenda médica

## 1. Listar configurações de agenda

```http
GET /api/medicos/{medicoId}/agendas
```

### Perfis

```text
ADMIN
RECEPCIONISTA
MEDICO responsável
```

### Resposta

```json
[
  {
    "id": 1,
    "diaSemana": 1,
    "horaInicio": "08:00:00",
    "horaFim": "12:00:00",
    "intervaloInicio": "10:00:00",
    "intervaloFim": "10:15:00",
    "duracaoConsultaMinutos": 30,
    "vigenciaInicio": "2026-08-01",
    "vigenciaFim": null,
    "ativo": true
  }
]
```

---

## 2. Criar configuração de agenda

```http
POST /api/medicos/{medicoId}/agendas
```

### Perfis

```text
ADMIN
```

### Requisição

```json
{
  "diaSemana": 1,
  "horaInicio": "08:00:00",
  "horaFim": "12:00:00",
  "intervaloInicio": "10:00:00",
  "intervaloFim": "10:15:00",
  "duracaoConsultaMinutos": 30,
  "vigenciaInicio": "2026-08-01",
  "vigenciaFim": null
}
```

### Regras

- Médico ativo;
- dia entre 1 e 7;
- início anterior ao fim;
- intervalo dentro do expediente;
- não permitir sobreposição;
- duração maior que zero.

---

## 3. Atualizar configuração

```http
PUT /api/medicos/{medicoId}/agendas/{agendaId}
```

---

## 4. Desativar configuração

```http
PATCH /api/medicos/{medicoId}/agendas/{agendaId}/desativar
```

---

# Bloqueios

## 5. Listar bloqueios

```http
GET /api/medicos/{medicoId}/bloqueios
```

### Filtros

```http
GET /api/medicos/4/bloqueios?dataInicio=2026-08-01&dataFim=2026-08-31
```

---

## 6. Criar bloqueio

```http
POST /api/medicos/{medicoId}/bloqueios
```

### Requisição

```json
{
  "inicioEm": "2026-08-15T08:00:00-03:00",
  "fimEm": "2026-08-15T12:00:00-03:00",
  "tipo": "AUSENCIA",
  "motivo": "Compromisso profissional"
}
```

### Resposta sem conflitos

```http
201 Created
```

### Quando existirem agendamentos no período

```http
409 Conflict
```

Exemplo:

```json
{
  "timestamp": "2026-07-31T14:30:00-03:00",
  "status": 409,
  "error": "SCHEDULE_BLOCK_CONFLICT",
  "message": "Existem agendamentos ativos no período informado.",
  "path": "/api/medicos/4/bloqueios",
  "details": {
    "agendamentosConflitantes": [40, 41]
  }
}
```

---

## 7. Cancelar bloqueio

```http
PATCH /api/medicos/{medicoId}/bloqueios/{bloqueioId}/cancelar
```

---

# Disponibilidade

## 8. Consultar horários disponíveis

```http
GET /api/medicos/{medicoId}/disponibilidade
```

### Exemplo

```http
GET /api/medicos/4/disponibilidade?data=2026-08-10&especialidadeId=2
```

### Resposta

```json
{
  "medicoId": 4,
  "data": "2026-08-10",
  "duracaoConsultaMinutos": 30,
  "horarios": [
    {
      "inicioEm": "2026-08-10T08:00:00-03:00",
      "fimEm": "2026-08-10T08:30:00-03:00"
    },
    {
      "inicioEm": "2026-08-10T08:30:00-03:00",
      "fimEm": "2026-08-10T09:00:00-03:00"
    }
  ]
}
```

---

# Agendamentos

## 9. Listar agendamentos

```http
GET /api/agendamentos
```

### Filtros

```http
GET /api/agendamentos?medicoId=4&pacienteId=15&status=AGENDADO&dataInicio=2026-08-01&dataFim=2026-08-31
```

### Resposta

```json
{
  "content": [
    {
      "id": 40,
      "paciente": {
        "id": 15,
        "nomeCompleto": "Maria da Silva"
      },
      "medico": {
        "id": 4,
        "nomeCompleto": "Dra. Ana Souza"
      },
      "especialidade": {
        "id": 2,
        "nome": "Cardiologia"
      },
      "inicioEm": "2026-08-10T09:00:00-03:00",
      "fimEm": "2026-08-10T09:30:00-03:00",
      "status": "AGENDADO",
      "tipoAtendimento": "PRESENCIAL"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1,
  "totalPages": 1
}
```

---

## 10. Buscar agendamento

```http
GET /api/agendamentos/{id}
```

---

## 11. Criar agendamento

```http
POST /api/agendamentos
```

### Perfis

```text
ADMIN
RECEPCIONISTA
```

### Requisição

```json
{
  "pacienteId": 15,
  "medicoId": 4,
  "especialidadeId": 2,
  "inicioEm": "2026-08-10T09:00:00-03:00",
  "tipoAtendimento": "PRESENCIAL",
  "motivoConsulta": "Avaliação cardiológica",
  "observacoes": null
}
```

O backend calculará `fimEm` com base na duração configurada.

### Regras

- Paciente ativo;
- médico ativo;
- especialidade ativa e vinculada;
- horário dentro da agenda;
- ausência de bloqueio;
- ausência de conflito;
- data futura;
- paciente não pode possuir outro agendamento conflitante.

### Resposta

```http
201 Created
```

---

## 12. Confirmar agendamento

```http
PATCH /api/agendamentos/{id}/confirmar
```

### Transição permitida

```text
AGENDADO → CONFIRMADO
```

### Resposta

```http
204 No Content
```

---

## 13. Informar presença

```http
PATCH /api/agendamentos/{id}/informar-presenca
```

### Transições permitidas

```text
AGENDADO → PACIENTE_PRESENTE
CONFIRMADO → PACIENTE_PRESENTE
```

---

## 14. Cancelar agendamento

```http
PATCH /api/agendamentos/{id}/cancelar
```

### Requisição

```json
{
  "motivo": "Paciente solicitou cancelamento."
}
```

### Regras

- Motivo obrigatório;
- não permitir cancelamento de consulta concluída;
- registrar histórico;
- liberar o horário.

---

## 15. Informar não comparecimento

```http
PATCH /api/agendamentos/{id}/nao-compareceu
```

### Requisição

```json
{
  "observacao": "Paciente não compareceu até o limite definido."
}
```

---

## 16. Reagendar

```http
POST /api/agendamentos/{id}/reagendar
```

### Requisição

```json
{
  "novoInicioEm": "2026-08-12T10:00:00-03:00",
  "motivo": "Solicitação do paciente."
}
```

### Comportamento

- Validar o novo horário;
- preservar o agendamento original;
- cancelar o original com indicação de reagendamento;
- criar um novo agendamento;
- preencher `agendamentoOrigemId`;
- registrar histórico.

### Resposta — `201 Created`

Retorna o novo agendamento.

---

## 17. Histórico do agendamento

```http
GET /api/agendamentos/{id}/historico
```

### Resposta

```json
[
  {
    "id": 100,
    "statusAnterior": "AGENDADO",
    "statusNovo": "CONFIRMADO",
    "motivo": null,
    "alteradoEm": "2026-08-08T15:00:00-03:00",
    "alteradoPor": {
      "id": 3,
      "nome": "Carla Almeida"
    }
  }
]
```