package com.example.domain.repository

import com.example.domain.model.Movie
import kotlinx.coroutines.flow.Flow

/**
 * Created by Ahmad Pahmi on May 2026
 */

interface SearchRepository {
    fun searchMoviesLocally(query: String): Flow<List<Movie>>
    fun getRecentSearches(limit: Int = 10): Flow<List<String>>
    suspend fun saveSearchQuery(query: String)
    suspend fun deleteSearch(query: String)
    suspend fun clearHistory()
}