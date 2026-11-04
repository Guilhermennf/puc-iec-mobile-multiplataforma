package com.puciec.ghiblikmp.data

/** Contrato de acesso aos filmes. A camada de estado só conhece esta interface. */
interface FilmRepository {
    /** Sempre vai à rede (usado no carregamento inicial, no filtro e no "tentar de novo"). */
    suspend fun films(director: String? = null): List<Film>

    /** Primeiro o cache (filmes já vistos na lista), depois a rede. */
    suspend fun film(id: String): Film
}

class DefaultFilmRepository(private val api: GhibliApi) : FilmRepository {
    // Acessado só a partir do dispatcher principal (state holders), então não precisa de lock.
    private val cache = mutableMapOf<String, Film>()

    override suspend fun films(director: String?): List<Film> =
        api.fetchFilms(director).also { list -> list.forEach { cache[it.id] = it } }

    override suspend fun film(id: String): Film =
        cache[id] ?: api.fetchFilm(id).also { cache[id] = it }
}
