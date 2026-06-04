package com.example.presentation.ui.search

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.Movie
import com.example.presentation.ui.components.EmptyView
import com.example.presentation.ui.components.MovieCard

/**
 * Created by Ahmad Pahmi on June 2026
 */

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
            .statusBarsPadding()
    ) {
        // Search Header
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Text("Search", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = uiState.query,
                onValueChange = onQueryChanged,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Movies, genres, actors...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
                trailingIcon = {
                    if (uiState.query.isNotEmpty()) {
                        IconButton(onClick = onClearQuery) {
                            Icon(Icons.Filled.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }

        when {
            uiState.isLoading -> CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(32.dp)
            )

            uiState.showHistory -> SearchHistory(
                searches = uiState.recentSearches,
                onHistoryClick = onHistoryItemClick
            )

            uiState.isEmpty -> EmptyView(
                title = "No Results for \"${uiState.query}\"",
                description = "Check the spelling or try a different term."
            )

            uiState.showResults -> SearchResultGrid(
                movies = uiState.searchResults,
                onMovieClick = onMovieClick
            )

            uiState.isIdle -> EmptyView(
                title = "Discover Movies",
                description = "Search by Title, genre, or language"
            )
        }
    }
}

@Composable
private fun SearchHistory(
    searches: List<String>,
    onHistoryClick: (String) -> Unit
) {
    LazyColumn(contentPadding = PaddingValues(vertical = 8.dp)) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) { Text("Recent Searches", style = MaterialTheme.typography.titleSmall) }
        }
        items(searches) { query ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onHistoryClick(query) }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.History,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(query, style = MaterialTheme.typography.bodyMedium)
            }
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = DividerDefaults.Thickness,
                color = DividerDefaults.color
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
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = movies, key = { it.id }) { movie ->
            MovieCard(movie = movie, onClick = onMovieClick)
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun SearchHistoryPreview() {
    SearchHistory(listOf("ashjd", "sjdhf")) { }
}

@Composable
@Preview(showBackground = true)
private fun SearchScreenPreview() {
    SearchScreen(
        uiState = SearchUiState(
            query = "dj",
            isLoading = false,
            searchResults = listOf(
                Movie(
                    id = "1",
                    title = "The Shawshank Redemption",
                    description = "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
                    posterUrl = "https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg",
                    releaseDate = "1994-09-23",
                    rating = "90",
//                    voteCount = 1484,
                    genres = emptyList(),
                    runtime = "142",
                    language = listOf("English"),
//                    popularity = 7.6
                )
            ),
            recentSearches = listOf("a", "b", "c"),
            error = null
        ),
        onQueryChanged = {},
        onQuerySubmit = { },
        onHistoryItemClick = {},
        onClearQuery = {},
        onMovieClick = {}
    )
}