package com.puciec.ghiblikmp.state

import com.puciec.ghiblikmp.data.Film
import com.puciec.ghiblikmp.data.FilmRepository
import com.puciec.ghiblikmp.data.FavoritesRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

const val DETAIL_ERROR_MESSAGE =
    "Não foi possível carregar este filme. Verifique sua conexão e tente novamente."

data class DetailUiState(
    val loading: Boolean = true,
    val film: Film? = null,
    val error: String? = null,
)

/** Feature 2 (detalhe) + o botão de favoritar da feature 5. Um holder por filme aberto. */
class DetailStateHolder(
    private val filmId: String,
    private val repository: FilmRepository,
    private val favorites: FavoritesRepository,
    private val scope: CoroutineScope,
) {
    private val _state = MutableStateFlow(DetailUiState())
    val state: StateFlow<DetailUiState> = _state.asStateFlow()

    val isFavorite: StateFlow<Boolean> = favorites.ids
        .map { filmId in it }
        .stateIn(scope, SharingStarted.Eagerly, favorites.isFavorite(filmId))

    init {
        load()
    }

    fun load() {
        _state.value = DetailUiState(loading = true)
        scope.launch {
            try {
                _state.value = DetailUiState(loading = false, film = repository.film(filmId))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.value = DetailUiState(loading = false, error = DETAIL_ERROR_MESSAGE)
            }
        }
    }

    fun toggleFavorite() = favorites.toggle(filmId)
}
