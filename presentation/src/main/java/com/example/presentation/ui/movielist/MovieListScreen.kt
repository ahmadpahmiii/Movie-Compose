package com.example.presentation.ui.movielist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.domain.model.Movie
import com.example.presentation.theme.GoldRating
import com.example.presentation.ui.components.EmptyView
import com.example.presentation.ui.components.ErrorView
import com.example.presentation.ui.components.MovieCard
import com.example.presentation.ui.components.MovieListShimmer

/**
 * Created by Ahmad Pahmi on May 2026
 */

/**
 * Route composable – owns the ViewModel, connects state to the screen.
 *
 * Architectural Decision: Separating Route (stateful) from Screen (stateless)
 * allows Screen to be tested/previewed without Hilt or ViewModel injection.
 */

@Composable
fun MovieListRoute(
    onMovieClick: (Movie) -> Unit,
    viewModel: MovieListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    MovieListScreen(
        uiState = uiState,
        searchQuery = searchQuery,
        onMovieClick = onMovieClick,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onClearSearch = viewModel::clearSearch,
        onRefresh = viewModel::refresh
    )
}

@Composable
fun MovieListScreen(
    uiState: MovieListUiState,
    searchQuery: String,
    onMovieClick: (Movie) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onClearSearch: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isRefreshing = (uiState as? MovieListUiState.Success)?.isRefreshing == true
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Movie App",
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    SearchBarView(
                        query = searchQuery,
                        onQueryChanged = onSearchQueryChanged,
                        onClearSearch = onClearSearch
                    )
                }
            }

            when (uiState) {
                is MovieListUiState.Loading -> item {
                    Spacer(modifier = Modifier.height(16.dp))
                    MovieListShimmer()
                }

                is MovieListUiState.Success -> {
                    if (!uiState.isSearchActive && uiState.featuredMovie != null) {
                        item {
                            FeatureMovieBanner(
                                movie = uiState.featuredMovie,
                                onClick = onMovieClick,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                        }
                    }

                    // Section title
                    item {
                        Text(
                            text = if (uiState.isSearchActive) "Search Results" else "Popular Movies",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    // Movie Grid
                    if (uiState.displayMovies.isEmpty()) {
                        item {
                            EmptyView(
                                title = "No movies found ${uiState.searchQuery}",
                                description = "Try searching for something else"
                            )
                        }
                    } else {
                        item {
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.semantics {
                                    contentDescription = "Movie List"
                                }
                            ) {
                                items(
                                    items = uiState.displayMovies,
                                    key = { movie -> movie.id }
                                ) { movie ->
                                    MovieCard(
                                        movie = movie,
                                        onClick = onMovieClick
                                    )
                                }
                            }
                        }
                    }
                }

                is MovieListUiState.Error -> item {
                    ErrorView(
                        title = uiState.message,
                        description = uiState.message,
                        onRetry = onRefresh
                    )
                }

                is MovieListUiState.Empty -> item { EmptyView() }
            }
        }
    }
}

@Composable
private fun SearchBarView(
    query: String,
    onQueryChanged: (String) -> Unit,
    onClearSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text("Search movies...") },
        leadingIcon = {
            Icon(imageVector = Icons.Filled.Search, contentDescription = "search")
        },
        trailingIcon = {
            AnimatedVisibility(visible = query.isNotEmpty(), enter = fadeIn(), exit = fadeOut()) {
                IconButton(onClick = onClearSearch) {
                    Icon(imageVector = Icons.Filled.Clear, contentDescription = "clear")
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
        )
    )
}

@Composable
private fun FeatureMovieBanner(
    movie: Movie,
    onClick: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable { onClick.invoke(movie) }
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
    ) {
        AsyncImage(
            model = movie.posterPath,
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = GoldRating,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = " ${movie.voteAverage} - ${movie.releaseDate}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }
        }
    }
}

/*
@Composable
@Preview(showBackground = true)
private fun FeatureMovieBannerPreview() {
    FeatureMovieBanner(
        movie = Movie(
            id = "1",
            title = "The Shawshank Redemption",
            overview = "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
            posterUrl = "https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg",
            releaseDate = "1994-09-23",
            rating = 9.3,
            voteCount = 1484,
            genres = emptyList(),
            runtime = 142,
            language = "English",
            popularity = 7.6
        ),
        onClick = {}
    )
}
*/


@Composable
@Preview(showBackground = true)
private fun SearchBarViewPreview() {
    SearchBarView(
        query = "",
        onQueryChanged = {},
        onClearSearch = {}
    )
}

/*
@Composable
@Preview(showBackground = true, device = Devices.PIXEL_4, showSystemUi = true)
fun MoveListScreenPreview() {
    MovieListScreen(
        uiState = MovieListUiState.Success(
            movies = listOf(
                Movie(
                    id = "1",
                    title = "The Shawshank Redemption",
                    overview = "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
                    posterUrl = "https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg",
                    backdropUrl = "https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg",
                    releaseDate = "1994-09-23",
                    rating = 9.3,
                    voteCount = 1484,
                    genres = emptyList(),
                    runtime = 142,
                    language = "English",
                    popularity = 7.6
                )
            ),
            filteredMovies = emptyList(),
            featuredMovie = null
        ),
        searchQuery = "",
        onMovieClick = {},
        onSearchQueryChanged = {},
        onClearSearch = {},
        onRefresh = {}
    )
}*/
