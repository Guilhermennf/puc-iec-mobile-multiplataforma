package com.puciec.ghiblikmp.state

import com.puciec.ghiblikmp.data.Film
import com.puciec.ghiblikmp.data.FilmRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val LIST_ERROR_MESSAGE =
    "Não foi possível carregar os filmes. Verifique sua conexão e tente novamente."

/** Estado completo da tela de lista. A UI só desenha isto — não decide nada. */
data class ListUiState(
    val loading: Boolean = true,
    val error: String? = null,
    /** Filmes carregados para o diretor selecionado (ou todos). */
    val films: List<Film> = emptyList(),
    val directors: List<String> = emptyList(),
    val selectedDirector: String? = null,
    val query: String = "",
) {
    /** O que aparece de fato: filmes carregados + busca por texto. */
    val visible: List<Film> get() = filterByQuery(films, query)

    /** Carregou sem erro, mas não há nada para mostrar (busca sem resultado, por exemplo). */
    val isEmpty: Boolean get() = !loading && error == null && visible.isEmpty()
}

/**
 * Features 1 (lista), 3 (busca) e 4 (filtro por diretor).
 * [scope] é injetado: no app é o escopo da aplicação, nos testes é o `backgroundScope` do `runTest`.
 */
class ListStateHolder(
    private val repository: FilmRepository,
    private val scope: CoroutineScope,
) {
    private val _state = MutableStateFlow(ListUiState())
    val state: StateFlow<ListUiState> = _state.asStateFlow()

    private var job: Job? = null

    init {
        fetch(director = null)
    }

    /** "Tentar de novo" depois de um erro, mantendo o filtro de diretor escolhido. */
    fun retry() = fetch(_state.value.selectedDirector)

    fun onQueryChange(query: String) {
        _state.update { it.copy(query = query) }
    }

    /** `null` = "Todos". Usa o parâmetro real da API (`?director=`), não filtra no cliente. */
    fun onDirectorSelected(director: String?) {
        if (director != _state.value.selectedDirector) fetch(director)
    }

    private fun fetch(director: String?) {
        job?.cancel()
        _state.update { it.copy(loading = true, error = null, selectedDirector = director) }
        job = scope.launch {
            try {
                val films = repository.films(director)
                _state.update { current ->
                    current.copy(
                        loading = false,
                        films = films,
                        // Os chips vêm da lista completa; ao filtrar, mantém os que já existem.
                        directors = if (director == null) directorsOf(films) else current.directors,
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.update { it.copy(loading = false, error = LIST_ERROR_MESSAGE) }
            }
        }
    }
}
