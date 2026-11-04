package com.puciec.ghiblikmp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Filme do Studio Ghibli, exatamente como a API devolve em `/films` e `/films/{id}`.
 *
 * O `id` é UUID (String). `release_date`, `running_time` e `rt_score` vêm como String na API.
 * Campos que não usamos (people, species, locations, vehicles, url) são ignorados
 * (`ignoreUnknownKeys` no cliente) e os usados têm default para não quebrar se a API omitir algum.
 */
@Serializable
data class Film(
    val id: String,
    val title: String,
    @SerialName("original_title_romanised") val originalTitleRomanised: String = "",
    val image: String = "",
    @SerialName("movie_banner") val movieBanner: String = "",
    val description: String = "",
    val director: String = "",
    val producer: String = "",
    @SerialName("release_date") val releaseDate: String = "",
    @SerialName("running_time") val runningTime: String = "",
    @SerialName("rt_score") val rtScore: String = "",
)
