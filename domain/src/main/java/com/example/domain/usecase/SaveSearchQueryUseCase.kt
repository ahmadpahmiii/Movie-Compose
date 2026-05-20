package com.example.domain.usecase

import com.example.domain.repository.SearchRepository
import javax.inject.Inject

/**
 * Created by Ahmad Pahmi on May 2026
 */

class SaveSearchQueryUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(query: String) {
        if (query.length >= 2) repository.saveSearchQuery(query)
    }
}