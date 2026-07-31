# Diagrama do Banco de Dados

## Diagrama lógico simplificado

```mermaid
erDiagram
    USUARIOS {
        BIGINT id PK
        VARCHAR nome
        VARCHAR email UK
        VARCHAR senha
        VARCHAR perfil
        BOOLEAN ativo
        TIMESTAMPTZ criado_em
        TIMESTAMPTZ atualizado_em
    }

    REFRESH_TOKENS {
        BIGINT id PK
        BIGINT usuario_id FK
        VARCHAR token_hash UK
        TIMESTAMPTZ expira_em
        TIMESTAMPTZ revogado_em
        TIMESTAMPTZ criado_em
    }

    PACIENTES {
        BIGINT id PK
        VARCHAR nome_completo
        CHAR cpf UK
        DATE data_nascimento
        VARCHAR telefone
        VARCHAR email
        BOOLEAN ativo
        TIMESTAMPTZ criado_em
        TIMESTAMPTZ atualizado_em
    }

    MEDICOS {
        BIGINT id PK
        BIGINT usuario_id FK
        VARCHAR nome_completo
        VARCHAR crm_numero
        CHAR crm_uf
        SMALLINT duracao_consulta_minutos
        BOOLEAN ativo
    }

    ESPECIALIDADES {
        BIGINT id PK
        VARCHAR nome UK
        VARCHAR descricao
        BOOLEAN ativo
    }

    MEDICOS_ESPECIALIDADES {
        BIGINT medico_id PK, FK
        BIGINT especialidade_id PK, FK
        BOOLEAN principal
        TIMESTAMPTZ criado_em
    }

    AGENDAS_MEDICAS {
        BIGINT id PK
        BIGINT medico_id FK
        SMALLINT dia_semana
        TIME hora_inicio
        TIME hora_fim
        TIME intervalo_inicio
        TIME intervalo_fim
        SMALLINT duracao_consulta_minutos
        DATE vigencia_inicio
        DATE vigencia_fim
        BOOLEAN ativo
    }

    BLOQUEIOS_AGENDA {
        BIGINT id PK
        BIGINT medico_id FK
        TIMESTAMPTZ inicio_em
        TIMESTAMPTZ fim_em
        VARCHAR tipo
        VARCHAR motivo
        TIMESTAMPTZ cancelado_em
    }

    AGENDAMENTOS {
        BIGINT id PK
        BIGINT paciente_id FK
        BIGINT medico_id FK
        BIGINT especialidade_id FK
        BIGINT agendamento_origem_id FK
        TIMESTAMPTZ inicio_em
        TIMESTAMPTZ fim_em
        VARCHAR status
        VARCHAR tipo_atendimento
        VARCHAR motivo_consulta
        TIMESTAMPTZ criado_em
    }

    HISTORICOS_AGENDAMENTO {
        BIGINT id PK
        BIGINT agendamento_id FK
        VARCHAR status_anterior
        VARCHAR status_novo
        TIMESTAMPTZ alterado_em
        BIGINT alterado_por
    }

    CONSULTAS {
        BIGINT id PK
        BIGINT agendamento_id FK, UK
        BIGINT paciente_id FK
        BIGINT medico_id FK
        TIMESTAMPTZ iniciada_em
        TIMESTAMPTZ finalizada_em
        VARCHAR status
        TEXT diagnostico
        TEXT conduta
    }

    PRONTUARIOS {
        BIGINT id PK
        BIGINT paciente_id FK, UK
        VARCHAR tipo_sanguineo
        TEXT historico_familiar
        TEXT doencas_preexistentes
        TEXT medicamentos_em_uso
    }

    ALERGIAS {
        BIGINT id PK
        BIGINT prontuario_id FK
        VARCHAR substancia
        VARCHAR reacao
        VARCHAR gravidade
        BOOLEAN ativo
    }

    RECEITAS {
        BIGINT id PK
        BIGINT consulta_id FK
        BIGINT paciente_id FK
        BIGINT medico_id FK
        TIMESTAMPTZ emitida_em
        VARCHAR status
    }

    ITENS_RECEITA {
        BIGINT id PK
        BIGINT receita_id FK
        VARCHAR medicamento
        VARCHAR dosagem
        VARCHAR frequencia
        VARCHAR duracao
        SMALLINT ordem
    }

    ATESTADOS {
        BIGINT id PK
        BIGINT consulta_id FK
        BIGINT paciente_id FK
        BIGINT medico_id FK
        DATE data_inicio
        SMALLINT quantidade_dias
        VARCHAR cid
        BOOLEAN autorizacao_cid
        VARCHAR status
    }

    AUDITORIAS {
        BIGINT id PK
        BIGINT usuario_id FK
        VARCHAR acao
        VARCHAR entidade
        BIGINT entidade_id
        JSONB dados_anteriores
        JSONB dados_novos
        TIMESTAMPTZ ocorrido_em
    }

    USUARIOS ||--o{ REFRESH_TOKENS : possui
    USUARIOS ||--o| MEDICOS : representa
    USUARIOS ||--o{ AUDITORIAS : realiza

    MEDICOS ||--o{ MEDICOS_ESPECIALIDADES : possui
    ESPECIALIDADES ||--o{ MEDICOS_ESPECIALIDADES : pertence

    MEDICOS ||--o{ AGENDAS_MEDICAS : configura
    MEDICOS ||--o{ BLOQUEIOS_AGENDA : possui

    PACIENTES ||--o{ AGENDAMENTOS : agenda
    MEDICOS ||--o{ AGENDAMENTOS : atende
    ESPECIALIDADES ||--o{ AGENDAMENTOS : classifica

    AGENDAMENTOS ||--o{ HISTORICOS_AGENDAMENTO : registra
    AGENDAMENTOS ||--o| CONSULTAS : gera
    AGENDAMENTOS o|--o{ AGENDAMENTOS : origina

    PACIENTES ||--|| PRONTUARIOS : possui
    PRONTUARIOS ||--o{ ALERGIAS : registra

    PACIENTES ||--o{ CONSULTAS : recebe
    MEDICOS ||--o{ CONSULTAS : realiza

    CONSULTAS ||--o{ RECEITAS : gera
    RECEITAS ||--|{ ITENS_RECEITA : contem

    CONSULTAS ||--o{ ATESTADOS : gera

    PACIENTES ||--o{ RECEITAS : recebe
    MEDICOS ||--o{ RECEITAS : emite

    PACIENTES ||--o{ ATESTADOS : recebe
    MEDICOS ||--o{ ATESTADOS : emite
```

## Observações

Este diagrama representa a estrutura lógica inicial do MVP.

Alguns campos foram omitidos para melhorar a visualização.

A documentação completa está disponível em:

```text
docs/04-database-modeling.md
```