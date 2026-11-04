# Studio Ghibli — app mobile em Kotlin Multiplatform (Compose)

Projeto Final em Grupo — *Arquitetura de Aplicações Móveis e Multiplataforma* (PUC).
Catálogo dos filmes do Studio Ghibli consumindo a [Ghibli API](https://ghibliapi.vercel.app/)
(pública, **sem chave**): 22 filmes, 7 diretores, pôsteres e sinopses reais.

**Grupo:** _nome 1_ · _nome 2_ · _nome 3_ · _nome 4_ &nbsp;|&nbsp; **Decisões de arquitetura:** [`ADR.md`](./ADR.md) &nbsp;|&nbsp; **Quem fez o quê:** [`TAREFAS.md`](./TAREFAS.md)

## O que o app faz

| # | Feature | Como funciona |
|---|---|---|
| 1 | **Lista** de dados reais | Grade de pôsteres vinda de `GET /films` |
| 2 | **Detalhe** | Toque no pôster → banner, diretor, ano, duração, nota e sinopse |
| 3 | **Busca** | Campo de texto filtra a lista por título (sem diferenciar maiúsculas) |
| 4 | **Filtro / categoria** | Chips por diretor (gerados dos dados); usa o parâmetro real da API `?director=` |
| 5 | **Favoritos** que sobrevivem a fechar o app | ♡/♥ no detalhe; aba **Favoritos**; ids salvos em SharedPreferences |

Em todas as telas com dados: **carregando**, **vazio** (ex.: busca sem resultado) e **erro/sem internet**
com botão *Tentar de novo* — nunca tela em branco. Tema **claro e escuro** automático.

## Screenshots

> TODO do grupo: mínimo de 3 (lista, detalhe, favoritos; ideal incluir o tema escuro e um estado de erro).
> Salvem em `docs/screenshots/` e atualizem os links abaixo.

| Lista | Detalhe | Favoritos |
|---|---|---|
| ![Lista](docs/screenshots/01-lista.png) | ![Detalhe](docs/screenshots/02-detalhe.png) | ![Favoritos](docs/screenshots/03-favoritos.png) |

## Como rodar

Pré-requisitos: **JDK 17+** e **Android SDK 35** (o Android Studio instala os dois). Emulador ou celular com
Android 7.0+ (API 24+) e internet.

```bash
# a partir da raiz do repositório
cd ghibli-kmp
```

**Pelo Android Studio:** abra **esta pasta** (`ghibli-kmp/`) → *Sync Gradle* → selecione `androidApp` → **Run** ▶.

**Pela linha de comando** (no Windows use `gradlew.bat`):

```bash
./gradlew :androidApp:assembleDebug     # gera o APK em androidApp/build/outputs/apk/debug/
./gradlew :shared:testDebugUnitTest     # roda os testes unitários (JVM, sem emulador)
```

O GitHub Actions ([`ghibli-kmp-build.yml`](../.github/workflows/ghibli-kmp-build.yml)) roda esses dois comandos
a cada push; o selo verde significa "builda e testa".

## Arquitetura (3 camadas, sem fetch dentro de tela)

```
shared/  (Kotlin puro — sem Android)                 androidApp/  (Android + Compose)
┌──────────────────────────────────────────┐        ┌────────────────────────────────────┐
│ data/   Film · GhibliApi (Ktor)          │        │ ui/        telas STATELESS         │
│         FilmRepository (cache)           │◄───────│            (recebem estado + eventos)│
│         FavoritesRepository + Store(if.) │        │ ui/theme   cores/espaçamento        │
│ state/  *StateHolder (StateFlow)         │        │ platform/  SharedPrefsFavoritesStore│
│         FilmFilters (busca/categorias)   │        │ AppContainer  injeção manual        │
└──────────────────────────────────────────┘        └────────────────────────────────────┘
```

Fluxo de dados (unidirecional): **UI** dispara evento → **StateHolder** pede dados ao **Repository** →
publica um novo `UiState` em `StateFlow` → a **UI** só redesenha. Os porquês e as alternativas
descartadas estão no [`ADR.md`](./ADR.md).

## Testes

`shared/src/commonTest/` — 17 casos sobre o que o grupo criou: busca e categorias
(`FilmFiltersTest`), estados carregando/erro/retry e filtro por diretor (`ListStateHolderTest`),
favoritos persistidos entre "aberturas" do app (`FavoritesTest`), detalhe e cache do repositório
(`DetailAndRepositoryTest`). A API e o armazenamento são substituídos por *fakes* (`TestFixtures.kt`).

Opcional: os fluxos [Maestro](https://docs.maestro.dev/) em `flows/` conferem as 5 features no emulador
(`maestro test flows/` com o app instalado).
