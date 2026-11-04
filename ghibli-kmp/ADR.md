# ADR — Arquitetura do app Studio Ghibli (KMP + Compose)

**Status:** rascunho para revisão do grupo · **Data:** 2026-11-04 · **Decisores:** _nomes do grupo_

> Este ADR só vale se refletir o que o grupo **decidiu e consegue defender na demo**. Revisem cada seção,
> troquem o que não concordarem (e o código junto) e atualizem o status para *Aceito*.

## Contexto

App mobile com 5 features (lista, detalhe, busca, filtro por categoria, favoritos persistidos) e estados de
carregando/vazio/erro, sobre a Ghibli API (22 filmes, sem chave). Grupo de 3 a 4 pessoas, prazo curto (Aula 6),
e o professor **builda o projeto na máquina dele**: o que não builda zera Funcionalidade. Isso favorece
decisões simples, com poucas dependências.

## Decisões

### 1. Framework: Kotlin Multiplatform + Compose (partindo do skeleton da disciplina)
- **Por quê:** a lógica de dados e de estado fica em **Kotlin puro** no módulo `shared` (sem `android.*`), o que a
  torna **testável na JVM, sem emulador**, e reaproveitável no iOS no futuro. Compose é declarativo e já tem
  Material 3. _[Confirmar com o grupo: já usamos KMP na Atividade 5.]_
- **Descartado — Flutter:** boa opção de UI, mas exigiria reescrever tudo em Dart e perderíamos o reuso do `shared`.
- **Descartado — React Native:** idem (TypeScript); o ganho de ecossistema web não compensa recomeçar o que já tínhamos em Kotlin.
- **Custo assumido:** Gradle é lento e mais frágil que `flutter run`; por isso há CI que builda a cada push.
  A **UI hoje é só Android** (Compose no `androidApp`); migrar a UI para `shared` (Compose Multiplatform) seria o
  próximo passo para iOS.

### 2. Estado: *state holders* em Kotlin puro + `StateFlow` (fluxo unidirecional)
- **Como:** `ListStateHolder`, `DetailStateHolder`, `FavoritesStateHolder` expõem um `UiState` imutável em
  `StateFlow`; as telas são **stateless** (recebem estado, devolvem eventos). O escopo de coroutines é injetado.
- **Por quê:** separa UI de lógica (exigência do enunciado: "sem fetch dentro do componente") e permite testar
  carregando → sucesso → erro → retry com `runTest`, sem Android.
- **Descartado — estado e `LaunchedEffect` dentro dos composables (como no skeleton):** mistura rede com UI, não dá
  para testar sem rodar a tela e perde a busca/filtro ao girar o aparelho.
- **Descartado — `androidx.lifecycle.ViewModel`:** é o padrão no Android, mas amarraria a lógica ao Android e
  adicionaria dependência. Nossos holders vivem na `Application` (`AppContainer`): sobrevivem à rotação, mas **não** à
  morte do processo pelo sistema — aceitável aqui porque a lista é recarregada e os favoritos estão em disco.
- **Descartado — MVI/Redux (Orbit, MVIKotlin etc.):** cerimônia demais para 3 telas.

### 3. Rede: Ktor Client + kotlinx.serialization
- **Por quê:** multiplataforma (Retrofit é só JVM/Android) e roda em `commonMain`, junto com o resto do `shared`.
- **A API fica atrás de uma interface (`GhibliApi`)** → o repositório e os holders são testados com um *fake*.

### 4. Persistência dos favoritos: SharedPreferences atrás de `FavoritesStore`
- **Por quê:** o que persistimos é só um conjunto de ids — não justifica banco. A interface `FavoritesStore`
  isola a escolha: trocar por DataStore ou multiplatform-settings não mexe em estado nem em UI.
- **Descartado — Room/SQLDelight:** excesso para um `Set<String>`.
- **Descartado — DataStore:** melhor tecnicamente (assíncrono), mas mais código e dependência sem ganho visível hoje.
- **Limitação conhecida:** guardamos **só ids**. Abrir *Favoritos* offline com cache frio mostra o estado de erro
  (com "Tentar de novo"). Guardar um *snapshot* dos filmes resolveria — fica como melhoria.

### 5. Filtro e busca
- **Filtro por diretor usa o parâmetro real da API** (`GET /films?director=…`; verificado em 2026-11-04:
  `Isao Takahata` → 5 filmes). Os chips são **derivados dos dados** (7 diretores), não uma lista fixa no código.
- **Busca é no cliente** (22 itens): filtra o que já foi carregado, sem nova requisição.

### 6. Imagens: Coil 2 (`coil-compose`)
- Cache em memória/disco e placeholder prontos. Coil 3 foi descartado por ser multiplataforma/mais complexo de
  configurar do que precisamos enquanto a UI é só Android.

## Consequências
- ✅ Lógica coberta por 17 testes unitários rápidos; UI trocável sem tocar na regra.
- ✅ Poucas dependências → menor risco de o professor não conseguir buildar.
- ⚠️ Estado dos holders se perde se o processo for encerrado pelo sistema (ver decisão 2).
- ⚠️ UI só Android; iOS exigiria migrar telas para `shared`.
