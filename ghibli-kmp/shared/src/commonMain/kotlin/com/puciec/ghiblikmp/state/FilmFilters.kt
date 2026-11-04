package com.puciec.ghiblikmp.state

import com.puciec.ghiblikmp.data.Film

/** Feature 3 — busca: substring case-insensitive no título (ou no título romanizado). Vazio = tudo. */
fun filterByQuery(films: List<Film>, query: String): List<Film> {
    val q = query.trim()
    if (q.isEmpty()) return films
    return films.filter {
        it.title.contains(q, ignoreCase = true) ||
            it.originalTitleRomanised.contains(q, ignoreCase = true)
    }
}

/** Feature 4 — categorias: diretores distintos (ordenados) presentes na lista. Viram os chips. */
fun directorsOf(films: List<Film>): List<String> =
    films.map { it.director }.filter { it.isNotBlank() }.distinct().sorted()
