# ENTREGA — Atividade 4 (PWA)

> Relatório para finalizar com as observações e capturas feitas no navegador.

**Nome:** Guilherme Nunes · **GitHub:** Guilhermennf

---

## 1. Site comum × PWA — o que eu vi

Comparei o PWA publicado pelo professor (`https://pwa-tmdb-aula4.vercel.app`) com o site comum (`https://www.themoviedb.org`). Observei as diferenças abaixo; para o teste offline, carreguei os sites primeiro e selecionei **Offline** em DevTools → Network.

| # | Site comum | PWA | Print/evidência |
|---|---|---|---|
| 1 | O TMDB abriu como site tradicional, com cabeçalho, busca e áreas como tendências. | O PWA mostrou uma interface de filmes com navegação inferior Início, Buscar e Favoritos, além do aviso “App pronto para uso offline”. | [Site comum online](evidencias/03-site-comum-online.png) · [PWA online](evidencias/01-demo-pwa-online.png) |
| 2 | Ao recarregar sem conexão, o navegador exibiu `ERR_INTERNET_DISCONNECTED`. | Após recarregar com Network em Offline, o PWA continuou mostrando os filmes; a chamada de filmes aparece servida pelo cache de disco. | [Site comum offline](evidencias/11-site-comum-offline.png) · [PWA recarregado offline](evidencias/10-demo-pwa-recarregado-offline.png) |
| 3 | O site comum não manteve a página disponível após a recarga offline. | Na captura offline do PWA, os títulos e descrições continuam, enquanto os pôsteres externos não carregam. | [Erro do site comum](evidencias/11-site-comum-offline.png) · [PWA offline](evidencias/02-demo-pwa-offline.png) |

## 2. Prova do offline no meu build

- [x] DevTools → Application → Cache Storage: [cache da API `tmdb-api`, com 3 entradas](evidencias/04-cache-storage.png) · [cache de pôsteres `tmdb-images`, com 48 imagens](evidencias/04b-cache-storage-images.jpg).
- [x] Build local em modo offline: [Network em Offline e lista renderizada](evidencias/08-app-offline-network.png) · [estado Offline com dados do IndexedDB](evidencias/07-meu-build-offline.png) · [Favoritos com banner offline](evidencias/09-favoritos-offline.png).
- [x] Aviso de atualização: [“Nova versão disponível”, com Atualizar e Depois](evidencias/05-nova-versao.png). Esta captura está recortada ao aviso.

Na captura do Cache Storage, `tmdb-api` contém 3 respostas e `tmdb-images` contém 48 imagens JPEG de `image.tmdb.org`. A lista de filmes também foi conferida online no build local em [06](evidencias/06-meu-build-online.png).

### Índice das evidências

| # | O que o print mostra |
|---|---|
| 01 | PWA do professor online, com lista de filmes e aviso de uso offline. |
| 02 | PWA do professor offline: títulos e descrições permanecem, pôsteres externos não carregam. |
| 03 | Site comum TMDB online, com cabeçalho, busca e tendências. |
| 04 | DevTools → Application → Cache Storage, cache `tmdb-api`, 3 entradas. |
| 04b | DevTools → Application → Cache Storage, cache `tmdb-images`, 48 imagens. |
| 05 | Aviso de nova versão com as ações Atualizar e Depois; captura recortada. |
| 06 | Build local online, com pôsteres e lista de filmes. |
| 07 | Build local em estado Offline, exibindo dados do IndexedDB. |
| 08 | Build local com Network em Offline e interface de filmes ainda renderizada. |
| 09 | Aba Favoritos do build local com o banner de offline. |
| 10 | PWA do professor após recarga offline; a chamada da API foi servida do cache de disco. |
| 11 | Site comum após recarga offline, com `ERR_INTERNET_DISCONNECTED`. |

## 3. Reflexão (5–8 linhas)

- Para checkout, escolheria `prompt` para manter a mesma versão durante o pagamento.
- `autoUpdate` poderia recarregar a página no meio da transação e perder o estado da compra.
- Se o usuário adiar o aviso por muito tempo, pode continuar em uma versão antiga com defeitos.
- Para notícias, escolheria `autoUpdate` para entregar artigos e correções mais recentes rapidamente.
- Com `prompt`, o leitor pode permanecer em uma versão desatualizada até aceitar a atualização.
- `autoUpdate` também pode recarregar a página durante a leitura e perder a posição do leitor.

## 4. Declaração de uso de IA

Usei o Codex para implementar a busca de filmes, o cache do Service Worker, o aviso de atualização, a aba de favoritos e o estado de conexão. Também usei IA para revisar a implementação e completar os testes unitários. Rodei `npm test` e `npm run build` para verificar o projeto.
