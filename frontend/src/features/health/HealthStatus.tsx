import { useHealth } from './useHealth';

export function HealthStatus() {
  const healthQuery = useHealth();

  if (healthQuery.isPending) {
    return (
      <section className="status-card">
        <span className="status-indicator status-loading" />
        <div>
          <strong>Verificando a API</strong>
          <p>Aguardando resposta do backend.</p>
        </div>
      </section>
    );
  }

  if (healthQuery.isError) {
    return (
      <section className="status-card">
        <span className="status-indicator status-error" />
        <div>
          <strong>Backend indisponível</strong>
          <p>Não foi possível estabelecer comunicação com a API.</p>

          <button
            type="button"
            onClick={() => void healthQuery.refetch()}
          >
            Tentar novamente
          </button>
        </div>
      </section>
    );
  }

  return (
    <section className="status-card">
      <span className="status-indicator status-success" />

      <div>
        <strong>Backend conectado</strong>
        <p>
          Aplicação: {healthQuery.data.application}
        </p>
        <p>
          Status: {healthQuery.data.status}
        </p>
      </div>
    </section>
  );
}