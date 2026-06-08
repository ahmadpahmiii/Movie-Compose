package com.example.domain.usecase

import com.example.core.common.State
import com.example.domain.model.Movie
import com.example.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by Ahmad Pahmi on May 2026
 */
class SearchMoviesLocallyUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(query: String): Flow<List<Movie>> {
        if (query.isBlank()) return flowOf(emptyList())
        return repository.searchMovies(query.trim()).map { state ->
            if (state is State.Success) state.data else emptyList()
        }
    }
}
