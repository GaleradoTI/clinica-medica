import { Link } from 'react-router';

export function NotFoundPage() {
  return (
    <div className="page-container not-found-page">
      <span className="eyebrow">Erro 404</span>

      <h1>Página não encontrada</h1>

      <p>
        O endereço informado não existe ou foi alterado.
      </p>

      <Link className="primary-link" to="/">
        Voltar para o início
      </Link>
    </div>
  );
}