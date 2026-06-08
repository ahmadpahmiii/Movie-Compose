package com.example.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.local.dao.CachedMovieDao
import com.example.data.util.Converters
import com.example.domain.entity.CachedMovieEntity

/**
 * Created by Ahmad Pahmi on May 2026
 */

@Database(
    entities = [CachedMovieEntity::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun cachedMovieDao(): CachedMovieDao

    companion object {
        const val DATABASE_NAME = "movie.db"
    }
}