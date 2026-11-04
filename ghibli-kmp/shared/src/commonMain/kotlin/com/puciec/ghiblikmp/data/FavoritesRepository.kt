package com.puciec.ghiblikmp.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Persistência crua dos ids favoritos. Implementada por plataforma
 * (Android: SharedPreferences, ver `SharedPrefsFavoritesStore` no androidApp).
 */
interface FavoritesStore {
    fun load(): Set<String>
    fun save(ids: Set<String>)
}

/**
 * Fonte única da verdade dos favoritos: lê do [FavoritesStore] ao iniciar (é isso que faz os
 * favoritos sobreviverem a fechar o app) e grava a cada [toggle]. Quem precisa reagir
 * (tela de detalhe, aba Favoritos, corações da lista) observa [ids].
 */
class FavoritesRepository(private val store: FavoritesStore) {
    private val _ids = MutableStateFlow(store.load())
    val ids: StateFlow<Set<String>> = _ids.asStateFlow()

    fun isFavorite(id: String): Boolean = id in _ids.value

    fun toggle(id: String) {
        val current = _ids.value
        val next = if (id in current) current - id else current + id
        store.save(next)
        _ids.value = next
    }
}
