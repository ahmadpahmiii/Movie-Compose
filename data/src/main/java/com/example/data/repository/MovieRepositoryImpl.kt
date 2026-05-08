package com.example.data.repository

import com.example.core.common.DispatcherProvider
import com.example.core.common.State
import com.example.core.common.map
import com.example.data.mapper.toDomain
import com.example.data.remote.api.ApiService
import com.example.data.remote.safeApiCall
import com.example.domain.model.Movie
import com.example.domain.model.MoviesPage
import com.example.domain.repository.MovieRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Created by Ahmad Pahmi on May 2026
 */

/**
 * Concrete implementation of [MovieRepository].
 *
 * Architectural Decision: The repository returns Flows rather than
 * suspend functions. This enables:
 *  - Reactive updates (Room DB can emit new values on DB change)
 *  - Easy integration with offline caching (combine remote + local sources)
 *  - Clean cancellation via coroutine scope
 *
 * All network operations are dispatched on [DispatcherProvider.io]
 * to avoid blocking the main thread.
 */

class MovieRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val dispatchers: DispatcherProvider
) : MovieRepository {
    override fun getMovies(
        page: Int, limit: Int
    ): Flow<State<MoviesPage>> = flow {
        emit(State.Loading)
        val result = safeApiCall {
            apiService.getMovies(page = page, limit = limit)
        }
        emit(result.map { it.toDomain() })
    }.flowOn(dispatchers.io)

    override fun searchMovies(
        query: String, page: Int
    ): Flow<State<MoviesPage>> = flow {
        emit(State.Loading)
        val result = safeApiCall {
            apiService.getMovies(page = page, query = query)
        }
        emit(result.map { it.toDomain() })
    }.flowOn(dispatchers.io)

    override fun getMovieById(id: String): Flow<State<Movie>> = flow {
        emit(State.Loading)
        val result = safeApiCall {
            apiService.getMovieById(id)
        }
        emit(result.map { it.toDomain() })
    }.flowOn(dispatchers.io)

    override fun getRandomMovie(): Flow<State<Movie>> = flow {
        emit(State.Loading)
        val result = safeApiCall {
            apiService.getRandomMovie()
        }
        emit(result.map { it.toDomain() })
    }.flowOn(dispatchers.io)
}