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
 */
@Entity(tableName = "cached_movies")
data class CachedMovieEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "poster_url")
    val posterUrl: String,

    @ColumnInfo(name = "release_date")
    val releaseDate: String,

    @ColumnInfo(name = "rating")
    val rating: String,

    @ColumnInfo(name = "genres")
    val genres: String,

    @ColumnInfo(name = "runtime")
    val runtime: String,

    @ColumnInfo(name = "language")
    val language: String,

    @ColumnInfo(name = "cached_at")
    val cachedAt: Long = System.currentTimeMillis()
)