package com.example.domain.usecase

import com.example.domain.repository.MovieRepository
import javax.inject.Inject

/**
 * Created by Ahmad Pahmi on May 2026
 */

class ClearRecentSearchesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke() {
        repository.clearRecentSearches()
    }
}
