package com.example.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Created by Ahmad Pahmi on May 2026
 */

data class MoviesDto(
    @SerializedName("data")
    val data: List<MovieDto>? = null
)

data class MovieDetailDto(
    @SerializedName("data")
    val data: MovieDto
)

data class MovieDto(
    @SerializedName("actors")
    val actors: String? = null,

    @SerializedName("awards")
    val awards: String? = null,

    @SerializedName("boxOffice")
    val boxOffice: String? = null,

    @SerializedName("country")
    val country: String? = null,

    @SerializedName("director")
    val director: String? = null,

    @SerializedName("genre")
    val genre: String? = null,

    @SerializedName("id")
    val id: String? = null,

    @SerializedName("imdbId")
    val imdbId: String? = null,

    @SerializedName("imdbRating")
    val imdbRating: String? = null,

    @SerializedName("language")
    val language: String? = null,

    @SerializedName("plot")
    val plot: String? = null,

    @SerializedName("poster")
    val poster: String? = null,

    @SerializedName("rated")
    val rated: String? = null,

    @SerializedName("released")
    val released: String? = null,

    @SerializedName("runtime")
    val runtime: String? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("writer")
    val writer: String? = null,

    @SerializedName("year")
    val year: String? = null,

    /*  @SerializedName("id")
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
      val adult: Boolean? = null*/
)