package com.example.domain.model

/**
 * Created by Ahmad Pahmi on May 2026
 */

/**
 * The pure domain model for a Movie.
 *
 * Architectural Decision: Domain models are plain Kotlin data classes.
 * They have NO Android imports, NO Retrofit annotations, NO Room annotations.
 * This makes them reusable across platforms (e.g., KMM) and trivially testable.
 */

data class Movie(
    val id: Int,
    val title: String,
    val originalTitle: String,
    val originalLanguage: String,
    val overview: String,
    val releaseDate: String,
    val genreIds: List<Int>,
    val popularity: Double,
    val voteAverage: Double,
    val voteCount: Int,
    val posterPath: String,
    val backdropPath: String,
    val isAdult: Boolean,
    val includeVideo: Boolean
)