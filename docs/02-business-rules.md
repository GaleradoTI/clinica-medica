# Regras de Negócio

## Usuários

- Todo usuário deverá possuir um perfil.
- Apenas administradores poderão criar usuários.
- Senhas deverão ser criptografadas.
- Usuários inativos não poderão acessar o sistema.

---

# Pacientes

- CPF deverá ser único.
- E-mail é opcional.
- Telefone é obrigatório.
- Exclusão será lógica.
- Histórico deverá ser preservado.

---

# Médicos

- CRM deverá ser único.
- Médico deverá possuir ao menos uma especialidade.
- Apenas administradores poderão alterar seus dados cadastrais.

---

# Especialidades

- Nome deverá ser único.
- Não poderá ser removida caso existam médicos vinculados.

---

# Agenda

- Apenas horários disponíveis poderão ser utilizados.
- Bloqueios impedirão novos agendamentos.
- Não poderá existir conflito de horários.

---

# Agendamentos

Status possíveis:

- AGENDADO
- CONFIRMADO
- PACIENTE_PRESENTE
- EM_ATENDIMENTO
- CONCLUIDO
- CANCELADO
- NAO_COMPARECEU

---

# Consultas

- Apenas médicos poderão iniciar consultas.
- Consultas finalizadas não poderão ser editadas.

---

# Prontuário

- Somente médicos poderão alterar.
- Histórico deverá permanecer permanente.

---

# Receitas

- Apenas consultas concluídas poderão gerar receitas.

---

# Atestados

- Apenas consultas concluídas poderão gerar atestados.

---

# Auditoria

Serão registradas ações como:

- Login
- Cadastro
- Atualização
- Exclusão lógica
- Cancelamentos
- Reagendamentos

---

# Exclusão

Nenhuma informação clínica será removida fisicamente.

Será utilizada exclusão lógica.