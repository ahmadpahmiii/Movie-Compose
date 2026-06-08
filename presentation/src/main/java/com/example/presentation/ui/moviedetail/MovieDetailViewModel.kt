package com.example.presentation.ui.moviedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.common.State
import com.example.domain.usecase.GetMovieDetailUseCase
import com.example.domain.usecase.GetMovieWishlistStatusUseCase
import com.example.domain.usecase.ToggleWishlistUseCase
import com.example.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
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
    private val toggleWishlistUseCase: ToggleWishlistUseCase,
    private val wishlistStatusUseCase: GetMovieWishlistStatusUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieId: Int = checkNotNull(savedStateHandle[Screen.MovieDetail.ARG_MOVIE_ID])

    private val _uiState = MutableStateFlow<MovieDetailUiState>(MovieDetailUiState.Loading)
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()

    /** Reactive wishlist status – updates instantly across screens. */
    val ishWishlisted: StateFlow<Boolean> = wishlistStatusUseCase(movieId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = false
        )

    init {
        loadMovieDetail()
    }

    fun retry() = loadMovieDetail()

    fun toggleWishlist() {
        val currentState = _uiState.value
        if (currentState is MovieDetailUiState.Success) {
            viewModelScope.launch { toggleWishlistUseCase.invoke(currentState.movie.id) }
        }
    }

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