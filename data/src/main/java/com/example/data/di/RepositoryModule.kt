package com.example.data.di

import com.example.data.repository.MovieRepositoryImpl
import com.example.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Ahmad Pahmi on May 2026
 */


/**
 * Binds domain repository interfaces to their data layer implementations.
 *
 * Architectural Decision (Dependency Inversion):
 * The domain layer owns the interfaces; the data layer implements them.
 * This module is the "adapter" that wires them together at runtime.
 * Neither layer knows about this module — they're fully decoupled.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepository(impl: MovieRepositoryImpl): MovieRepository
}