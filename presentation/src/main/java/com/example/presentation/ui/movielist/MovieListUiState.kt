package com.example.presentation.ui.movielist

import com.example.domain.model.Movie

/**
 * Created by Ahmad Pahmi on May 2026
 */

sealed class MovieListUiState {
    data object Loading : MovieListUiState()
    data class Success(
        val movies: List<Movie>,
        val filteredMovies: List<Movie>,
        val searchQuery: String = "",
        val isRefreshing: Boolean = false,
        val featuredMovie: Movie? = null
    ) : MovieListUiState() {
        val isSearchActive: Boolean get() = searchQuery.isNotEmpty()
        val displayMovies: List<Movie> get() = if (isSearchActive) filteredMovies else movies
    }

    data class Error(
        val message: String,
        val retryAction: (() -> Unit)? = null
    ) : MovieListUiState()

    data object Empty : MovieListUiState()
}