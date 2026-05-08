package com.example.presentation.ui.moviedetail

import com.example.domain.model.Movie

/**
 * Created by Ahmad Pahmi on May 2026
 */

sealed class MovieDetailUiState {
    data object Loading : MovieDetailUiState()
    data class Success(val movie: Movie) : MovieDetailUiState()
    data class Error(val message: String) : MovieDetailUiState()
}