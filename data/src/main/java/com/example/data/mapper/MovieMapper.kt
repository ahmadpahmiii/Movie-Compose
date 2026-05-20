package com.example.data.mapper

import com.example.data.remote.dto.MovieDto
import com.example.domain.model.Movie

/**
 * Created by Ahmad Pahmi on May 2026
 */

fun MovieDto.toDomain(): Movie {
    return Movie(
        id = id.orEmpty(),
        title = title.orEmpty(),
        description = plot.orEmpty(),
        posterUrl = poster.orEmpty(),
        releaseDate = released.orEmpty(),
        rating = imdbRating.orEmpty(),
        genres = genre?.split(",").orEmpty(),
        runtime = runtime.orEmpty(),
        language = language?.split(",").orEmpty()
    )
}