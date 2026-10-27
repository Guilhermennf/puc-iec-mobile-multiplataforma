import { useRegisterSW } from 'virtual:pwa-register/react';

export function UpdatePrompt() {
  const {
    offlineReady: [offlineReady, setOfflineReady],
    needRefresh: [needRefresh, setNeedRefresh],
    updateServiceWorker,
  } = useRegisterSW();

  const dismiss = () => {
    setOfflineReady(false);
    setNeedRefresh(false);
  };

  if (!offlineReady && !needRefresh) return null;

  return (
    <aside
      role="status"
      aria-live="polite"
      style={{
        position: 'fixed',
        right: 16,
        bottom: 88,
        zIndex: 1000,
        display: 'flex',
        alignItems: 'center',
        gap: 12,
        maxWidth: 'min(440px, calc(100vw - 32px))',
        padding: '14px 16px',
        borderRadius: 12,
        background: '#0d253f',
        color: '#fff',
        boxShadow: '0 8px 24px rgba(0, 0, 0, 0.3)',
      }}
    >
      <span>{needRefresh ? 'Nova versão disponível' : 'App pronto para uso offline'}</span>
      {needRefresh ? (
        <>
          <button type="button" onClick={() => void updateServiceWorker(true)}>
            Atualizar
          </button>
          <button type="button" onClick={dismiss}>
            Depois
          </button>
        </>
      ) : (
        <button type="button" onClick={dismiss} aria-label="Fechar aviso">
          Fechar
        </button>
      )}
    </aside>
  );
}
