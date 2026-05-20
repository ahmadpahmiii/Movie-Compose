package com.example.domain.repository

import com.example.domain.model.Movie
import kotlinx.coroutines.flow.Flow

/**
 * Created by Ahmad Pahmi on May 2026
 */

interface WishlistRepository {
    suspend fun addToWishlist(movie: Movie)
    suspend fun removeFromWishlist(movieId: String)
    fun getWishlistMovies(): Flow<List<Movie>>
    fun isMovieInWishlist(movieId: String): Flow<Boolean>
    fun getWishlistCount(): Flow<Int>
    suspend fun clearWishlist()
}