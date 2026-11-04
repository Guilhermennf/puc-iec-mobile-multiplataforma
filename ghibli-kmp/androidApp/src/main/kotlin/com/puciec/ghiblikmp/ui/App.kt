package com.puciec.ghiblikmp.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.puciec.ghiblikmp.AppContainer
import com.puciec.ghiblikmp.ui.detail.DetailScreen
import com.puciec.ghiblikmp.ui.favorites.FavoritesScreen
import com.puciec.ghiblikmp.ui.list.ListScreen
import com.puciec.ghiblikmp.ui.theme.GhibliTheme

enum class Tab { List, Favorites }

/**
 * Raiz da UI. Navegação simples e à prova de rotação: a aba e o filme aberto são
 * `rememberSaveable`. O detalhe ocupa a área da aba atual; Voltar (botão ou gesto) fecha o detalhe.
 */
@Composable
fun App(container: AppContainer, modifier: Modifier = Modifier) {
    GhibliTheme {
        var tab by rememberSaveable { mutableStateOf(Tab.List) }
        var openFilmId by rememberSaveable { mutableStateOf<String?>(null) }

        BackHandler(enabled = openFilmId != null) { openFilmId = null }

        Scaffold(
            modifier = modifier,
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                BottomNav(selected = tab, onSelect = { tab = it; openFilmId = null })
            },
        ) { padding ->
            Box(Modifier.padding(padding).fillMaxSize()) {
                val filmId = openFilmId
                when {
                    filmId != null -> DetailRoute(filmId, container, onBack = { openFilmId = null })
                    tab == Tab.List -> ListRoute(container, onOpenFilm = { openFilmId = it })
                    else -> FavoritesRoute(container, onOpenFilm = { openFilmId = it })
                }
            }
        }
    }
}

// Cada "Route" liga um state holder (estado) a uma tela stateless (UI). É a única costura entre as camadas.

@Composable
private fun ListRoute(container: AppContainer, onOpenFilm: (String) -> Unit) {
    val holder = container.listStateHolder
    val state by holder.state.collectAsState()
    val favoriteIds by container.favoritesRepository.ids.collectAsState()
    ListScreen(
        state = state,
        favoriteIds = favoriteIds,
        onQueryChange = holder::onQueryChange,
        onDirectorSelected = holder::onDirectorSelected,
        onRetry = holder::retry,
        onOpenFilm = onOpenFilm,
    )
}

@Composable
private fun FavoritesRoute(container: AppContainer, onOpenFilm: (String) -> Unit) {
    val holder = container.favoritesStateHolder
    val state by holder.state.collectAsState()
    FavoritesScreen(state = state, onOpenFilm = onOpenFilm, onRetry = holder::retry)
}

@Composable
private fun DetailRoute(filmId: String, container: AppContainer, onBack: () -> Unit) {
    // Escopo atrelado à tela: cancela a busca se o usuário sair antes de terminar.
    val scope = rememberCoroutineScope()
    val holder = remember(filmId) { container.newDetailStateHolder(filmId, scope) }
    val state by holder.state.collectAsState()
    val isFavorite by holder.isFavorite.collectAsState()
    DetailScreen(
        state = state,
        isFavorite = isFavorite,
        onToggleFavorite = holder::toggleFavorite,
        onBack = onBack,
        onRetry = holder::load,
    )
}

@Composable
private fun BottomNav(selected: Tab, onSelect: (Tab) -> Unit) {
    NavigationBar {
        NavigationBarItem(
            selected = selected == Tab.List,
            onClick = { onSelect(Tab.List) },
            icon = { Text("🎬") },
            label = { Text("Filmes") },
            modifier = Modifier.testTag("tab-list"),
        )
        NavigationBarItem(
            selected = selected == Tab.Favorites,
            onClick = { onSelect(Tab.Favorites) },
            icon = { Text("♥") },
            label = { Text("Favoritos") },
            modifier = Modifier.testTag("tab-favorites"),
        )
    }
}
