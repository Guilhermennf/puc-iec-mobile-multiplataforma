package com.puciec.ghiblikmp.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.puciec.ghiblikmp.data.Film
import com.puciec.ghiblikmp.state.ListUiState
import com.puciec.ghiblikmp.ui.components.EmptyView
import com.puciec.ghiblikmp.ui.components.ErrorView
import com.puciec.ghiblikmp.ui.components.LoadingView
import com.puciec.ghiblikmp.ui.theme.Spacing

/**
 * Features 1 (lista), 3 (busca) e 4 (filtro por diretor) + estados carregando/vazio/erro.
 * Stateless: recebe o [state] pronto e devolve eventos. Nada de fetch aqui.
 */
@Composable
fun ListScreen(
    state: ListUiState,
    favoriteIds: Set<String>,
    onQueryChange: (String) -> Unit,
    onDirectorSelected: (String?) -> Unit,
    onRetry: () -> Unit,
    onOpenFilm: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize()) {
        OutlinedTextField(
            value = state.query,
            onValueChange = onQueryChange,
            label = { Text("Buscar filme") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.lg, vertical = Spacing.sm)
                .testTag("search-input"),
        )
        if (state.directors.isNotEmpty()) {
            DirectorChips(state.directors, state.selectedDirector, onDirectorSelected)
        }
        Box(Modifier.weight(1f).fillMaxWidth()) {
            val error = state.error
            when {
                state.loading -> LoadingView()
                error != null -> ErrorView(error, onRetry)
                state.isEmpty -> EmptyView("Nenhum filme encontrado.")
                else -> FilmGrid(state.visible, favoriteIds, onOpenFilm)
            }
        }
    }
}

@Composable
private fun DirectorChips(
    directors: List<String>,
    selected: String?,
    onSelected: (String?) -> Unit,
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = Spacing.lg),
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        item {
            FilterChip(
                selected = selected == null,
                onClick = { onSelected(null) },
                label = { Text("Todos") },
                modifier = Modifier.testTag("category-chip-all"),
            )
        }
        items(directors) { director ->
            FilterChip(
                selected = director == selected,
                onClick = { onSelected(director) },
                label = { Text(director) },
                modifier = Modifier.testTag("category-chip-${slug(director)}"),
            )
        }
    }
}

@Composable
private fun FilmGrid(
    films: List<Film>,
    favoriteIds: Set<String>,
    onOpenFilm: (String) -> Unit,
) {
    // Adaptive: mais colunas em paisagem/tablet sem código extra.
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        contentPadding = PaddingValues(Spacing.lg),
        horizontalArrangement = Arrangement.spacedBy(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
        modifier = Modifier.fillMaxSize().testTag("item-list-screen"),
    ) {
        items(films, key = { it.id }) { film ->
            FilmCard(
                film = film,
                isFavorite = film.id in favoriteIds,
                onClick = { onOpenFilm(film.id) },
            )
        }
    }
}

@Composable
private fun FilmCard(film: Film, isFavorite: Boolean, onClick: () -> Unit) {
    val placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("item-card-${film.id}")
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Box {
            AsyncImage(
                model = film.image,
                contentDescription = "Pôster de ${film.title}",
                contentScale = ContentScale.Crop,
                placeholder = placeholder,
                error = placeholder,
                modifier = Modifier.fillMaxWidth().aspectRatio(2f / 3f),
            )
            if (isFavorite) {
                Text(
                    text = "♥",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(Spacing.sm)
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f), CircleShape)
                        .padding(Spacing.sm),
                )
            }
        }
        Column(Modifier.padding(Spacing.md)) {
            Text(
                text = film.title,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "${film.releaseDate} · ${film.director}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

/** "Isao Takahata" -> "isao-takahata" (usado só nos testTags dos chips). */
private fun slug(name: String): String =
    name.lowercase().replace(Regex("[^a-z0-9]+"), "-").trim('-')
