package com.example.domain.usecase

import com.example.domain.repository.SearchRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Created by Ahmad Pahmi on May 2026
 */

class GetSearchHistoryUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    operator fun invoke(limit: Int = 10): Flow<List<String>> =
        repository.getRecentSearches(limit)
}