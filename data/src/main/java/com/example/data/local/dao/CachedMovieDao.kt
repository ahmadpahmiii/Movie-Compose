package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.domain.entity.CachedMovieEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by Ahmad Pahmi on May 2026
 */

@Dao
interface CachedMovieDao {

    /**
     * Inserts movies while preserving local-only state like wishlist and search history.
     */
    @Transaction
    suspend fun insertMovies(movies: List<CachedMovieEntity>) {
        val results = insertMoviesInternal(movies)
        val updateList = mutableListOf<CachedMovieEntity>()

        for (i in results.indices) {
            if (results[i] == -1L) {
                val movie = movies[i]
                val existing = getMovieByIdSync(movie.id)
                if (existing != null) {
                    updateList.add(
                        movie.copy(
                            isWishlisted = existing.isWishlisted,
                            wishlistedAt = existing.wishlistedAt,
                            isRecentlySearched = existing.isRecentlySearched,
                            searchedAt = existing.searchedAt,
                            viewedAt = existing.viewedAt
                        )
                    )
                }
            }
        }
        if (updateList.isNotEmpty()) {
            updateMoviesInternal(updateList)
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMoviesInternal(movies: List<CachedMovieEntity>): List<Long>

    @Update
    suspend fun updateMoviesInternal(movies: List<CachedMovieEntity>)

    @Query("SELECT * FROM cached_movies WHERE id = :movieId")
    suspend fun getMovieByIdSync(movieId: Int): CachedMovieEntity?

    @Query("SELECT * FROM cached_movies ORDER BY cached_at DESC")
    fun getCachedMovies(): Flow<List<CachedMovieEntity>>

    @Query("SELECT * FROM cached_movies WHERE id = :movieId")
    fun getMovieById(movieId: Int): Flow<CachedMovieEntity?>

    @Query(
        """
        SELECT * FROM cached_movies
        WHERE title LIKE '%' || :query || '%'
        OR overview LIKE '%' || :query || '%'
        ORDER BY vote_average DESC
    """
    )
    fun searchCachedMovies(query: String): Flow<List<CachedMovieEntity>>

    @Query("SELECT COUNT(*) FROM cached_movies")
    suspend fun getCachedMovieCount(): Int

    /** Check if cache is stale (older than [maxAgeMs] milliseconds). */
    @Query("SELECT MIN(cached_at) FROM cached_movies")
    suspend fun getOldestCacheTimestamp(): Long?

    @Query("DELETE FROM cached_movies")
    suspend fun clearCache()

    // --- Wishlist Operations ---

    @Query("SELECT * FROM cached_movies WHERE is_wishlisted = 1 ORDER BY wishlisted_at DESC")
    fun getWishlistMovies(): Flow<List<CachedMovieEntity>>

    @Query("UPDATE cached_movies SET is_wishlisted = :isWishlisted, wishlisted_at = :timestamp WHERE id = :movieId")
    suspend fun updateWishlistStatus(movieId: Int, isWishlisted: Boolean, timestamp: Long = System.currentTimeMillis())

    @Query("SELECT EXISTS(SELECT 1 FROM cached_movies WHERE id = :movieId AND is_wishlisted = 1)")
    fun isMovieWishlisted(movieId: Int): Flow<Boolean>

    // --- Recent Search Operations ---

    @Query("SELECT * FROM cached_movies WHERE is_recently_searched = 1 ORDER BY searched_at DESC")
    fun getRecentSearchedMovies(): Flow<List<CachedMovieEntity>>

    @Query("UPDATE cached_movies SET is_recently_searched = :isRecentlySearched, searched_at = :timestamp WHERE id = :movieId")
    suspend fun updateRecentSearchStatus(movieId: Int, isRecentlySearched: Boolean, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE cached_movies SET is_recently_searched = 0")
    suspend fun clearRecentSearches()
}
