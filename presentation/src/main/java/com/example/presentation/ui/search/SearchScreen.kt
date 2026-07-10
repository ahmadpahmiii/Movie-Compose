package com.example.presentation.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.Movie
import com.example.presentation.designsystem.AppAnimation
import com.example.presentation.designsystem.AppIconSize
import com.example.presentation.designsystem.AppShape
import com.example.presentation.designsystem.AppSpacing
import com.example.presentation.designsystem.MovieColors
import com.example.presentation.designsystem.MovieTypography
import com.example.presentation.ui.components.EmptyView
import com.example.presentation.ui.components.MovieCard

@Composable
fun SearchRoute(
    onMovieClick: (Movie) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SearchScreen(
        uiState = uiState,
        onQueryChanged = viewModel::onQueryChanged,
        onQuerySubmit = viewModel::onSearchSubmit,
        onHistoryItemClick = viewModel::onHistoryItemClicked,
        onClearQuery = viewModel::clearQuery,
        onMovieClick = onMovieClick
    )
}

@Composable
fun SearchScreen(
    uiState: SearchUiState,
    onQueryChanged: (String) -> Unit,
    onQuerySubmit: (String) -> Unit,
    onHistoryItemClick: (String) -> Unit,
    onClearQuery: () -> Unit,
    onMovieClick: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MovieColors.PrimaryBackground)
            .statusBarsPadding()
    ) {
        SearchHeader(
            query = uiState.query,
            onQueryChanged = onQueryChanged,
            onQuerySubmit = onQuerySubmit,
            onClearQuery = onClearQuery
        )

        when {
            uiState.isLoading -> CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(AppSpacing.XXL),
                color = MovieColors.MovieAccent
            )

            uiState.showHistory -> SearchHistory(
                searches = uiState.recentSearches.map { it.title },
                onHistoryClick = onHistoryItemClick
            )

            uiState.isEmpty -> EmptyView(
                title = "No results for \"${uiState.query}\"",
                description = "Check the spelling or try a different term."
            )

            uiState.showResults -> SearchResultGrid(
                movies = uiState.searchResults,
                onMovieClick = onMovieClick
            )

            uiState.isIdle -> EmptyView(
                title = "Discover movies",
                description = "Search by title, genre, or language."
            )
        }
    }
}

@Composable
private fun SearchHeader(
    query: String,
    onQueryChanged: (String) -> Unit,
    onQuerySubmit: (String) -> Unit,
    onClearQuery: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = AppSpacing.L, vertical = AppSpacing.M)) {
        Text(
            text = "Search",
            style = MovieTypography.MovieTitle,
            color = MovieColors.TextPrimary,
            modifier = Modifier.semantics { heading() }
        )
        Spacer(modifier = Modifier.height(AppSpacing.L))
        OutlinedTextField(
            value = query,
            onValueChange = {
                onQueryChanged(it)
                if (it.isNotBlank()) onQuerySubmit(it)
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Movies, genres, actors...") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
            trailingIcon = {
                AnimatedVisibility(
                    visible = query.isNotEmpty(),
                    enter = fadeIn(animationSpec = AppAnimation.fastTween()),
                    exit = fadeOut(animationSpec = AppAnimation.fastTween())
                ) {
                    IconButton(onClick = onClearQuery) {
                        Icon(Icons.Filled.Clear, contentDescription = "Clear search")
                    }
                }
            },
            singleLine = true,
            shape = AppShape.Large,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MovieColors.SurfaceVariant,
                unfocusedContainerColor = MovieColors.SurfaceVariant,
                focusedIndicatorColor = MovieColors.MovieAccent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = MovieColors.MovieAccent
            )
        )
    }
}

@Composable
private fun SearchHistory(
    searches: List<String>,
    onHistoryClick: (String) -> Unit
) {
    LazyColumn(contentPadding = PaddingValues(vertical = AppSpacing.S)) {
        item(key = "history-title", contentType = "title") {
            Text(
                text = "Recent Searches",
                style = MovieTypography.SectionTitle,
                color = MovieColors.TextPrimary,
                modifier = Modifier
                    .padding(horizontal = AppSpacing.L, vertical = AppSpacing.S)
                    .semantics { heading() }
            )
        }
        items(
            items = searches,
            key = { it },
            contentType = { "history-item" }
        ) { query ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onHistoryClick(query) }
                    .padding(horizontal = AppSpacing.L, vertical = AppSpacing.M),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.History,
                    contentDescription = null,
                    tint = MovieColors.TextTertiary,
                    modifier = Modifier.size(AppIconSize.M)
                )
                Spacer(modifier = Modifier.width(AppSpacing.M))
                Text(query, style = MovieTypography.Body, color = MovieColors.TextPrimary)
            }
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = AppSpacing.L),
                color = MovieColors.DividerColor
            )
        }
    }
}

@Composable
private fun SearchResultGrid(
    movies: List<Movie>,
    onMovieClick: (Movie) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 156.dp),
        contentPadding = PaddingValues(AppSpacing.L),
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.M),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.L)
    ) {
        items(items = movies, key = { it.id }, contentType = { "movie-card" }) { movie ->
            MovieCard(movie = movie, onClick = onMovieClick, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun SearchHistoryPreview() {
    SearchHistory(listOf("Inception", "Arrival")) { }
}

@Composable
@Preview(showBackground = true)
private fun SearchScreenPreview() {
    SearchScreen(
        uiState = SearchUiState(
            query = "in",
            isLoading = false,
            searchResults = listOf(),
            error = null
        ),
        onQueryChanged = {},
        onQuerySubmit = { },
        onHistoryItemClick = {},
        onClearQuery = {},
        onMovieClick = {}
    )
}
