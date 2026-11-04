package com.puciec.ghiblikmp.ui.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.puciec.ghiblikmp.data.Film
import com.puciec.ghiblikmp.state.FavoritesUiState
import com.puciec.ghiblikmp.ui.components.EmptyView
import com.puciec.ghiblikmp.ui.components.ErrorView
import com.puciec.ghiblikmp.ui.components.LoadingView
import com.puciec.ghiblikmp.ui.theme.Spacing

/** Feature 5 — aba Favoritos (ids persistidos, filmes resolvidos pelo repositório). Stateless. */
@Composable
fun FavoritesScreen(
    state: FavoritesUiState,
    onOpenFilm: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier.fillMaxSize()) {
        val error = state.error
        when {
            state.loading -> LoadingView()
            error != null -> ErrorView(error, onRetry)
            state.isEmpty -> EmptyView("Nenhum favorito ainda.\nToque em ♡ no detalhe de um filme.")
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(Spacing.lg),
                verticalArrangement = Arrangement.spacedBy(Spacing.md),
            ) {
                items(state.films, key = { it.id }) { film ->
                    FavoriteRow(film, onClick = { onOpenFilm(film.id) })
                }
            }
        }
    }
}

@Composable
private fun FavoriteRow(film: Film, onClick: () -> Unit) {
    val placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("favorite-item-${film.id}")
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = film.image,
                contentDescription = "Pôster de ${film.title}",
                contentScale = ContentScale.Crop,
                placeholder = placeholder,
                error = placeholder,
                modifier = Modifier.width(72.dp).aspectRatio(2f / 3f),
            )
            Column(Modifier.padding(Spacing.md)) {
                Text(text = film.title, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "${film.director} · ${film.releaseDate}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
