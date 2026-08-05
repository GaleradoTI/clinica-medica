import axios from 'axios';

import { env } from '../config/env';

export const api = axios.create({
  baseURL: env.apiUrl,
  timeout: 10_000,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },
});

api.interceptors.response.use(
  (response) => response,
  (error: unknown) => {
    return Promise.reject(error);
  },
);