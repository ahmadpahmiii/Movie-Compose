package com.example.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.local.dao.CachedMovieDao
import com.example.data.local.dao.RecentlyViewedDao
import com.example.data.local.dao.SearchHistoryDao
import com.example.data.local.dao.WishlistDao
import com.example.domain.entity.CachedMovieEntity
import com.example.domain.entity.RecentlyViewedEntity
import com.example.domain.entity.SearchHistoryEntity
import com.example.domain.entity.WishlistMovieEntity

/**
 * Created by Ahmad Pahmi on May 2026
 */

@Database(
    entities = [
        CachedMovieEntity::class,
        RecentlyViewedEntity::class,
        SearchHistoryEntity::class,
        WishlistMovieEntity::class
    ],
    version = 1,
    exportSchema = true,
)

abstract class MovieDatabase : RoomDatabase() {
    abstract fun cachedMovieDao(): CachedMovieDao
    abstract fun recentlyViewedDao(): RecentlyViewedDao
    abstract fun searchHistoryDao(): SearchHistoryDao
    abstract fun wishlistDao(): WishlistDao

    companion object {
        const val DATABASE_NAME = "movie.db"
    }
}