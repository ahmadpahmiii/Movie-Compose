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
 * Use case for fetching the movie list.
 *
 * Architectural Decision: Use cases (interactors) encapsulate a single
 * business operation. They act as the boundary between ViewModels and
 * repositories. Benefits:
 *  - Single Responsibility: one class = one operation
 *  - Reusable: multiple ViewModels can share the same use case
 *  - Testable: independently testable with a fake repository
 *  - Business logic lives here, not in ViewModels or Repositories
 *
 * Using operator fun invoke() allows calling the use case like a function:
 * getMoviesUseCase(page = 1) instead of getMoviesUseCase.execute(page = 1)
 */

class GetMovieUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(page: Int = 1, limit: Int = 20): Flow<State<MoviesPage>> {
        return repository.getMovies(page = page, limit = limit)
    }
}