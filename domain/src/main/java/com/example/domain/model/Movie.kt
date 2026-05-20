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
    val id: String,
    val title: String,
    val description: String,
    val posterUrl: String,
    val releaseDate: String,
    val rating: String,
    val genres: List<String>,
    val runtime: String,
    val language: List<String>
)