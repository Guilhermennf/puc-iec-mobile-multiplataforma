package com.puciec.ghiblikmp.platform

import android.content.Context
import com.puciec.ghiblikmp.data.FavoritesStore

private const val PREFS_NAME = "favorites"
private const val KEY_IDS = "favorite_film_ids"

/** Implementação Android do [FavoritesStore]: os ids ficam em SharedPreferences (sobrevivem ao fechar o app). */
class SharedPrefsFavoritesStore(context: Context) : FavoritesStore {
    private val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // O Set devolvido pelo SharedPreferences não pode ser modificado: sempre copiamos.
    override fun load(): Set<String> = prefs.getStringSet(KEY_IDS, emptySet())?.toSet() ?: emptySet()

    override fun save(ids: Set<String>) {
        prefs.edit().putStringSet(KEY_IDS, ids.toSet()).apply()
    }
}
