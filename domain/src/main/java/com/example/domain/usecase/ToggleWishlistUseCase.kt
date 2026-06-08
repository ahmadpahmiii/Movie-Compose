package com.example.domain.usecase

import com.example.domain.repository.MovieRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Created by Ahmad Pahmi on May 2026
 */

class ToggleWishlistUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int): Boolean {
        val isCurrentlyInWishlist = repository.isMovieWishlisted(movieId).first()
        val newState = !isCurrentlyInWishlist
        repository.toggleWishlist(movieId, newState)
        return newState
    }
}
