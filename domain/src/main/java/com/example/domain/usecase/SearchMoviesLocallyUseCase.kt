package com.example.domain.usecase

import com.example.domain.model.Movie
import com.example.domain.repository.SearchRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Created by Ahmad Pahmi on May 2026
 */
class SearchMoviesLocallyUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    operator fun invoke(query: String): Flow<List<Movie>> {
        if (query.isBlank()) return flowOf(emptyList())
        return searchRepository.searchMoviesLocally(query.trim())
    }
}