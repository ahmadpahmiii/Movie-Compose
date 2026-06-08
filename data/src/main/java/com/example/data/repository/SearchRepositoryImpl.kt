package com.example.data.repository

import com.example.data.di.DispatcherProvider
import com.example.data.local.datasource.LocalDataSource
import com.example.data.mapper.toDomain
import com.example.domain.model.Movie
import com.example.domain.repository.SearchRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Created by Ahmad Pahmi on May 2026
 */

class SearchRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val dispatchers: DispatcherProvider
) : SearchRepository {

    override fun searchMoviesLocally(query: String): Flow<List<Movie>> {
        return localDataSource.searchCachedMovies(query)
            .map { entities -> entities.map { it.toDomain() } }
            .flowOn(dispatchers.io)
    }

    override fun getRecentSearches(limit: Int): Flow<List<String>> {
        return localDataSource.getRecentSearches(limit)
            .map { entities -> entities.map { it.query } }
            .flowOn(dispatchers.io)
    }

    override suspend fun saveSearchQuery(query: String) {
        withContext(dispatchers.io) {
            if (query.isNotBlank()) localDataSource.saveSearchQuery(query)
        }
    }

    override suspend fun deleteSearch(query: String) {
        withContext(dispatchers.io) {
            localDataSource.deleteSearch(query)
        }
    }

    override suspend fun clearHistory() {
        withContext(dispatchers.io) {
            localDataSource.clearHistory()
        }
    }
}