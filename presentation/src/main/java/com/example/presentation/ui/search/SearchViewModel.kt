package com.example.presentation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.AddMovieToRecentSearchUseCase
import com.example.domain.usecase.GetSearchHistoryUseCase
import com.example.domain.usecase.SearchMoviesLocallyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Ahmad Pahmi on June 2026
 */

/**
 * SearchViewModel – demonstrates advanced Flow composition.
 *
 * Key patterns:
 *  - `debounce`: Waits 300ms after the last keystroke before searching.
 *    Prevents a DB query on every character typed.
 *
 *  - `distinctUntilChanged`: Skips emissions when the query hasn't actually changed.
 *    Prevents redundant searches when the user types, deletes, retypes the same query.
 *
 *  - `flatMapLatest`: When a new query arrives, cancels the previous search Flow
 *    and starts a new one. This ensures we never show stale results from a
 *    superseded query — critical for fast typers.
 *
 *  - `combine`: Merges the query flow and search results flow into a single UiState.
 *    The UI observes ONE StateFlow, reducing recomposition surface area.
 *
 *  - `stateIn`: Converts a cold Flow to a hot StateFlow, shared among all collectors.
 *    SharingStarted.WhileSubscribed(5000) keeps the flow alive for 5 seconds
 *    after the last collector stops — handles configuration changes gracefully.
 */

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMoviesLocallyUseCase: SearchMoviesLocallyUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val addMovieToRecentSearchUseCase: AddMovieToRecentSearchUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")
    private val _isLoading = MutableStateFlow(false)

    /** Reactive search results driven by query changes. */
    @OptIn(FlowPreview::class)
    private val searchResults = _query
        .debounce(300L)
        .distinctUntilChanged()
        .onEach { query ->
            _isLoading.value = query.isNotEmpty()
        }
        .flatMapLatest { query ->
            if (query.length < 2) flowOf(emptyList())
            else searchMoviesLocallyUseCase(query)
        }
        .onEach {
            _isLoading.value = false
        }
        .catch { _isLoading.value = false }

    val uiState: StateFlow<SearchUiState> = combine(
        _query,
        _isLoading,
        searchResults,
        getSearchHistoryUseCase()
    ) { query, isLoading, results, history ->
        SearchUiState(
            query = query,
            isLoading = isLoading,
            searchResults = results,
            recentSearches = history.getOrNull().orEmpty(),
            error = null
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = SearchUiState()
    )

    fun onQueryChanged(query: String) {
        _query.value = query
    }

    fun onSearchSubmit(query: String) = viewModelScope.launch {
//        addMovieToRecentSearchUseCase(query)
    }

    fun onHistoryItemClicked(query: String) {
        _query.value = query
    }

    fun clearQuery() {
        _query.value = ""
    }
}