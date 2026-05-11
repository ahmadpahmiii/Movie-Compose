package com.example.presentation.ui.moviedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.common.State
import com.example.domain.usecase.GetMovieDetailUseCase
import com.example.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Created by Ahmad Pahmi on May 2026
 */

/**
 * ViewModel for the Movie Detail screen.
 *
 * Architectural Decision: Uses [SavedStateHandle] to retrieve the movie ID
 * from the navigation back stack. This survives process death and
 * configuration changes, more robust than passing via constructor.
 */
@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieId: String = checkNotNull(savedStateHandle[Screen.MovieDetail.ARG_MOVIE_ID])

    private val _uiState = MutableStateFlow<MovieDetailUiState>(MovieDetailUiState.Loading)
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()

    init {
        loadMovieDetail()
    }

    fun retry() = loadMovieDetail()

    private fun loadMovieDetail() = viewModelScope.launch {
        getMovieDetailUseCase.invoke(movieId).collect { result ->
            _uiState.value = when (result) {
                is State.Loading -> MovieDetailUiState.Loading
                is State.Success -> MovieDetailUiState.Success(result.data)
                is State.Error -> MovieDetailUiState.Error(
                    result.exception.message ?: "Failed to load movie detail"
                )
            }
        }
    }
}