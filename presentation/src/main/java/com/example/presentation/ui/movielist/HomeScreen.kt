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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.domain.model.Movie
import com.example.presentation.designsystem.AppAnimation
import com.example.presentation.designsystem.AppShape
import com.example.presentation.designsystem.AppSpacing
import com.example.presentation.designsystem.MovieColors
import com.example.presentation.designsystem.MovieTypography
import com.example.presentation.ui.components.EmptyView
import com.example.presentation.ui.components.ErrorView
import com.example.presentation.ui.components.MetadataText
import com.example.presentation.ui.components.MovieListShimmer
import com.example.presentation.ui.components.PrimaryMovieButton
import com.example.presentation.ui.components.RatingBadge
import com.example.presentation.ui.components.SecondaryMovieButton
import com.example.presentation.ui.components.movieSection

@Composable
fun HomeScreenRoute(
    onMovieClick: (Movie) -> Unit,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    HomeScreen(
        uiState = uiState,
        searchQuery = searchQuery,
        onMovieClick = onMovieClick,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onClearSearch = viewModel::clearSearch,
        onRefresh = viewModel::refresh
    )
}

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    searchQuery: String,
    onMovieClick: (Movie) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onClearSearch: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isRefreshing = (uiState as? HomeUiState.Success)?.isRefreshing == true
    val listState = rememberLazyListState()

    val movies = (uiState as? HomeUiState.Success)?.movies ?: emptyList()
    val rankedMovies by derivedMovieSections(movies)

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier
            .fillMaxSize()
            .background(MovieColors.PrimaryBackground)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = AppSpacing.XL)
        ) {
            item(key = "home-header", contentType = "header") {
                HomeHeader(
                    searchQuery = searchQuery,
                    onSearchQueryChanged = onSearchQueryChanged,
                    onClearSearch = onClearSearch
                )
            }

            when (uiState) {
                is HomeUiState.Loading -> item(key = "loading", contentType = "loading") {
                    Spacer(modifier = Modifier.height(AppSpacing.L))
                    MovieListShimmer()
                }

                is HomeUiState.Success -> {
                    if (uiState.displayMovies.isEmpty()) {
                        item(key = "empty", contentType = "empty") {
                            EmptyView(
                                title = "No movies found for ${uiState.searchQuery}",
                                description = "Try another title, genre, or language."
                            )
                        }
                    } else if (uiState.isSearchActive) {
                        movieSection(
                            title = "Search Results",
                            movies = uiState.displayMovies,
                            onMovieClick = onMovieClick
                        )
                    } else {
                        val featuredMovie = uiState.featuredMovie ?: uiState.movies.firstOrNull()
                        if (featuredMovie != null) {
                            item(key = "hero-${featuredMovie.id}", contentType = "hero") {
                                HeroMovieCard(
                                    movie = featuredMovie,
                                    onMovieClick = onMovieClick,
                                    modifier = Modifier.padding(horizontal = AppSpacing.L)
                                )
                            }
                        }

                        movieSection("Trending Now", rankedMovies.trending, onMovieClick)
                        movieSection("Popular Movies", rankedMovies.popular, onMovieClick)
                        movieSection("Top Rated", rankedMovies.topRated, onMovieClick)
                        movieSection("Upcoming", rankedMovies.upcoming, onMovieClick)
                    }
                }

                is HomeUiState.Error -> item(key = "error", contentType = "error") {
                    ErrorView(
                        title = "Unable to load movies",
                        description = uiState.message,
                        onRetry = onRefresh
                    )
                }

                is HomeUiState.Empty -> item(key = "empty", contentType = "empty") { EmptyView() }
            }
        }
    }
}

@Composable
private fun HomeHeader(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onClearSearch: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = AppSpacing.L, vertical = AppSpacing.M)
    ) {
        Text(
            text = "Movie App",
            style = MovieTypography.AppTitle,
            color = MovieColors.TextPrimary,
            modifier = Modifier.semantics { heading() }
        )
        Spacer(modifier = Modifier.height(AppSpacing.S))
        Text(
            text = "Curated stories for tonight",
            style = MovieTypography.Caption,
            color = MovieColors.TextTertiary
        )
        Spacer(modifier = Modifier.height(AppSpacing.L))
        SearchBarView(
            query = searchQuery,
            onQueryChanged = onSearchQueryChanged,
            onClearSearch = onClearSearch
        )
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
            Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = query.isNotEmpty(),
                enter = fadeIn(animationSpec = AppAnimation.fastTween()),
                exit = fadeOut(animationSpec = AppAnimation.fastTween())
            ) {
                IconButton(onClick = onClearSearch) {
                    Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear search")
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

@Composable
private fun HeroMovieCard(
    movie: Movie,
    onMovieClick: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 10f)
            .clip(AppShape.ExtraLarge)
            .clickable { onMovieClick(movie) }
            .semantics {
                role = Role.Button
                contentDescription = "Featured movie ${movie.title}"
            }
    ) {
        AsyncImage(
            model = movie.backdropPath.ifBlank { movie.posterPath },
            contentDescription = "${movie.title} backdrop",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.05f),
                            MovieColors.Scrim
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(AppSpacing.L)
        ) {
            RatingBadge(rating = movie.voteAverage)
            Spacer(modifier = Modifier.height(AppSpacing.M))
            Text(
                text = movie.title,
                style = MovieTypography.MovieTitle,
                color = MovieColors.TextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(AppSpacing.S))
            Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.M)) {
                MetadataText(text = movie.releaseDate.take(4).ifBlank { "TBA" })
                MetadataText(text = movie.originalLanguage.uppercase())
            }
            Spacer(modifier = Modifier.height(AppSpacing.S))
            Text(
                text = movie.overview,
                style = MovieTypography.Body,
                color = MovieColors.TextSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(AppSpacing.L))
            Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.M)) {
                PrimaryMovieButton(
                    text = "Watch",
                    onClick = { onMovieClick(movie) },
                    icon = Icons.Filled.PlayArrow
                )
                SecondaryMovieButton(
                    text = "Save",
                    onClick = { onMovieClick(movie) },
                    icon = Icons.Filled.FavoriteBorder
                )
            }
        }
    }
}


@Composable
private fun derivedMovieSections(movies: List<Movie>) = remember(movies) {
    derivedStateOf {
        MovieSections(
            trending = movies.sortedByDescending(Movie::popularity).take(10),
            popular = movies.take(10),
            topRated = movies.sortedByDescending(Movie::voteAverage).take(10),
            upcoming = movies.sortedByDescending(Movie::releaseDate).take(10)
        )
    }
}

private data class MovieSections(
    val trending: List<Movie>,
    val popular: List<Movie>,
    val topRated: List<Movie>,
    val upcoming: List<Movie>
)

@Composable
@Preview(showBackground = true)
private fun SearchBarViewPreview() {
    SearchBarView(
        query = "",
        onQueryChanged = {},
        onClearSearch = {}
    )
}

@Composable
@Preview(showBackground = true, device = Devices.PIXEL_4, showSystemUi = true)
fun MoveListScreenPreview() {
    HomeScreen(
        uiState = HomeUiState.Success(
            movies = previewMovies(),
            filteredMovies = emptyList(),
            featuredMovie = previewMovies().first()
        ),
        searchQuery = "",
        onMovieClick = {},
        onSearchQueryChanged = {},
        onClearSearch = {},
        onRefresh = {}
    )
}

private fun previewMovies() = listOf(
    Movie(
        id = 1,
        title = "Inception",
        overview = "A thief who steals corporate secrets through dream-sharing technology is given the inverse task of planting an idea.",
        posterPath = "https://image.tmdb.org/t/p/w500/edv5CZvfk0YUPmUIBQPRO4s5B3y.jpg",
        backdropPath = "https://image.tmdb.org/t/p/w1280/8ZTPRkdUiM36vU90mG8vTS4OGvG.jpg",
        releaseDate = "2010-07-15",
        voteAverage = 8.4,
        voteCount = 34500,
        popularity = 120.5,
        originalLanguage = "en",
        genreIds = emptyList(),
        isAdult = false,
        includeVideo = false,
        originalTitle = "Inception"
    )
)
