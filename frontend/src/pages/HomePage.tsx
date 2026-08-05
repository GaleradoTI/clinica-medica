import { HealthStatus } from '../features/health/HealthStatus';

export function HomePage() {
  return (
    <div className="page-container">
      <section className="hero">
        <span className="eyebrow">Sistema em desenvolvimento</span>

        <h1>Gestão integrada para clínica médica</h1>

        <p>
          Plataforma para gerenciamento de pacientes, médicos,
          agendas, consultas, prontuários, receitas e atestados.
        </p>
      </section>

      <section className="section">
        <h2>Status da aplicação</h2>
        <HealthStatus />
      </section>

      <section className="section">
        <h2>Módulos planejados</h2>

        <div className="module-grid">
          <article className="module-card">
            <h3>Pacientes</h3>
            <p>Cadastro e gerenciamento dos pacientes.</p>
          </article>

          <article className="module-card">
            <h3>Médicos</h3>
            <p>Profissionais, especialidades e agendas.</p>
          </article>

          <article className="module-card">
            <h3>Agendamentos</h3>
            <p>Horários, confirmações e reagendamentos.</p>
          </article>

          <article className="module-card">
            <h3>Consultas</h3>
            <p>Atendimentos e informações clínicas.</p>
          </article>
        </div>
      </section>
    </div>
  );
}