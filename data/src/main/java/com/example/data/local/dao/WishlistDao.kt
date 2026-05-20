package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.domain.entity.WishlistMovieEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by Ahmad Pahmi on May 2026
 */

/**
 * DAO for wishlist operations.
 *
 * Architectural Decision: All list/status queries return [Flow] so the UI
 * automatically reflects database changes without manual refresh calls.
 * Room emits a new value whenever the underlying table changes, giving us
 * true reactive data.
 *
 * Single-item lookups (isWishlisted) also use Flow so the detail screen's
 * "Add to Wishlist" button updates reactively when state changes from
 * another tab/screen.
 */

@Dao
interface WishlistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWishlist(movieEntity: WishlistMovieEntity)

    @Query("DELETE FROM wishlist_movies WHERE id = :movieId")
    suspend fun removeFromWishlist(movieId: String)

    @Query("SELECT * FROM wishlist_movies ORDER BY added_at DESC")
    fun getWishlistMovies(): Flow<List<WishlistMovieEntity>>

    /**
     * Returns a Flow<Boolean> – emits true if the movie is wishlisted.
     * The COUNT approach is more efficient than SELECT * for status checks.
     */
    @Query("SELECT COUNT(*) > 0 FROM wishlist_movies WHERE id = :movieId")
    fun isMovieInWishlist(movieId: String): Flow<Boolean>

    @Query("SELECT COUNT(*) FROM wishlist_movies")
    fun getWishlistCount(): Flow<Int>

    @Query("DELETE FROM wishlist_movies")
    suspend fun clearWishlist()

    /**
     * Full-text search within wishlisted movies.
     * Uses LIKE for simplicity; upgrade to FTS4/FTS5 for large datasets.
     */
    @Query(
        """
        SELECT * FROM wishlist_movies
        WHERE title LIKE '%' || :query || '%'
        OR description LIKE '%' || :query || '%'
        ORDER BY added_at DESC
    """
    )
    fun searchWishlist(query: String): Flow<List<WishlistMovieEntity>>
}