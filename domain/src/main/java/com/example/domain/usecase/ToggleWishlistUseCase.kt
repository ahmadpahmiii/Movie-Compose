package com.example.domain.usecase

import com.example.domain.model.Movie
import com.example.domain.repository.WishlistRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.first

/**
 * Created by Ahmad Pahmi on May 2026
 */

/**
 * Toggles a movie's wishlist status.
 *
 * Architectural Decision: The toggle logic (check → add/remove) lives
 * here in the use case, not in the ViewModel. This ensures the business
 * rule is centrally located and testable independently of any UI concern.
 *
 * Returns the new wishlist state (true = added, false = removed).
 */
class ToggleWishlistUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository
) {
    suspend operator fun invoke(movie: Movie): Boolean {
        val isCurrentlyInWishlist = wishlistRepository.isMovieInWishlist(movie.id).first()

        return if (isCurrentlyInWishlist) {
            wishlistRepository.removeFromWishlist(movie.id)
            false
        } else {
            wishlistRepository.addToWishlist(movie)
            true
        }
    }
}