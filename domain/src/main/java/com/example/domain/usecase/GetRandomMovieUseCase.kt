package com.example.domain.usecase

import com.example.core.common.State
import com.example.domain.model.Movie
import com.example.domain.repository.MovieRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Created by Ahmad Pahmi on May 2026
 */

/** Use case for fetching a random/featured movie. */
class GetRandomMovieUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(): Flow<State<Movie>> = repository.getRandomMovie()
}