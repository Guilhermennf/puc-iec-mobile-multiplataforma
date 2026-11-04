package com.puciec.ghiblikmp

import com.puciec.ghiblikmp.data.DefaultFilmRepository
import com.puciec.ghiblikmp.data.FavoritesRepository
import com.puciec.ghiblikmp.state.DETAIL_ERROR_MESSAGE
import com.puciec.ghiblikmp.state.DetailStateHolder
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Feature 2 (detalhe) e o cache do repositório que evita requisições repetidas. */
class DetailAndRepositoryTest {

    @Test
    fun detailLoadsTheRequestedFilm() = runTest {
        val holder = DetailStateHolder(
            "3", DefaultFilmRepository(FakeGhibliApi()), FavoritesRepository(InMemoryFavoritesStore()), backgroundScope,
        )

        advanceUntilIdle()

        assertEquals("Kiki's Delivery Service", holder.state.value.film?.title)
        assertFalse(holder.state.value.loading)
    }

    @Test
    fun detailShowsErrorWhenOffline() = runTest {
        val api = FakeGhibliApi().apply { failure = RuntimeException("offline") }
        val holder = DetailStateHolder(
            "3", DefaultFilmRepository(api), FavoritesRepository(InMemoryFavoritesStore()), backgroundScope,
        )

        advanceUntilIdle()

        assertEquals(DETAIL_ERROR_MESSAGE, holder.state.value.error)
        assertNull(holder.state.value.film)
    }

    @Test
    fun toggleFavoriteFlipsIsFavorite() = runTest {
        val holder = DetailStateHolder(
            "3", DefaultFilmRepository(FakeGhibliApi()), FavoritesRepository(InMemoryFavoritesStore()), backgroundScope,
        )
        advanceUntilIdle()
        assertFalse(holder.isFavorite.value)

        holder.toggleFavorite()
        advanceUntilIdle()

        assertTrue(holder.isFavorite.value)
    }

    @Test
    fun repositoryServesDetailFromCacheAfterListWasLoaded() = runTest {
        val api = FakeGhibliApi()
        val repository = DefaultFilmRepository(api)

        repository.films()
        repository.film("1")

        assertTrue(api.detailCalls.isEmpty()) // já estava no cache da lista
    }

    @Test
    fun repositoryFetchesDetailOnceWhenNotCached() = runTest {
        val api = FakeGhibliApi()
        val repository = DefaultFilmRepository(api)

        repository.film("2")
        repository.film("2")

        assertEquals(listOf("2"), api.detailCalls) // 2ª leitura veio do cache
    }
}
