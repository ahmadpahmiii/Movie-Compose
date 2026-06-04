package com.example.presentation.ui.moviedetail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.domain.model.Movie
import com.example.presentation.theme.GoldRating
import com.example.presentation.ui.components.ErrorView

/**
 * Created by Ahmad Pahmi on May 2026
 */

private val COLLAPSED_TOOLBAR_HEIGHT = 64.dp
private val EXPANDED_TOOLBAR_HEIGHT = 300.dp

@Composable
fun MovieDetailRoute(
    onBackPressed: () -> Unit,
    viewModel: MovieDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isWishlisted by viewModel.ishWishlisted.collectAsStateWithLifecycle()

    MovieDetailScreen(
        uiState = uiState,
        isWishlisted = isWishlisted,
        onBackPressed = onBackPressed,
        onToggleWishlist = viewModel::toggleWishlist,
        onRetry = viewModel::retry
    )
}

@Composable
fun MovieDetailScreen(
    uiState: MovieDetailUiState,
    isWishlisted: Boolean,
    onBackPressed: () -> Unit,
    onRetry: () -> Unit,
    onToggleWishlist: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (uiState) {
            is MovieDetailUiState.Loading -> CircularProgressIndicator(
                modifier = Modifier.align(
                    Alignment.Center
                )
            )

            is MovieDetailUiState.Success -> CollapsingMovieDetail(
                movie = uiState.movie, isWishlisted = isWishlisted,
                onBackPressed = onBackPressed, onToggleWishlist = onToggleWishlist
            )

            is MovieDetailUiState.Error -> ErrorView(
                title = uiState.message, description = uiState.message, onRetry = onRetry
            )
        }
    }
}

/**
 * Collapsing toolbar implementation using scroll-based interpolation.
 *
 * Architectural Decision: We use a plain ScrollState + derivedStateOf instead of
 * TopAppBarScrollBehavior. This gives us pixel-perfect control over the collapse
 * animation without coupling to Material3's internal scroll logic.
 *
 * derivedStateOf is critical here: it memoizes derived values so they only
 * recompute when scrollState value actually changes, not on every recomposition.
 */
@Composable
private fun CollapsingMovieDetail(
    movie: Movie,
    isWishlisted: Boolean,
    onBackPressed: () -> Unit,
    onToggleWishlist: () -> Unit
) {
    val scrollState = rememberScrollState()
    val density = LocalDensity.current

    val expandedHeightPx = with(density) { EXPANDED_TOOLBAR_HEIGHT.toPx() }
    val collapseHeightPx = with(density) { COLLAPSED_TOOLBAR_HEIGHT.toPx() }

    // derivedStateOf: Only recomputes when scrollState.value changes.
    // Without this, every recomposition would recalculate — wasteful.
    val collapseProgress by remember {
        derivedStateOf {
            (scrollState.value / (expandedHeightPx - collapseHeightPx)).coerceIn(0f, 1f)
        }
    }

    val toolbarAlpha by animateFloatAsState(
        targetValue = 1f - collapseProgress,
        label = "toolbar_alpha"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(EXPANDED_TOOLBAR_HEIGHT))

            // Content
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    RatingRow(movie)
                }
                Spacer(modifier = Modifier.height(16.dp))

                if (movie.genres.isNotEmpty()) {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        movie.genres.forEach { genre ->
                            AssistChip(
                                onClick = {},
                                label = { Text(genre) },
                                shape = RoundedCornerShape(20.dp)
                            )
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                }

                Text(
                    "Overview",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = movie.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
                Spacer(Modifier.height(80.dp))
            }
        }

        // Fixed header that collapses
        CollapsingHeader(
            movie = movie,
            collapseProgress = collapseProgress,
            backdropAlpha = toolbarAlpha,
            onBackPressed = onBackPressed
        )

        // Wishlist FAB
        FloatingActionButton(
            onClick = onToggleWishlist,
            containerColor = if (isWishlisted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .navigationBarsPadding()
        ) {
            Icon(
                imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "",
                tint = if (isWishlisted) Color.White else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun CollapsingHeader(
    movie: Movie,
    collapseProgress: Float,
    backdropAlpha: Float,
    onBackPressed: () -> Unit
) {
    val toolbarHeight = lerp(
        start = EXPANDED_TOOLBAR_HEIGHT,
        stop = COLLAPSED_TOOLBAR_HEIGHT,
        fraction = collapseProgress
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(toolbarHeight)
    ) {
        // Backdrop image fades out as toolbar collapses
        AsyncImage(
            model = movie.posterUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(backdropAlpha)
        )

        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Black.copy(0.4f), Color.Black.copy(0.7f))
                    )
                )
                .alpha(backdropAlpha)
        )

        // Navigation row (always visible)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 8.dp)
        ) {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = lerp(Color.White, MaterialTheme.colorScheme.onSurface, collapseProgress)
                )
            }

            // Title fades In when collapsed
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .alpha(collapseProgress),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun RatingRow(movie: Movie) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Filled.Favorite, null, tint = GoldRating, modifier = Modifier.size(16.dp))
        Text(" ${movie.rating} - ${movie.releaseDate} - ${movie.runtime}")
    }
}

// Lerp helper for Dp values
private fun lerp(
    start: androidx.compose.ui.unit.Dp,
    stop: androidx.compose.ui.unit.Dp,
    fraction: Float
): androidx.compose.ui.unit.Dp {
    return start + (stop - start) * fraction
}

/*@Composable
@Preview(showBackground = true)
private fun CollapsingMovieDetailPreview() {
    CollapsingMovieDetail(
        movie = Movie(
            id = "1",
            title = "The Shawshank Redemption",
            description = "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
            posterUrl = "https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg",
            releaseDate = "",
            runtime = "",
            rating = "",
            language = listOf(""),
            genres = listOf("")
        ),
        isWishlisted = true,
        onBackPressed = {},
        onToggleWishlist = {}
    )
}*/

@Composable
private fun MovieDetailContent(
    movie: Movie, onBackPressed: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Hero Backdrop
        Box(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(16f / 9f)
        ) {
            AsyncImage(
                model = movie.posterUrl,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Black.copy(0.3f), Color.Black.copy(0.7f))
                        )
                    )
            )

            IconButton(
                onClick = onBackPressed,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(8.dp)
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }

        // Content
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RatingBadge(movie.rating, 100)
                MetaItem(Icons.Filled.AccessTime, movie.runtime)
                MetaItem(Icons.Filled.Language, "Engleehs")
                Text(
                    text = movie.releaseDate,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            //Genres
            if (movie.genres.isNotEmpty()) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    movie.genres.forEach { genre ->
                        GenreChip(genre)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Overview
            Text(
                text = "Overview",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = movie.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
            )
        }
    }
}

@Composable
private fun RatingBadge(rating: String, voteCount: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = "Rating",
            tint = GoldRating,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = " $rating",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = GoldRating
        )
        Text(
            text = " ($voteCount)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RatingBadgePreview() {
    RatingBadge(rating = "9.3", voteCount = 1484)
}

@Composable()
private fun MetaItem(
    icon: ImageVector,
    text: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = " $text",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MetaItemPreview() {
    MetaItem(icon = Icons.Filled.Star, text = "9.3")
}

@Composable
private fun GenreChip(genre: String) {
    AssistChip(
        onClick = {},
        label = { Text(genre, style = MaterialTheme.typography.labelSmall) },
        shape = MaterialTheme.shapes.small
    )
}

@Preview(showBackground = true)
@Composable
fun MovieDetailScreenPreview() {
    MovieDetailScreen(
        uiState = MovieDetailUiState.Success(
            movie = Movie(
                id = "1",
                title = "The Shawshank Redemption",
                description = "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
                posterUrl = "https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg",
                releaseDate = "",
                runtime = "",
                rating = "",
                language = listOf(""),
                genres = listOf("")
            )
        ),
        onBackPressed = {},
        onToggleWishlist = {},
        isWishlisted = true,
        onRetry = {}
    )
}
