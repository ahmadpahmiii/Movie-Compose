package com.example.data.local.datasource

import com.example.data.local.dao.WishlistDao
import com.example.domain.entity.WishlistMovieEntity
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Created by Ahmad Pahmi on May 2026
 */

/**
 * Data source abstraction over the [WishlistDao].
 *
 * Architectural Decision: Introducing a DataSource class between the DAO and
 * Repository gives us an additional seam for:
 *  - Swapping the DAO for a different implementation in tests
 *  - Adding in-memory caching on top of Room in the future
 *  - Mixing multiple DAOs for complex operations without cluttering the Repository
 */

class WishlistLocalDataSource @Inject constructor(
    private val wishlistDao: WishlistDao
) {
    suspend fun addToWishlist(movie: WishlistMovieEntity) =
        wishlistDao.addToWishlist(movie)

    suspend fun removeFromWishlist(movieId: String) =
        wishlistDao.removeFromWishlist(movieId)

    fun getWishlistMovies(): Flow<List<WishlistMovieEntity>> =
        wishlistDao.getWishlistMovies()

    fun isMovieInWishlist(movieId: String): Flow<Boolean> =
        wishlistDao.isMovieInWishlist(movieId)

    fun getWishlistCount(): Flow<Int> =
        wishlistDao.getWishlistCount()

    fun searchWishlist(query: String): Flow<List<WishlistMovieEntity>> =
        wishlistDao.searchWishlist(query)

    suspend fun clearWishlist() =
        wishlistDao.clearWishlist()
}