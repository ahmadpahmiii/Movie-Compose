package com.example.data.repository

import com.example.data.di.DispatcherProvider
import com.example.data.local.datasource.WishlistLocalDataSource
import com.example.data.mapper.toDomain
import com.example.data.mapper.toWishlistEntity
import com.example.domain.model.Movie
import com.example.domain.repository.WishlistRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Created by Ahmad Pahmi on May 2026
 */

/**
 * Wishlist repository — pure local data, no network calls.
 *
 * All reads return Flows observed on [DispatcherProvider.io].
 * Writes are suspend functions that execute on [DispatcherProvider.io].
 *
 * The UI never calls Room directly; it always goes through this repository,
 * which can later add sync logic, analytics, or remote backup without
 * touching the domain or presentation layers.
 */
class WishlistRepositoryImpl @Inject constructor(
    private val localDataSource: WishlistLocalDataSource,
    private val dispatchers: DispatcherProvider
) : WishlistRepository {
    override suspend fun addToWishlist(movie: Movie) {
        withContext(dispatchers.io) {
            localDataSource.addToWishlist(movie.toWishlistEntity())
        }
    }

    override suspend fun removeFromWishlist(movieId: String) {
        withContext(dispatchers.io) {
            localDataSource.removeFromWishlist(movieId)
        }
    }

    override fun getWishlistMovies(): Flow<List<Movie>> {
        return localDataSource.getWishlistMovies()
            .map { entities -> entities.map { it.toDomain() } }
            .flowOn(dispatchers.io)
    }

    override fun isMovieInWishlist(movieId: String): Flow<Boolean> {
        return localDataSource.isMovieInWishlist(movieId)
            .flowOn(dispatchers.io)
    }

    override fun getWishlistCount(): Flow<Int> {
        return localDataSource.getWishlistCount()
            .flowOn(dispatchers.io)
    }

    override suspend fun clearWishlist() {
        withContext(dispatchers.io) {
            localDataSource.clearWishlist()
        }
    }
}