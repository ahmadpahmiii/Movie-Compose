package com.example.data.mapper

import com.example.core.extension.orFalse
import com.example.core.extension.orZero
import com.example.data.remote.dto.MoviesDto
import com.example.domain.model.Movie

/**
 * Created by Ahmad Pahmi on May 2026
 */

object MovieMapper {
    fun MoviesDto.toMovieList(): List<Movie> {
        return this.results?.map {
            Movie(
                id = it.id.orZero(),
                title = it.title.orEmpty(),
                originalTitle = it.originalTitle.orEmpty(),
                originalLanguage = it.originalLanguage.orEmpty(),
                overview = it.overview.orEmpty(),
                releaseDate = it.releaseDate.orEmpty(),
                genreIds = it.genreIds ?: emptyList(),
                popularity = it.popularity.orZero(),
                voteAverage = it.voteAverage.orZero(),
                voteCount = it.voteCount.orZero(),
                posterPath = it.posterPath.orEmpty(),
                backdropPath = it.backdropPath.orEmpty(),
                adult = it.adult.orFalse(),
                video = it.video.orFalse()
            )
        }.orEmpty()
    }
}