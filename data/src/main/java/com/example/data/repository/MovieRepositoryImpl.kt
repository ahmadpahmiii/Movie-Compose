package com.example.data.repository

import com.example.core.common.AppException
import com.example.core.common.State
import com.example.data.di.DispatcherProvider
import com.example.data.local.datasource.LocalDataSource
import com.example.data.mapper.MovieMapper.toCachedMovieEntity
import com.example.data.mapper.MovieMapper.toMovie
import com.example.data.remote.api.ApiService
import com.example.data.remote.safeApiCall
import com.example.domain.model.Movie
import com.example.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Ahmad Pahmi on May 2026
 */

class MovieRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val localDataSource: LocalDataSource,
    private val dispatchers: DispatcherProvider,
) : MovieRepository {

    override fun getMovies(page: Int, limit: Int): Flow<State<List<Movie>>> = channelFlow {
        // Immediately signal loading
        send(State.Loading)

        // Observe local database changes.
        // We launch this as a child coroutine. Because Room Flows never complete,
        // this "keeps the channel open" reactively.
        launch {
            localDataSource.getCachedMovies()
                .map { entities -> entities.map { it.toMovie() } }
                .collect { movies ->
                    // Only emit success if we actually have data
                    if (movies.isNotEmpty()) {
                        send(State.Success(movies))
                    }
                }
        }

        // Fetch fresh data from network
        val result = safeApiCall { apiService.getPopularMovies() }

        when (result) {
            is State.Success -> {
                val entities = result.data.results.orEmpty()
                    .map { it.toCachedMovieEntity() }
                localDataSource.cacheMovies(entities)
                // Note: We don't need to 'send' success here manually because
                // the DB observer above will automatically pick up the new data.
            }

            is State.Error -> {
                // Only send an error if the database is currently empty.
                // This prevents a network glitch from hiding cached data.
                val isCacheEmpty = localDataSource.getCachedMovies().first().isEmpty()
                if (isCacheEmpty) {
                    send(State.Error(result.exception))
                }
            }

            else -> Unit
        }
    }.catch {
        emit(State.Error(AppException.UnknownException(cause = it)))
    }.flowOn(dispatchers.io)

    override fun searchMovies(query: String, page: Int): Flow<State<List<Movie>>> =
        localDataSource.searchCachedMovies(query)
            .map { entities ->
                State.Success(entities.map { it.toMovie() }) as State<List<Movie>>
            }
            .catch { emit(State.Error(AppException.UnknownException(cause = it))) }
            .flowOn(dispatchers.io)

    override fun getMovieById(id: Int): Flow<State<Movie>> =
        localDataSource.getMovieById(id)
            .map { entity ->
                if (entity != null) State.Success(entity.toMovie())
                else State.Error(AppException.UnknownException(message = "Movie not found"))
            }
            .catch { emit(State.Error(AppException.UnknownException(cause = it))) }
            .flowOn(dispatchers.io)

    override fun getRandomCachedMovie(): Flow<State<Movie>> =
        localDataSource.getCachedMovies()
            .map { entities ->
                if (entities.isNotEmpty()) {
                    State.Success(entities.random().toMovie())
                } else {
                    State.Error(AppException.UnknownException(message = "No movies available"))
                }
            }
            .catch { emit(State.Error(AppException.UnknownException(cause = it))) }
            .flowOn(dispatchers.io)

    // --- Wishlist Implementation ---

    override fun getWishlistMovies(): Flow<State<List<Movie>>> =
        localDataSource.getWishlistMovies()
            .map { entities ->
                State.Success(entities.map { it.toMovie() }) as State<List<Movie>>
            }
            .catch { emit(State.Error(AppException.UnknownException(cause = it))) }
            .flowOn(dispatchers.io)

    override suspend fun toggleWishlist(movieId: Int, isWishlisted: Boolean) {
        localDataSource.updateWishlistStatus(movieId, isWishlisted)
    }

    override fun isMovieWishlisted(movieId: Int): Flow<Boolean> =
        localDataSource.isMovieWishlisted(movieId).flowOn(dispatchers.io)

    // --- Recent Search Implementation ---

    override fun getRecentSearches(): Flow<State<List<Movie>>> =
        localDataSource.getRecentSearchedMovies()
            .map { entities ->
                State.Success(entities.map { it.toMovie() }) as State<List<Movie>>
            }
            .catch { emit(State.Error(AppException.UnknownException(cause = it))) }
            .flowOn(dispatchers.io)

    override suspend fun addMovieToRecentSearch(movieId: Int) {
        localDataSource.updateRecentSearchStatus(movieId, true)
    }

    override suspend fun clearRecentSearches() {
        localDataSource.clearRecentSearches()
    }
}
