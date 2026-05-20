package com.example.data.mapper

import com.example.domain.entity.CachedMovieEntity
import com.example.domain.entity.RecentlyViewedEntity
import com.example.domain.entity.WishlistMovieEntity
import com.example.domain.model.Movie

/**
 * Created by Ahmad Pahmi on May 2026
 */

// ─── Movie → WishlistMovieEntity ───────────────────────────────────────────

fun Movie.toWishlistEntity(): WishlistMovieEntity = WishlistMovieEntity(
    id = id,
    title = title,
    description = description,
    posterUrl = posterUrl,
    releaseDate = releaseDate,
    rating = rating,
    genres = genres.joinToString(","),
    runtime = runtime,
    language = language.joinToString(",")
)

fun WishlistMovieEntity.toDomain(): Movie = Movie(
    id = id,
    title = title,
    description = description,
    posterUrl = posterUrl,
    releaseDate = releaseDate,
    rating = rating,
    genres = genres.split(","),
    runtime = runtime,
    language = language.split(",")
)

fun Movie.toCachedMovieEntity(): CachedMovieEntity = CachedMovieEntity(
    id = id,
    title = title,
    description = description,
    posterUrl = posterUrl,
    releaseDate = releaseDate,
    rating = rating,
    genres = genres.joinToString(","),
    runtime = runtime,
    language = language.joinToString(",")
)

fun CachedMovieEntity.toDomain(): Movie = Movie(
    id = id,
    title = title,
    description = description,
    posterUrl = posterUrl,
    releaseDate = releaseDate,
    rating = rating,
    genres = genres.split(","),
    runtime = runtime,
    language = language.split(",")
)

fun Movie.toRecentlyViewedEntity(): RecentlyViewedEntity = RecentlyViewedEntity(
    movieId = id,
    title = title,
    posterUrl = posterUrl,
    rating = rating
)
