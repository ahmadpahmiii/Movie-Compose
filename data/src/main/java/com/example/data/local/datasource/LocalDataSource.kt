package com.example.data.local.datasource

import com.example.data.local.dao.CachedMovieDao
import com.example.domain.entity.CachedMovieEntity
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Created by Ahmad Pahmi on May 2026
 */

class LocalDataSource @Inject constructor(
    private val cachedMovieDao: CachedMovieDao
) {
    // --- Movie Cache Operations ---

    fun getCachedMovies(): Flow<List<CachedMovieEntity>> =
        cachedMovieDao.getCachedMovies()

    fun getMovieById(movieId: Int): Flow<CachedMovieEntity?> =
        cachedMovieDao.getMovieById(movieId)

    suspend fun cacheMovies(movies: List<CachedMovieEntity>) =
        cachedMovieDao.insertMovies(movies)

    fun searchCachedMovies(query: String): Flow<List<CachedMovieEntity>> =
        cachedMovieDao.searchCachedMovies(query)

    suspend fun clearCache() = cachedMovieDao.clearCache()

    // --- Wishlist Operations ---

    fun getWishlistMovies(): Flow<List<CachedMovieEntity>> =
        cachedMovieDao.getWishlistMovies()

    suspend fun updateWishlistStatus(movieId: Int, isWishlisted: Boolean) =
        cachedMovieDao.updateWishlistStatus(movieId, isWishlisted)

    fun isMovieWishlisted(movieId: Int): Flow<Boolean> =
        cachedMovieDao.isMovieWishlisted(movieId)

    // --- Recent Search Operations ---

    fun getRecentSearchedMovies(): Flow<List<CachedMovieEntity>> =
        cachedMovieDao.getRecentSearchedMovies()

    suspend fun updateRecentSearchStatus(movieId: Int, isRecentlySearched: Boolean) =
        cachedMovieDao.updateRecentSearchStatus(movieId, isRecentlySearched)

    suspend fun clearRecentSearches() = cachedMovieDao.clearRecentSearches()
}
