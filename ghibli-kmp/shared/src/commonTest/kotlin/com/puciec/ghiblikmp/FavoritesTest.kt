package com.puciec.ghiblikmp

import com.puciec.ghiblikmp.data.DefaultFilmRepository
import com.puciec.ghiblikmp.data.FavoritesRepository
import com.puciec.ghiblikmp.state.FavoritesStateHolder
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Feature 5 — favoritos que sobrevivem a fechar o app. */
class FavoritesTest {

    @Test
    fun toggleAddsThenRemovesAFavorite() {
        val favorites = FavoritesRepository(InMemoryFavoritesStore())

        favorites.toggle("1")
        assertTrue(favorites.isFavorite("1"))

        favorites.toggle("1")
        assertFalse(favorites.isFavorite("1"))
    }

    @Test
    fun favoritesSurviveRecreatingTheApp() {
        val store = InMemoryFavoritesStore() // o "disco": sobrevive à recriação do repositório
        FavoritesRepository(store).toggle("3")

        val afterRestart = FavoritesRepository(store)

        assertEquals(setOf("3"), afterRestart.ids.value)
    }

    @Test
    fun favoritesTabStartsEmptyThenFollowsToggles() = runTest {
        val favorites = FavoritesRepository(InMemoryFavoritesStore())
        val holder = FavoritesStateHolder(DefaultFilmRepository(FakeGhibliApi()), favorites, backgroundScope)

        advanceUntilIdle()
        assertTrue(holder.state.value.isEmpty)

        favorites.toggle("3")
        advanceUntilIdle()
        assertEquals(listOf("Kiki's Delivery Service"), holder.state.value.films.map { it.title })

        favorites.toggle("3")
        advanceUntilIdle()
        assertTrue(holder.state.value.isEmpty)
    }
}
