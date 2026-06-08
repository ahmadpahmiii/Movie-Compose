package com.example.presentation.ui.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Movie
import com.example.domain.usecase.GetWishlistMoviesUseCase
import com.example.domain.usecase.ToggleWishlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Ahmad Pahmi on June 2026
 */

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val wishlistMoviesUseCase: GetWishlistMoviesUseCase,
    private val toggleWishlistUseCase: ToggleWishlistUseCase
) : ViewModel() {

    private val _removedMovieTitle = MutableStateFlow<String?>(null)
    private val _isLoading = MutableStateFlow(true)

    val uiState: StateFlow<WishlistUiState> = combine(
        wishlistMoviesUseCase(),
        _isLoading,
        _removedMovieTitle
    ) { movies, isLoading, removedTitle ->
        WishlistUiState(
            movies = movies.getOrNull().orEmpty(),
            isLoading = isLoading,
            removedMovieTitle = removedTitle
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = WishlistUiState()
    ).also {
        // Once the first emission arrives, we're no longer "loading"
        viewModelScope.launch { it.collect { _isLoading.value = false } }
    }

    fun removeFromWishlist(movie: Movie) = viewModelScope.launch {
        toggleWishlistUseCase(movie.id)
        _removedMovieTitle.value = movie.title
    }

    fun onSnackbarShown() {
        _removedMovieTitle.value = null
    }
}