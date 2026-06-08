package com.example.data.mapper

import com.example.core.extension.orFalse
import com.example.core.extension.orZero
import com.example.data.remote.dto.MoviesDto
import com.example.domain.entity.CachedMovieEntity
import com.example.domain.model.Movie

/**
 * Created by Ahmad Pahmi on May 2026
 */

object MovieMapper {
    fun MoviesDto.MovieItemDto.toCachedMovieEntity(): CachedMovieEntity {
        return CachedMovieEntity(
            id = id.orZero(),
            title = title.orEmpty(),
            originalTitle = originalTitle.orEmpty(),
            originalLanguage = originalLanguage.orEmpty(),
            overview = overview.orEmpty(),
            releaseDate = releaseDate.orEmpty(),
            genreIds = genreIds.orEmpty(),
            popularity = popularity.orZero(),
            voteAverage = voteAverage.orZero(),
            voteCount = voteCount.orZero(),
            posterPath = posterPath.orEmpty(),
            backdropPath = backdropPath.orEmpty(),
            isAdult = adult.orFalse(),
            includeVideo = video.orFalse()
        )
    }

    fun CachedMovieEntity.toMovie(): Movie {
        return Movie(
            id = id,
            title = title,
            originalTitle = originalTitle,
            originalLanguage = originalLanguage,
            overview = overview,
            releaseDate = releaseDate,
            genreIds = genreIds,
            popularity = popularity,
            voteAverage = voteAverage,
            voteCount = voteCount,
            posterPath = getPosterUrl(posterPath),
            backdropPath = getBackdropUrl(backdropPath),
            isAdult = isAdult,
            includeVideo = includeVideo
        )
    }

    private fun getPosterUrl(path: String?): String =
        if (path.isNullOrEmpty()) "" else "$IMAGE_BASE_URL_POSTER$path"

    private fun getBackdropUrl(path: String?): String =
        if (path.isNullOrEmpty()) "" else "$IMAGE_BASE_URL_BACKDROP$path"

    const val IMAGE_BASE_URL_POSTER = "https://image.tmdb.org/t/p/w185/"
    const val IMAGE_BASE_URL_BACKDROP = "https://image.tmdb.org/t/p/w1280"
}
