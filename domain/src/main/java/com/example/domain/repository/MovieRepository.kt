package com.example.domain.repository

import com.example.core.common.State
import com.example.domain.model.Movie
import com.example.domain.model.MoviesPage
import kotlinx.coroutines.flow.Flow

/**
 * Created by Ahmad Pahmi on May 2026
 */

/**
 * Repository interface owned by the domain layer.
 *
 * Architectural Decision (Dependency Inversion):
 * The domain layer defines the interface; the data layer implements it.
 * This means the domain has ZERO dependency on the data layer,
 * making business logic completely isolated and easily testable
 * with fake/stub implementations.
 */
interface MovieRepository {
    fun getMovies(page: Int = 1, limit: Int = 20): Flow<State<MoviesPage>>
    fun searchMovies(query: String, page: Int = 1): Flow<State<MoviesPage>>
    fun getMovieById(id: String): Flow<State<Movie>>
    fun getRandomMovie(): Flow<State<Movie>>
}