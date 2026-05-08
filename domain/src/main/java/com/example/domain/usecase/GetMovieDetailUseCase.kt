package com.example.domain.usecase

import com.example.core.common.State
import com.example.domain.model.Movie
import com.example.domain.repository.MovieRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Created by Ahmad Pahmi on May 2026
 */

/**
 * Use case for fetching a single movie's details.
 *
 * Validates input before delegating to the repository,
 * keeping validation logic out of both ViewModel and Repository.
 */
class GetMovieDetailUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(id: String): Flow<State<Movie>> {
        require(id.isNotBlank()) { "Movie ID cannot be blank" }
        return repository.getMovieById(id)
    }
}