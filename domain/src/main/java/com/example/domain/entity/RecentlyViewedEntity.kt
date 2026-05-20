package com.example.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Ahmad Pahmi on May 2026
 */

/**
 * Stores recently viewed movie IDs with timestamps.
 * We only store the ID + basic info to keep the table lightweight.
 * Full details are fetched from the API or wishlist table.
 */
@Entity(tableName = "recently_viewed")
data class RecentlyViewedEntity(

    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    val movieId: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "poster_url")
    val posterUrl: String,

    @ColumnInfo(name = "rating")
    val rating: String,

    @ColumnInfo(name = "viewed_at")
    val viewedAt: Long = System.currentTimeMillis()
)