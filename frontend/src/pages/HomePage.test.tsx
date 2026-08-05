import {
  QueryClient,
  QueryClientProvider,
} from '@tanstack/react-query';
import {
  render,
  screen,
} from '@testing-library/react';
import {
  afterEach,
  describe,
  expect,
  it,
  vi,
} from 'vitest';

import { HomePage } from './HomePage';

vi.mock('../features/health/HealthStatus', () => ({
  HealthStatus: () => <div>Backend conectado</div>,
}));

describe('HomePage', () => {
  afterEach(() => {
    vi.clearAllMocks();
  });

  it('should display the main system information', () => {
    const queryClient = new QueryClient({
      defaultOptions: {
        queries: {
          retry: false,
        },
      },
    });

    render(
      <QueryClientProvider client={queryClient}>
        <HomePage />
      </QueryClientProvider>,
    );

    expect(
      screen.getByRole('heading', {
        name: /gestão integrada para clínica médica/i,
      }),
    ).toBeInTheDocument();

    expect(
      screen.getByText(/backend conectado/i),
    ).toBeInTheDocument();

    expect(
      screen.getByRole('heading', {
        name: /pacientes/i,
      }),
    ).toBeInTheDocument();
  });
});