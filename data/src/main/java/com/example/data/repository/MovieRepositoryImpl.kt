package com.example.data.repository

import com.example.core.common.AppException
import com.example.core.common.State
import com.example.core.common.transform
import com.example.data.di.DispatcherProvider
import com.example.data.local.datasource.SearchLocalDataSource
import com.example.data.mapper.toCachedMovieEntity
import com.example.data.mapper.toDomain
import com.example.data.remote.api.ApiService
import com.example.data.remote.safeApiCall
import com.example.domain.model.Movie
import com.example.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

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
 * All network operations are dispatched on [com.example.di.DispatcherProvider.io]
 * to avoid blocking the main thread.
 */

class MovieRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val localDataSource: SearchLocalDataSource,
    private val dispatchers: DispatcherProvider,
) : MovieRepository {

    /**
     * Offline-first movie list with stale-while-revalidate pattern.
     *
     * Flow:
     * 1. Emit Loading
     * 2. Emit cached movies immediately (if available) → UI shows instantly
     * 3. Fetch from network in parallel
     * 4. Update cache with fresh data
     * 5. Emit fresh data (Room Flow triggers automatically)
     *
     * Uses [channelFlow] because we need to launch a parallel coroutine
     * (network fetch) while emitting from the cache. Regular [flow {}]
     * doesn't allow concurrent emissions.
     */
    override fun getMovies(
        page: Int, limit: Int
    ): Flow<State<List<Movie>>> = channelFlow {
        send(State.Loading)
        // emit cached data immediately for instant UI response
        val cached = localDataSource.searchCachedMovies("").first()
        if (cached.isNotEmpty()) {
            val cachedMovies = cached.map { it.toDomain() }
            send(State.Success(cachedMovies))
        }

        // Fetch fresh data from network in parallel
        launch {
            val result = safeApiCall { apiService.getMovies() }

            when (result) {
                is State.Loading -> {}
                is State.Error -> if (cached.isEmpty()) send(result)
                is State.Success -> {
                    val domainPage = result.data.data?.map { it.toDomain() }.orEmpty()
                    localDataSource.cacheMovies(domainPage.map { it.toCachedMovieEntity() })
                    send(State.Success(domainPage))
                }
            }
        }
    }
        .catch { emit(State.Error(AppException.UnknownException(cause = it))) }
        .flowOn(dispatchers.io)

    override fun searchMovies(
        query: String, page: Int
    ): Flow<State<List<Movie>>> =
        localDataSource.searchCachedMovies(query)
            .map { entities ->
                val movies = entities.map { it.toDomain() }
                State.Success(movies) as State<List<Movie>>
            }
            .catch { emit(State.Error(AppException.UnknownException(cause = it))) }
            .flowOn(dispatchers.io)

    override fun getMovieById(id: String): Flow<State<Movie>> = flow {
        emit(State.Loading)
        val result = safeApiCall { apiService.getMovieById(id) }
        emit(result.transform { it.data.toDomain() })
    }.flowOn(dispatchers.io)

    override fun getRandomMovie(): Flow<State<Movie>> = flow {
        emit(State.Loading)
        val result = safeApiCall { apiService.getRandomMovie() }
        emit(result.transform { it.toDomain() })
    }.flowOn(dispatchers.io)
}