package com.puciec.ghiblikmp.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val BASE_URL = "https://ghibliapi.vercel.app"

/** Fonte de dados remota. Interface para que o repositório seja testável com um fake. */
interface GhibliApi {
    /** `GET /films` — com [director], usa o parâmetro real da API: `GET /films?director=...`. */
    suspend fun fetchFilms(director: String? = null): List<Film>

    /** `GET /films/{id}`. */
    suspend fun fetchFilm(id: String): Film
}

class KtorGhibliApi : GhibliApi {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    override suspend fun fetchFilms(director: String?): List<Film> =
        client.get("$BASE_URL/films") {
            if (director != null) parameter("director", director)
        }.body()

    override suspend fun fetchFilm(id: String): Film =
        client.get("$BASE_URL/films/$id").body()
}
