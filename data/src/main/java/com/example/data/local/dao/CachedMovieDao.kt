package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.domain.entity.CachedMovieEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by Ahmad Pahmi on May 2026
 */

@Dao
interface CachedMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<CachedMovieEntity>)

    @Query("SELECT * FROM cached_movies ORDER BY rating DESC")
    fun getCachedMovies(): Flow<List<CachedMovieEntity>>

    @Query(
        """
        SELECT * FROM cached_movies
        WHERE title LIKE '%' || :query || '%'
        OR description LIKE '%' || :query || '%'
        ORDER BY rating DESC
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
}