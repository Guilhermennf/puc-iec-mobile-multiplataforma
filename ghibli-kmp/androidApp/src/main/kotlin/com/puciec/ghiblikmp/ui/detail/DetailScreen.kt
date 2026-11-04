package com.puciec.ghiblikmp.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import coil.compose.AsyncImage
import com.puciec.ghiblikmp.data.Film
import com.puciec.ghiblikmp.state.DetailUiState
import com.puciec.ghiblikmp.ui.components.ErrorView
import com.puciec.ghiblikmp.ui.components.LoadingView
import com.puciec.ghiblikmp.ui.theme.Spacing

/** Feature 2 (detalhe) + botão de favoritar (feature 5). Stateless. */
@Composable
fun DetailScreen(
    state: DetailUiState,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize().testTag("detail-screen")) {
        TextButton(
            onClick = onBack,
            modifier = Modifier.padding(horizontal = Spacing.sm).testTag("detail-back-button"),
        ) {
            Text("← Voltar")
        }
        val error = state.error
        val film = state.film
        when {
            state.loading -> LoadingView()
            error != null -> ErrorView(error, onRetry)
            film != null -> FilmDetailContent(film, isFavorite, onToggleFavorite)
            else -> Unit
        }
    }
}

@Composable
private fun FilmDetailContent(film: Film, isFavorite: Boolean, onToggleFavorite: () -> Unit) {
    val placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant)
    val favoriteLabel = if (isFavorite) "Remover dos favoritos" else "Adicionar aos favoritos"

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        AsyncImage(
            model = film.movieBanner.ifBlank { film.image },
            contentDescription = "Banner de ${film.title}",
            contentScale = ContentScale.Crop,
            placeholder = placeholder,
            error = placeholder,
            modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f),
        )
        Column(Modifier.padding(Spacing.lg)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = film.title,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.weight(1f).testTag("detail-title"),
                )
                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier
                        .testTag("detail-favorite-button")
                        .semantics { contentDescription = favoriteLabel },
                ) {
                    Text(
                        text = if (isFavorite) "♥" else "♡",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
            Text(
                text = "Dirigido por ${film.director} · ${film.releaseDate} · ${film.runningTime} min",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (film.rtScore.isNotBlank()) {
                Text(
                    text = "Rotten Tomatoes: ${film.rtScore}%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
            Spacer(Modifier.height(Spacing.lg))
            Text(text = film.description, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
