import { useQuery } from '@tanstack/react-query';

import { getApplicationHealth } from './healthService';

export function useHealth() {
  return useQuery({
    queryKey: ['application-health'],
    queryFn: getApplicationHealth,
    retry: 1,
  });
}