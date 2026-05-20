package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.domain.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by Ahmad Pahmi on May 2026
 */

@Dao
interface SearchHistoryDao {
    /**
     * Insert or update a search query.
     * REPLACE strategy handles the unique index on [query]:
     * if the query already exists, it gets replaced (timestamp updated).
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearch(search: SearchHistoryEntity)

    @Query("SELECT * FROM search_history ORDER BY searched_at DESC LIMIT :limit")
    fun getRecentSearches(limit: Int = 10): Flow<List<SearchHistoryEntity>>

    @Query("DELETE FROM search_history WHERE `query` = :queryStr")
    suspend fun deleteSearch(queryStr: String)

    @Query("DELETE FROM search_history")
    suspend fun clearHistory()

    @Query("SELECT COUNT(*) FROM search_history")
    fun getHistoryCount(): Flow<Int>
}