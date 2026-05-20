package com.example.domain.usecase

import com.example.domain.repository.WishlistRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Created by Ahmad Pahmi on May 2026
 */

/** Reactive wishlist status for a single movie – drives the ♥ button state. */
class GetMovieWishlistStatusUseCase @Inject constructor(
    private val repository: WishlistRepository
) {
    operator fun invoke(movieId: String): Flow<Boolean> = repository.isMovieInWishlist(movieId)
}