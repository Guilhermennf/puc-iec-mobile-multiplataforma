# Divisão de tarefas e roteiro até a Aula 6 (09/11)

A **Contribuição individual** (3 pts) olha commits e PRs **de cada pessoa**. Tudo commitado por uma pessoa só =
o grupo perde esses pontos. O esqueleto já tem as camadas e as 5 features funcionando de forma básica; o trabalho
de cada um é **ser dono de uma fatia ponta a ponta**: estado + tela + testes + o que falta de UI/UX.

## 0. Primeiro (hoje) — antes de qualquer feature

- [ ] **Reservar o tema:** abrir a issue no repo da disciplina `Grupo <nomes> — Studio Ghibli + KMP`
  (vale a ordem de chegada; olhem as issues existentes antes — em 2026-11-04 ninguém tinha reservado Ghibli).
- [ ] Criar o **repositório do grupo** (fork do repo da disciplina ou repo novo), adicionar **todos** como
  *collaborators* e **`jacksonsmith`** se o repo for privado.
- [ ] **Rodar o build** (`./gradlew :androidApp:assembleDebug` e `./gradlew :shared:testDebugUnitTest`) e/ou conferir o
  Actions verde. Este esqueleto foi escrito sem poder compilar localmente: **se algo falhar, corrijam antes de tudo**
  (build quebrado = Funcionalidade zerada).
- [ ] Cada pessoa clona, cria branch `feat/<nome>-<fatia>` e abre **PR** (revisado por outra pessoa) — nada direto na `main`.

## 1. Fatias (ajustem para 3 ou 4 pessoas)

| Dono | Fatia | Onde mexer | Pontos de UI/UX e qualidade para fazer |
|---|---|---|---|
| **A** _nome_ | **Feature 1 — Lista** + estados | `state/ListStateHolder.kt` · `ui/list/ListScreen.kt` · `ui/components/StateViews.kt` | Skeleton/shimmer no lugar do spinner · pull-to-refresh · testes de estado extras |
| **B** _nome_ | **Feature 2 — Detalhe** + navegação | `state/DetailStateHolder.kt` · `ui/detail/DetailScreen.kt` · `ui/App.kt` | Layout em paisagem · transição lista→detalhe · ícones em vez de ♥/← · compartilhar |
| **C** _nome_ | **Features 3 e 4 — Busca e filtro** | `state/FilmFilters.kt` · `ListScreen` (chips/campo) | Botão "limpar" na busca · ordenar (ano/nota) · testes de borda |
| **D** _nome_ | **Feature 5 — Favoritos** + identidade | `data/FavoritesRepository.kt` · `ui/favorites/` · `ui/theme/Theme.kt` | **Ícone e splash próprios** · tipografia · conferir tema escuro · testes da persistência |

Com 3 pessoas, **C** assume também a fatia de **D** ou divida Tema/ícone entre A e B.
**Todos:** README (screenshots — mínimo 3), ADR (revisar e assumir), ensaio da apresentação.

## 2. Cronograma sugerido

| Dia | Entrega |
|---|---|
| 04/11 (qua) | Issue + repo + build/CI verde + branches criadas |
| 05–06/11 | Cada dono implementa e abre PR da sua fatia (review cruzado) |
| 07/11 (sáb) | Testes completos, UI/UX consistente (claro/escuro/paisagem/fonte grande), ícone e splash |
| 08/11 (dom) | README com screenshots, ADR *Aceito*, ensaio cronometrado, **build limpo em máquina diferente** |
| 09/11 (seg) | **Aula 6 — demonstração** (URL do repo colada no Canvas antes) |

## 3. Roteiro da demonstração (até 20 min, **incluindo perguntas**)

1. (2 min) O que é o app e qual API usa.
2. (7 min) Demo ao vivo: lista → busca → filtro por diretor → detalhe → favoritar → **fechar o app e reabrir** (favorito continua) →
   modo avião (estado de erro + *Tentar de novo*) → tema escuro.
3. (6 min) Arquitetura: as 3 camadas, por que *state holders* + `StateFlow`, o que o ADR descartou e por quê. Cada pessoa mostra **a sua fatia**.
4. (5 min) Perguntas — prevejam: "por que sem ViewModel?", "onde está o fetch?", "o que acontece se o processo morrer?",
   "como testaram sem emulador?", "quem fez o quê?".

## 4. Checklist final (dia 08)

- [ ] `git clone` num diretório novo → `./gradlew :androidApp:assembleDebug` passa
- [ ] `./gradlew :shared:testDebugUnitTest` passa (≥ 3 casos próprios — já são 17)
- [ ] README com como rodar (`cd ghibli-kmp`) + ≥ 3 screenshots + link do ADR
- [ ] ADR sem rascunho, coerente com o código
- [ ] Todos os integrantes têm commits **e** PRs no histórico
- [ ] URL do repositório colada no Canvas
