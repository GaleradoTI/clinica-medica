const DEFAULT_API_URL = '/api';

export const env = {
  apiUrl: import.meta.env.VITE_API_URL || DEFAULT_API_URL,
} as const;