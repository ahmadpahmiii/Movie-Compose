package com.example.domain.usecase

import com.example.domain.model.Movie
import com.example.domain.repository.WishlistRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Created by Ahmad Pahmi on May 2026
 */

class GetWishlistMoviesUseCase @Inject constructor(
    private val repository: WishlistRepository
) {
    operator fun invoke(): Flow<List<Movie>> = repository.getWishlistMovies()
}