package com.example.domain.repository

import com.example.core.common.State
import com.example.domain.model.Movie
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
    fun getMovies(page: Int = 1, limit: Int = 20): Flow<State<List<Movie>>>
    fun searchMovies(query: String, page: Int = 1): Flow<State<List<Movie>>>
    fun getMovieById(id: Int): Flow<State<Movie>>
    fun getRandomCachedMovie(): Flow<State<Movie>>

    // --- Wishlist ---
    fun getWishlistMovies(): Flow<State<List<Movie>>>
    suspend fun toggleWishlist(movieId: Int, isWishlisted: Boolean)
    fun isMovieWishlisted(movieId: Int): Flow<Boolean>

    // --- Recent Search ---
    fun getRecentSearches(): Flow<State<List<Movie>>>
    suspend fun addMovieToRecentSearch(movieId: Int)
    suspend fun clearRecentSearches()
}