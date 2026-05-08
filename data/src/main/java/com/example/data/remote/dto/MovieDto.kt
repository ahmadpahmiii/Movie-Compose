package com.example.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Created by Ahmad Pahmi on May 2026
 */

data class MovieDto(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("overview")
    val overview: String? = null,

    @SerializedName("poster_path")
    val posterPath: String? = null,

    @SerializedName("backdrop_path")
    val backdropPath: String? = null,

    @SerializedName("release_date")
    val releaseDate: String? = null,

    @SerializedName("vote_average")
    val voteAverage: Double? = null,

    @SerializedName("vote_count")
    val voteCount: Int? = null,

    @SerializedName("genre_ids")
    val genreIds: List<Int>? = null,

    @SerializedName("genres")
    val genres: List<GenreDto>? = null,

    @SerializedName("runtime")
    val runtime: Int? = null,

    @SerializedName("original_language")
    val originalLanguage: String? = null,

    @SerializedName("popularity")
    val popularity: Double? = null,

    @SerializedName("adult")
    val adult: Boolean? = null
)

data class GenreDto(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("name")
    val name: String? = null
)

/**
 * Paginated wrapper for the movies list endpoint.
 */
data class MoviesDto(
    @SerializedName("results")
    val results: List<MovieDto>? = null,

    @SerializedName("page")
    val page: Int? = null,

    @SerializedName("total_pages")
    val totalPages: Int? = null,

    @SerializedName("total_results")
    val totalResults: Int? = null
)