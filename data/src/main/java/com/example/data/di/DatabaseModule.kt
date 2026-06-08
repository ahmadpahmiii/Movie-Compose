package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.local.MovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Ahmad Pahmi on May 2026
 */

/**
 * Provides the Room database and all DAOs.
 *
 * Architectural Decision: DAOs are provided individually so clients
 * depend only on the DAO they need, not the full database class.
 * This enforces the Interface Segregation Principle.
 *
 * The database itself is a @Singleton – one instance for the app's lifetime.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            MovieDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideCachedMovieDao(database: MovieDatabase) = database.cachedMovieDao()
}