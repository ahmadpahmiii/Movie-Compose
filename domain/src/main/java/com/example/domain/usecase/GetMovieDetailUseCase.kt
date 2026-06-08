package com.example.domain.usecase

import com.example.core.common.State
import com.example.domain.model.Movie
import com.example.domain.repository.MovieRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Created by Ahmad Pahmi on May 2026
 */

class GetMovieDetailUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(id: Int): Flow<State<Movie>> {
        require(id > 0) { "Movie ID must be greater than 0" }
        return repository.getMovieById(id)
    }
}
