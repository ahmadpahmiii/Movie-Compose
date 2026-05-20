package com.example.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Created by Ahmad Pahmi on May 2026
 */

/**
 * Stores user search history entries.
 *
 * Uses a unique index on [query] so duplicate searches are auto-deduplicated.
 * When a repeated query is searched, we UPDATE the timestamp instead of inserting.
 */
@Entity(
    tableName = "search_history",
    indices = [Index(value = ["query"], unique = true)]
)
data class SearchHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "query")
    val query: String,

    @ColumnInfo(name = "searched_at")
    val searchedAt: Long = System.currentTimeMillis()
)
