package com.puciec.ghiblikmp

import android.content.Context
import com.puciec.ghiblikmp.data.DefaultFilmRepository
import com.puciec.ghiblikmp.data.FavoritesRepository
import com.puciec.ghiblikmp.data.FilmRepository
import com.puciec.ghiblikmp.data.KtorGhibliApi
import com.puciec.ghiblikmp.platform.SharedPrefsFavoritesStore
import com.puciec.ghiblikmp.state.DetailStateHolder
import com.puciec.ghiblikmp.state.FavoritesStateHolder
import com.puciec.ghiblikmp.state.ListStateHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Injeção de dependência manual: o único lugar que sabe como as camadas se ligam
 * (API → repositório → state holders). Vive na `Application`, então o estado da lista
 * (busca, filtro) sobrevive a rotação de tela.
 */
class AppContainer(context: Context) {
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    val filmRepository: FilmRepository = DefaultFilmRepository(KtorGhibliApi())
    val favoritesRepository = FavoritesRepository(SharedPrefsFavoritesStore(context))

    val listStateHolder = ListStateHolder(filmRepository, appScope)
    val favoritesStateHolder = FavoritesStateHolder(filmRepository, favoritesRepository, appScope)

    /** O detalhe é por filme: quem abre a tela passa o escopo (cancelado ao sair da tela). */
    fun newDetailStateHolder(filmId: String, scope: CoroutineScope) =
        DetailStateHolder(filmId, filmRepository, favoritesRepository, scope)
}
