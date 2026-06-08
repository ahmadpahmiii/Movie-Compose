package com.example.presentation.ui.movielist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.common.State
import com.example.domain.model.Movie
import com.example.domain.usecase.GetMovieUseCase
import com.example.domain.usecase.GetRandomCachedMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

/**
 * Created by Ahmad Pahmi on May 2026
 */

/**
 * ViewModel for the Movie List screen.
 *
 * Architectural Decisions:
 *  - Exposes a single [StateFlow<MovieListUiState>] – single source of truth for UI.
 *  - Uses [MutableStateFlow] internally; exposes immutable [StateFlow] externally.
 *  - All business logic delegated to use cases; ViewModel only orchestrates.
 *  - Configuration change safe: StateFlow retains state across recompositions.
 *  - Search uses debounce to avoid excessive API calls on every keystroke.
 *  - Local filtering runs in-memory when movies are already loaded.
 */

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val getMovieUseCase: GetMovieUseCase,
    private val getRandomCachedMovieUseCase: GetRandomCachedMovieUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<MovieListUiState>(MovieListUiState.Loading)
    val uiState: StateFlow<MovieListUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var cachedMovies: List<Movie> = emptyList()

    init {
        loadMovies()
        observeSearchQuery()
    }

    fun loadMovies() = viewModelScope.launch {
        getMovieUseCase.invoke().collect { result ->
            when (result) {
                is State.Loading -> _uiState.value = MovieListUiState.Loading
                is State.Success -> {
                    val movies = result.data
                    cachedMovies = movies
                    if (movies.isEmpty()) {
                        _uiState.value = MovieListUiState.Empty
                    } else {
                        _uiState.value = MovieListUiState.Success(
                            movies = movies,
                            filteredMovies = movies,
                            searchQuery = _searchQuery.value
                        )
                    }

                    // Load a random movie for the featured movie
                    loadFeaturedMovie()
                }

                is State.Error -> _uiState.value = MovieListUiState.Error(
                    message = result.exception.message ?: "Failed to load movies",
                    retryAction = ::loadMovies
                )
            }
        }
    }

    fun refresh() {
        val currentState = _uiState.value
        if (currentState is MovieListUiState.Success) {
            _uiState.update {
                (it as? MovieListUiState.Success)?.copy(isRefreshing = true) ?: it
            }
        }
        loadMovies()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun clearSearch() {
        _searchQuery.value = ""
    }

    /**
     * Observes search query changes with debounce.
     * Performs local filtering when movies are cached.
     * Could be extended to call searchMoviesUseCase for server-side search.
     */
    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        _searchQuery
            .debounce(300.milliseconds)
            .distinctUntilChanged()
            .onEach { query ->
                val currentMovies = cachedMovies
                if (currentMovies.isNotEmpty()) {
                    val filtered = if (query.isBlank()) {
                        currentMovies
                    } else {
                        currentMovies.filter { movie ->
                            movie.title.contains(query, true) ||
                                    movie.overview.contains(query, true)
                        }
                    }
                    _uiState.update { currentState ->
                        if (currentState is MovieListUiState.Success) {
                            currentState.copy(
                                filteredMovies = filtered,
                                searchQuery = query
                            )
                        } else currentState
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadFeaturedMovie() = viewModelScope.launch {
        getRandomCachedMovieUseCase.invoke().collect { result ->
            if (result is State.Success) {
                _uiState.update { currentState ->
                    if (currentState is MovieListUiState.Success) {
                        currentState.copy(featuredMovie = result.data)
                    } else currentState
                }
            }
        }
    }
}