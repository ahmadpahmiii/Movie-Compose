package com.example.data.mapper

import com.example.data.remote.dto.GenreDto
import com.example.data.remote.dto.MovieDto
import com.example.data.remote.dto.MoviesDto
import com.example.domain.model.Genre
import com.example.domain.model.Movie
import com.example.domain.model.MoviesPage

/**
 * Created by Ahmad Pahmi on May 2026
 */

fun MovieDto.toDomain(): Movie {
    return Movie(
        id = id.orEmpty(),
        title = title.orEmpty(),
        overview = overview.orEmpty(),
        posterUrl = posterPath?.let { buildImageUrl(it) }.orEmpty(),
        backdropUrl = backdropPath?.let { buildImageUrl(it) }.orEmpty(),
        releaseDate = releaseDate.orEmpty(),
        rating = voteAverage ?: 0.0,
        voteCount = voteCount ?: 0,
        genres = genres?.mapNotNull { it.toDomain() } ?: emptyList(),
        runtime = runtime ?: 0,
        language = originalLanguage.orEmpty(),
        popularity = popularity ?: 0.0
    )
}

fun GenreDto.toDomain(): Genre? {
    val safeId = id ?: return null
    val safeName = name ?: return null
    return Genre(id = safeId, name = safeName)
}

fun MoviesDto.toDomain(): MoviesPage {
    return MoviesPage(
        movies = results?.map { it.toDomain() } ?: emptyList(),
        currentPage = page ?: 1,
        totalPages = totalPages ?: 1,
        totalResults = totalResults ?: 0
    )
}

/**
 * Constructs a full image URL from a relative path.
 * Adjust the base URL as needed for your API.
 */
private fun buildImageUrl(path: String): String {
    return if (path.startsWith("http")) path else "https://image.tmdb.org/t/p/w500$path"
}