package com.puciec.ghiblikmp

import com.puciec.ghiblikmp.data.FavoritesStore
import com.puciec.ghiblikmp.data.Film
import com.puciec.ghiblikmp.data.GhibliApi

const val MIYAZAKI = "Hayao Miyazaki"
const val TAKAHATA = "Isao Takahata"

fun film(id: String, title: String, director: String) =
    Film(id = id, title = title, director = director)

val sampleFilms = listOf(
    film("1", "Castle in the Sky", MIYAZAKI),
    film("2", "Grave of the Fireflies", TAKAHATA),
    film("3", "Kiki's Delivery Service", MIYAZAKI),
    film("4", "Pom Poko", TAKAHATA),
)

/** API fake: registra as chamadas e pode simular "sem internet" via [failure]. */
class FakeGhibliApi(var films: List<Film> = sampleFilms) : GhibliApi {
    var failure: Throwable? = null
    val listCalls = mutableListOf<String?>()
    val detailCalls = mutableListOf<String>()

    override suspend fun fetchFilms(director: String?): List<Film> {
        listCalls += director
        failure?.let { throw it }
        return if (director == null) films else films.filter { it.director == director }
    }

    override suspend fun fetchFilm(id: String): Film {
        detailCalls += id
        failure?.let { throw it }
        return films.first { it.id == id }
    }
}

/** Persistência em memória: reusar a mesma instância simula "fechar e reabrir o app". */
class InMemoryFavoritesStore(initial: Set<String> = emptySet()) : FavoritesStore {
    private var saved: Set<String> = initial
    override fun load(): Set<String> = saved
    override fun save(ids: Set<String>) {
        saved = ids
    }
}
