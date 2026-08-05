# Clínica Médica — Frontend

Interface web do Sistema de Gestão para Clínica Médica.

## Tecnologias

- React;
- TypeScript;
- Vite;
- React Router;
- Axios;
- TanStack Query;
- React Hook Form;
- Zod;
- Vitest;
- React Testing Library.

## Requisitos

- Node.js 20.19 ou superior;
- npm;
- backend executando na porta 8080.

## Instalar dependências

```bash
npm install
```

## Executar em desenvolvimento

```bash
npm run dev
```

A aplicação estará disponível em:

```text
http://localhost:5173
```

## Executar os testes

```bash
npm run test
```

## Executar o lint

```bash
npm run lint
```

## Gerar o build

```bash
npm run build
```

## Variáveis de ambiente

Crie um arquivo `.env` baseado em:

```text
.env.example
```

Exemplo:

```env
VITE_API_URL=/api
```

## Comunicação com o backend

Durante o desenvolvimento, o Vite encaminha as chamadas iniciadas por `/api` para:

```text
http://localhost:8080
```

Endpoint inicial:

```text
GET /api/health
```