# ENTREGA — Atividade 4 (PWA)

> Relatório para finalizar com as observações e capturas feitas no navegador.

**Nome:** Guilherme Nunes · **GitHub:** Guilhermennf

---

## 1. Site comum × PWA — o que eu vi

Compare `https://pwa-tmdb-aula4.vercel.app` com um site comum (por exemplo, themoviedb.org): tente abrir sem conexão, feche e reabra, e instale o PWA. Preencha as diferenças que você observou.

| # | Site comum | PWA | Print/evidência |
|---|---|---|---|
| 1 | Preencher após comparar | Preencher após comparar | `evidencias/01-comparacao.png` |
| 2 | Preencher após comparar | Preencher após comparar | `evidencias/01-comparacao.png` |
| 3 | Preencher após comparar | Preencher após comparar | `evidencias/01-comparacao.png` |

## 2. Prova do offline no meu build

- [ ] Print do DevTools → Application → Cache Storage com `tmdb-api` e `tmdb-images` contendo itens.
- [ ] Print do app carregando a lista em modo Offline.
- [ ] Print de “Nova versão disponível” ou explicação de por que não foi possível reproduzir.

Coloque as capturas na pasta `evidencias/` e referencie os arquivos aqui. Para reproduzir, rode `npm run build` e `npm run preview`, abra `http://localhost:4173`, carregue filmes e pôsteres e então ative Network → Offline no DevTools.

## 3. Reflexão (5–8 linhas)

- Para checkout, escolheria `prompt` para manter a mesma versão durante o pagamento.
- `autoUpdate` poderia recarregar a página no meio da transação e perder o estado da compra.
- Se o usuário adiar o aviso por muito tempo, pode continuar em uma versão antiga com defeitos.
- Para notícias, escolheria `autoUpdate` para entregar artigos e correções mais recentes rapidamente.
- Com `prompt`, o leitor pode permanecer em uma versão desatualizada até aceitar a atualização.
- `autoUpdate` também pode recarregar a página durante a leitura e perder a posição do leitor.

## 4. Declaração de uso de IA

Usei o Codex para implementar a busca de filmes, o cache do Service Worker, o aviso de atualização, a aba de favoritos e o estado de conexão. Também usei IA para revisar a implementação e completar os testes unitários. Rodei `npm test` e `npm run build` para verificar o projeto.
