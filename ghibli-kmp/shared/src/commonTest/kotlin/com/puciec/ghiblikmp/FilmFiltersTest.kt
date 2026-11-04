package com.puciec.ghiblikmp

import com.puciec.ghiblikmp.state.directorsOf
import com.puciec.ghiblikmp.state.filterByQuery
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Features 3 (busca) e 4 (categorias): lógica pura, sem rede nem coroutines. */
class FilmFiltersTest {

    @Test
    fun blankQueryReturnsAllFilms() {
        assertEquals(sampleFilms, filterByQuery(sampleFilms, ""))
        assertEquals(sampleFilms, filterByQuery(sampleFilms, "   "))
    }

    @Test
    fun queryIsCaseInsensitiveSubstringOfTitle() {
        val result = filterByQuery(sampleFilms, "KIKI")
        assertEquals(listOf("Kiki's Delivery Service"), result.map { it.title })
    }

    @Test
    fun queryWithoutMatchReturnsEmptyList() {
        assertTrue(filterByQuery(sampleFilms, "zzz").isEmpty())
    }

    @Test
    fun directorsAreDistinctAndSorted() {
        assertEquals(listOf(MIYAZAKI, TAKAHATA), directorsOf(sampleFilms))
    }
}
