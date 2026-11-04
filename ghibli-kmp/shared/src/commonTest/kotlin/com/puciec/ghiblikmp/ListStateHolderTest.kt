package com.puciec.ghiblikmp

import com.puciec.ghiblikmp.data.DefaultFilmRepository
import com.puciec.ghiblikmp.state.LIST_ERROR_MESSAGE
import com.puciec.ghiblikmp.state.ListStateHolder
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Features 1 (lista + carregando/erro), 3 (busca) e 4 (filtro por diretor). */
class ListStateHolderTest {

    @Test
    fun startsLoadingThenShowsFilmsAndDirectors() = runTest {
        val holder = ListStateHolder(DefaultFilmRepository(FakeGhibliApi()), backgroundScope)
        assertTrue(holder.state.value.loading)

        advanceUntilIdle()

        val state = holder.state.value
        assertFalse(state.loading)
        assertNull(state.error)
        assertEquals(4, state.films.size)
        assertEquals(listOf(MIYAZAKI, TAKAHATA), state.directors)
    }

    @Test
    fun showsErrorWhenOfflineAndRecoversOnRetry() = runTest {
        val api = FakeGhibliApi().apply { failure = RuntimeException("offline") }
        val holder = ListStateHolder(DefaultFilmRepository(api), backgroundScope)

        advanceUntilIdle()
        assertEquals(LIST_ERROR_MESSAGE, holder.state.value.error)
        assertFalse(holder.state.value.loading)

        api.failure = null
        holder.retry()
        advanceUntilIdle()

        assertNull(holder.state.value.error)
        assertEquals(4, holder.state.value.films.size)
    }

    @Test
    fun selectingDirectorUsesApiParameterAndKeepsChips() = runTest {
        val api = FakeGhibliApi()
        val holder = ListStateHolder(DefaultFilmRepository(api), backgroundScope)
        advanceUntilIdle()

        holder.onDirectorSelected(TAKAHATA)
        advanceUntilIdle()

        val state = holder.state.value
        assertEquals(listOf("Grave of the Fireflies", "Pom Poko"), state.films.map { it.title })
        assertEquals(listOf(MIYAZAKI, TAKAHATA), state.directors) // chips não somem ao filtrar
        assertEquals(listOf(null, TAKAHATA), api.listCalls) // 2ª chamada com o parâmetro da API
    }

    @Test
    fun searchNarrowsVisibleFilmsAndReportsEmpty() = runTest {
        val holder = ListStateHolder(DefaultFilmRepository(FakeGhibliApi()), backgroundScope)
        advanceUntilIdle()

        holder.onQueryChange("kiki")
        assertEquals(listOf("Kiki's Delivery Service"), holder.state.value.visible.map { it.title })
        assertFalse(holder.state.value.isEmpty)

        holder.onQueryChange("zzz")
        assertTrue(holder.state.value.isEmpty)
    }

    @Test
    fun selectingAllAfterADirectorLoadsEveryFilmAgain() = runTest {
        val holder = ListStateHolder(DefaultFilmRepository(FakeGhibliApi()), backgroundScope)
        advanceUntilIdle()
        holder.onDirectorSelected(MIYAZAKI)
        advanceUntilIdle()
        assertEquals(2, holder.state.value.films.size)

        holder.onDirectorSelected(null)
        advanceUntilIdle()

        assertEquals(4, holder.state.value.films.size)
        assertNull(holder.state.value.selectedDirector)
    }
}
