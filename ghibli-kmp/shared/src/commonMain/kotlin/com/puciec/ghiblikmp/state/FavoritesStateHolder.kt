package com.puciec.ghiblikmp.state

import com.puciec.ghiblikmp.data.Film
import com.puciec.ghiblikmp.data.FilmRepository
import com.puciec.ghiblikmp.data.FavoritesRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val FAVORITES_ERROR_MESSAGE =
    "Não foi possível carregar seus favoritos. Verifique sua conexão e tente novamente."

data class FavoritesUiState(
    val loading: Boolean = true,
    val films: List<Film> = emptyList(),
    val error: String? = null,
) {
    val isEmpty: Boolean get() = !loading && error == null && films.isEmpty()
}

/**
 * Feature 5 — aba Favoritos. Reage a mudanças nos ids (favoritar/desfavoritar em qualquer tela)
 * e resolve cada id em um [Film] via repositório (cache primeiro, rede se precisar).
 */
class FavoritesStateHolder(
    private val repository: FilmRepository,
    private val favorites: FavoritesRepository,
    private val scope: CoroutineScope,
) {
    private val _state = MutableStateFlow(FavoritesUiState())
    val state: StateFlow<FavoritesUiState> = _state.asStateFlow()

    private var job: Job? = null

    init {
        observe()
    }

    fun retry() = observe()

    private fun observe() {
        job?.cancel()
        job = scope.launch {
            favorites.ids.collectLatest { ids -> load(ids) }
        }
    }

    private suspend fun load(ids: Set<String>) {
        if (ids.isEmpty()) {
            _state.value = FavoritesUiState(loading = false)
            return
        }
        // Só mostra "carregando" se ainda não há nada na tela (evita piscar ao desfavoritar).
        _state.update { it.copy(loading = it.films.isEmpty(), error = null) }
        try {
            val films = coroutineScope { ids.map { id -> async { repository.film(id) } }.awaitAll() }
            _state.value = FavoritesUiState(loading = false, films = films.sortedBy { it.title })
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            _state.update { it.copy(loading = false, error = FAVORITES_ERROR_MESSAGE) }
        }
    }
}
