package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.domain.entity.RecentlyViewedEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by Ahmad Pahmi on May 2026
 */

@Dao
interface RecentlyViewedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun markAsViewed(entity: RecentlyViewedEntity)

    @Query("SELECT * FROM recently_viewed ORDER BY viewed_at DESC LIMIT :limit")
    fun getRecentlyViewed(limit: Int = 20): Flow<List<RecentlyViewedEntity>>

    @Query("DELETE FROM recently_viewed WHERE movie_id NOT IN (SELECT movie_id FROM recently_viewed ORDER BY viewed_at DESC LIMIT 50)")
    suspend fun pruneOldEntries()

    @Query("DELETE FROM recently_viewed")
    suspend fun clearAll()
}