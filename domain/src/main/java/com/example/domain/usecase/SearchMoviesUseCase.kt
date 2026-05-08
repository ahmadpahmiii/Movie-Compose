package com.example.domain.usecase

import com.example.core.common.State
import com.example.domain.model.MoviesPage
import com.example.domain.repository.MovieRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Created by Ahmad Pahmi on May 2026
 */

/**
 * Use case for searching movies by a text query.
 *
 * Business rule: query must have at least 2 characters.
 * This rule lives here, not in the ViewModel.
 */
class SearchMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    companion object {
        const val MINIMUM_QUERY_LENGTH = 2
    }

    operator fun invoke(query: String, page: Int = 1): Flow<State<MoviesPage>> {
        require(query.length >= MINIMUM_QUERY_LENGTH) {
            "Search query must be at least $MINIMUM_QUERY_LENGTH characters"
        }
        return repository.searchMovies(query = query.trim(), page = page)
    }
}