import { api } from '../../services/api';

export type HealthResponse = {
  status: string;
  application: string;
  timestamp: string;
};

export async function getApplicationHealth(): Promise<HealthResponse> {
  const response = await api.get<HealthResponse>('/health');

  return response.data;
}