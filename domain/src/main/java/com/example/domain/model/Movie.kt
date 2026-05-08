package com.example.domain.model

import java.util.Locale

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
    val id: String,
    val title: String,
    val overview: String,
    val posterUrl: String,
    val backdropUrl: String,
    val releaseDate: String,
    val rating: Double,
    val voteCount: Int,
    val genres: List<Genre>,
    val runtime: Int,
    val language: String,
    val popularity: Double
) {
    /** Derived property: formatted rating string. */
    val formattedRating: String get() = String.format(Locale.US, "%.1f", rating)

    /** Derived property: release year extracted from date. */
    val releaseYear: String get() = releaseDate.take(4)

    /** Derived property: human-readable runtime. */
    val formattedRuntime: String
        get() = if (runtime > 0) {
            val hours = runtime / 60
            val minutes = runtime % 60
            when {
                hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
                hours > 0 -> "${hours}h"
                else -> "${minutes}m"
            }
        } else ""
}

data class Genre(
    val id: Int,
    val name: String
)

/**
 * Domain model representing a paginated list of movies.
 * Pagination-ready from day one.
 */
data class MoviesPage(
    val movies: List<Movie>,
    val currentPage: Int,
    val totalPages: Int,
    val totalResults: Int
) {
    val hasNextPage: Boolean get() = currentPage < totalPages
}