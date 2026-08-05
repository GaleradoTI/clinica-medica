import { Link, Outlet } from 'react-router';

export function MainLayout() {
  return (
    <div className="application">
      <header className="application-header">
        <div className="header-content">
          <Link className="brand" to="/">
            Clínica Médica
          </Link>

          <nav aria-label="Navegação principal">
            <Link to="/">Início</Link>
          </nav>
        </div>
      </header>

      <main className="application-content">
        <Outlet />
      </main>

      <footer className="application-footer">
        Sistema de Gestão para Clínica Médica
      </footer>
    </div>
  );
}