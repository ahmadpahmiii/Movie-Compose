package com.example.presentation.ui.wishlist

import com.example.domain.model.Movie

/**
 * Created by Ahmad Pahmi on June 2026
 */

data class WishlistUiState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = true,
    val removedMovieTitle: String? = null // for snackbar
) {
    val isEmpty: Boolean get() = !isLoading && movies.isEmpty()
}
