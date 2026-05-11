package com.example.data.mapper

import com.example.data.remote.dto.MovieDto
import com.example.data.remote.dto.MoviesDto
import com.example.domain.model.Genre
import com.example.domain.model.Movie

/**
 * Created by Ahmad Pahmi on May 2026
 */

fun MovieDto.toDomain(): Movie {
    return Movie(
        id = id.orEmpty(),
        title = title.orEmpty(),
        plot = plot.orEmpty(),
        posterUrl = poster.orEmpty(),
        releaseDate = released.orEmpty(),
        rating = imdbRating.orEmpty(),
        genres = genre.mapGenre(),
        runtime = runtime.orEmpty(),
        language = language.mapGenre()
    )
}

fun String?.mapGenre(): List<Genre> {
    val values = this?.split(",").orEmpty()
    if (values.isEmpty()) {
        return emptyList()
    }

    return values.map {
        Genre(id = it.hashCode(), name = it)
    }
    /*   if (this?.isEmpty() == true) {
           return emptyList()
       } else {
       }*/
    /* val safeId = id ?: return null
     val safeName = name ?: return null
     return Genre(id = safeId, name = safeName)*/
}

fun MoviesDto.toDomain(): List<Movie> {
    return this.data?.map { it.toDomain() }.orEmpty()
    /* return MoviesPage(
         movies = data?.map { it.toDomain() } ?: emptyList(),
         *//*        currentPage = page ?: 1,
                totalPages = totalPages ?: 1,
                totalResults = totalResults ?: 0*//*
    )*/
}

/**
 * Constructs a full image URL from a relative path.
 * Adjust the base URL as needed for your API.
 */
/*
private fun buildImageUrl(path: String): String {
    return if (path.startsWith("http")) path else "https://image.tmdb.org/t/p/w500$path"
}*/
