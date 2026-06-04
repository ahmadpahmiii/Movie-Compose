package com.example.presentation.ui.wishlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
fun WishlistRoute(
    onMovieClick: (Movie) -> Unit,
    snackbarHostState: SnackbarHostState,
    viewModel: WishlistViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Show snackbar when a movie is removed
    LaunchedEffect(uiState.removedMovieTitle) {
        uiState.removedMovieTitle?.let { title ->
            val result = snackbarHostState.showSnackbar(
                message = "\"$title\" removed from wishlist",
                actionLabel = "Undo",
                duration = SnackbarDuration.Short
            )
            // TODO: implement undo via re-adding to wishlist
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
            .statusBarsPadding()
    ) {
        Text(
            text = "Wishlist",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )

        when {
            uiState.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            uiState.isEmpty -> EmptyView(
                title = "Your wishlist is empty",
                description = "Tap ♥ on any movie to save it here."
            )

            else -> LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items = uiState.movies, key = { it.id }) { movie ->
                    MovieCard(movie = movie, onClick = onMovieClick)
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