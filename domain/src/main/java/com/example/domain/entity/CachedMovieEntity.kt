package com.example.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Ahmad Pahmi on May 2026
 */

/**
 * Caches the full movie list for offline-first support.
 *
 * Architectural Decision: We cache the full API response in Room so the
 * list screen loads instantly from the database while we fetch fresh data.
 * This is the "stale-while-revalidate" pattern.
 *
 * This entity also tracks user-specific states such as wishlist status and
 * recent search history to provide a seamless personalized experience
 * without requiring additional network calls for local state management.
 */
@Entity(tableName = "cached_movies")
data class CachedMovieEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo("title")
    val title: String,

    @ColumnInfo(name = "original_title")
    val originalTitle: String,

    @ColumnInfo(name = "original_language")
    val originalLanguage: String,

    @ColumnInfo(name = "overview")
    val overview: String,

    @ColumnInfo(name = "release_date")
    val releaseDate: String,

    @ColumnInfo(name = "genre_ids")
    val genreIds: List<Int>,

    @ColumnInfo(name = "popularity")
    val popularity: Double,

    @ColumnInfo(name = "vote_average")
    val voteAverage: Double,

    @ColumnInfo(name = "vote_count")
    val voteCount: Int,

    @ColumnInfo(name = "poster_path")
    val posterPath: String,

    @ColumnInfo(name = "backdrop_path")
    val backdropPath: String,

    @ColumnInfo(name = "is_adult")
    val isAdult: Boolean,

    @ColumnInfo(name = "include_video")
    val includeVideo: Boolean,

    @ColumnInfo(name = "cached_at")
    val cachedAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "viewed_at")
    val viewedAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "is_recently_searched")
    val isRecentlySearched: Boolean = false,

    @ColumnInfo(name = "searched_at")
    val searchedAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "is_wishlisted")
    val isWishlisted: Boolean = false,

    @ColumnInfo(name = "wishlisted_at")
    val wishlistedAt: Long = System.currentTimeMillis()
)