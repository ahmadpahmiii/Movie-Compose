package com.example.presentation.ui.wishlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.Movie
import com.example.presentation.designsystem.AppSpacing
import com.example.presentation.designsystem.MovieColors
import com.example.presentation.designsystem.MovieTypography
import com.example.presentation.ui.components.EmptyView
import com.example.presentation.ui.components.MovieCard

@Composable
fun WishlistRoute(
    onMovieClick: (Movie) -> Unit,
    snackbarHostState: SnackbarHostState,
    viewModel: WishlistViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.removedMovieTitle) {
        uiState.removedMovieTitle?.let { title ->
            snackbarHostState.showSnackbar(
                message = "\"$title\" removed from wishlist",
                actionLabel = "Undo",
                duration = SnackbarDuration.Short
            )
            viewModel.onSnackbarShown()
        }
    }

    WishlistScreen(
        uiState = uiState,
        onMovieClick = onMovieClick,
        onRemoveMovie = viewModel::removeFromWishlist
    )
}

@Composable
fun WishlistScreen(
    uiState: WishlistUiState,
    onMovieClick: (Movie) -> Unit,
    onRemoveMovie: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MovieColors.PrimaryBackground)
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.L, vertical = AppSpacing.M)
        ) {
            Text(
                text = "Wishlist",
                style = MovieTypography.MovieTitle,
                color = MovieColors.TextPrimary,
                modifier = Modifier.semantics { heading() }
            )
            Text(
                text = "Saved films for later",
                style = MovieTypography.Caption,
                color = MovieColors.TextTertiary
            )
        }

        when {
            uiState.isLoading -> CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(AppSpacing.XXL),
                color = MovieColors.MovieAccent
            )

            uiState.isEmpty -> EmptyView(
                title = "Your wishlist is empty",
                description = "Tap the heart on any movie to save it here."
            )

            else -> LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 156.dp),
                contentPadding = PaddingValues(AppSpacing.L),
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.M),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.L)
            ) {
                items(
                    items = uiState.movies,
                    key = { it.id },
                    contentType = { "movie-card" }) { movie ->
                    MovieCard(
                        movie = movie,
                        onClick = onMovieClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun WishlistScreenPreview() {
    WishlistScreen(
        WishlistUiState(),
        {},
        {}
    )
}
