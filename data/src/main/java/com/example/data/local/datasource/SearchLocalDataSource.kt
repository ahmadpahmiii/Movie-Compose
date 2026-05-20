package com.example.data.local.datasource

import com.example.data.local.dao.CachedMovieDao
import com.example.data.local.dao.SearchHistoryDao
import com.example.domain.entity.CachedMovieEntity
import com.example.domain.entity.SearchHistoryEntity
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Created by Ahmad Pahmi on May 2026
 */

class SearchLocalDataSource @Inject constructor(
    private val searchHistoryDao: SearchHistoryDao,
    private val cachedMovieDao: CachedMovieDao
) {
    suspend fun saveSearchQuery(query: String) {
        searchHistoryDao.insertSearch(
            SearchHistoryEntity(query = query.trim())
        )
    }

    fun getRecentSearches(limit: Int = 10): Flow<List<SearchHistoryEntity>> =
        searchHistoryDao.getRecentSearches(limit)

    suspend fun deleteSearch(query: String) =
        searchHistoryDao.deleteSearch(query)

    suspend fun clearHistory() =
        searchHistoryDao.clearHistory()

    fun searchCachedMovies(query: String): Flow<List<CachedMovieEntity>> =
        cachedMovieDao.searchCachedMovies(query)

    suspend fun cacheMovies(movies: List<CachedMovieEntity>) =
        cachedMovieDao.insertMovies(movies)
}