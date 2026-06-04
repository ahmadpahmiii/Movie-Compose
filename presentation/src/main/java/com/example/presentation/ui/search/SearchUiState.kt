package com.example.presentation.ui.search

import com.example.domain.model.Movie

/**
 * Created by Ahmad Pahmi on June 2026
 */

/**
 * Represents the complete UI state for the Search screen.
 *
 * Architectural Decision: We use a single data class (not a sealed class)
 * because the search screen has multiple simultaneous states:
 * history and results can coexist. A sealed class would force us into
 * impossible mutual exclusivity.
 */

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val searchResults: List<Movie> = emptyList(),
    val recentSearches: List<String> = emptyList(),
    val error: String? = null
) {
    val isIdle: Boolean get() = query.isEmpty()
    val isEmpty: Boolean get() = !isLoading && query.isNotEmpty() && searchResults.isEmpty()
    val showHistory: Boolean get() = isIdle && recentSearches.isNotEmpty()
    val showResults: Boolean get() = !isIdle && searchResults.isNotEmpty()
}
