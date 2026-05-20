package com.example.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Ahmad Pahmi on May 2026
 */


@Entity(tableName = "wishlist_movies")
data class WishlistMovieEntity(
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

    /** Timestamp when the user added this to wishlist – used for sorting. */
    @ColumnInfo(name = "added_at")
    val addedAt: Long = System.currentTimeMillis()
)
