// src/hooks/useOfflineStatus.ts
// ✅ AVALIATIVO — TODO 5
//
// Implemente este hook pra rastrear o status de conexão do browser.
//
// O que fazer:
//   1. useState com valor inicial = navigator.onLine  (true = online)
//   2. useEffect: adicione dois listeners em window:
//        window.addEventListener('online',  () => setIsOnline(true))
//        window.addEventListener('offline', () => setIsOnline(false))
//      Retorne uma função de cleanup que remove esses listeners.
//   3. Retorne { isOnline }
//
// Como testar manualmente:
//   DevTools → Network → Throttling → "Offline"
//   O banner "📡 Offline — exibindo dados do cache" deve aparecer na aba Favoritos.

import { useState, useEffect } from 'react';

export function useOfflineStatus(): { isOnline: boolean } {
  const [isOnline, setIsOnline] = useState(() => navigator.onLine);

  useEffect(() => {
    const handleOnline = () => setIsOnline(true);
    const handleOffline = () => setIsOnline(false);

    window.addEventListener('online', handleOnline);
    window.addEventListener('offline', handleOffline);

    return () => {
      window.removeEventListener('online', handleOnline);
      window.removeEventListener('offline', handleOffline);
    };
  }, []);

  return { isOnline };
}
