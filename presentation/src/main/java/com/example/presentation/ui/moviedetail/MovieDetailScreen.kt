package com.example.presentation.ui.moviedetail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.domain.model.Movie
import com.example.presentation.designsystem.AppAnimation
import com.example.presentation.designsystem.AppIconSize
import com.example.presentation.designsystem.AppMovieSize
import com.example.presentation.designsystem.AppSpacing
import com.example.presentation.designsystem.MovieColors
import com.example.presentation.designsystem.MovieTypography
import com.example.presentation.ui.components.ErrorView
import com.example.presentation.ui.components.GenreChip
import com.example.presentation.ui.components.MetadataText
import com.example.presentation.ui.components.MoviePoster
import com.example.presentation.ui.components.PrimaryMovieButton
import com.example.presentation.ui.components.RatingBadge
import androidx.compose.ui.graphics.lerp as lerpColor

/**
 * Created by Ahmad Pahmi on May 2026
 */

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
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MovieColors.PrimaryBackground)
    ) {
        when (uiState) {
            is MovieDetailUiState.Loading -> CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MovieColors.MovieAccent
            )

            is MovieDetailUiState.Success -> CollapsingMovieDetail(
                movie = uiState.movie,
                isWishlisted = isWishlisted,
                onBackPressed = onBackPressed,
                onToggleWishlist = onToggleWishlist
            )

            is MovieDetailUiState.Error -> ErrorView(
                title = "Unable to load movie",
                description = uiState.message,
                onRetry = onRetry
            )
        }
    }
}

@Composable
private fun CollapsingMovieDetail(
    movie: Movie,
    isWishlisted: Boolean,
    onBackPressed: () -> Unit,
    onToggleWishlist: () -> Unit
) {
    val scrollState = rememberScrollState()
    val density = LocalDensity.current
    val expandedHeightPx = with(density) { AppMovieSize.DetailToolbarExpanded.toPx() }
    val collapseHeightPx = with(density) { AppMovieSize.DetailToolbarCollapsed.toPx() }
    val collapseProgress by remember {
        derivedStateOf {
            (scrollState.value / (expandedHeightPx - collapseHeightPx)).coerceIn(0f, 1f)
        }
    }
    val backdropAlpha by animateFloatAsState(
        targetValue = 1f - collapseProgress,
        animationSpec = AppAnimation.normalTween(),
        label = "detail_backdrop_alpha"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .navigationBarsPadding()
        ) {
            Spacer(modifier = Modifier.height(AppMovieSize.DetailToolbarExpanded))
            MovieDetailBody(
                movie = movie,
                isWishlisted = isWishlisted,
                onToggleWishlist = onToggleWishlist
            )
        }

        CollapsingHeader(
            movie = movie,
            collapseProgress = collapseProgress,
            backdropAlpha = backdropAlpha,
            onBackPressed = onBackPressed
        )
    }
}

@Composable
private fun MovieDetailBody(
    movie: Movie,
    isWishlisted: Boolean,
    onToggleWishlist: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MovieColors.PrimaryBackground)
            .padding(AppSpacing.L)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.L),
            verticalAlignment = Alignment.Top
        ) {
            MoviePoster(
                imageUrl = movie.posterPath,
                title = movie.title,
                modifier = Modifier.width(AppMovieSize.DetailPosterWidth)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = movie.title,
                    style = MovieTypography.MovieTitle,
                    color = MovieColors.TextPrimary,
                    modifier = Modifier.semantics { heading() }
                )
                Spacer(modifier = Modifier.height(AppSpacing.M))
                DetailMetadata(movie = movie)
                Spacer(modifier = Modifier.height(AppSpacing.L))
                PrimaryMovieButton(
                    text = if (isWishlisted) "Saved" else "Add to list",
                    onClick = onToggleWishlist,
                    icon = if (isWishlisted) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        if (movie.genreIds.isNotEmpty()) {
            Spacer(modifier = Modifier.height(AppSpacing.XL))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.S),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.S)
            ) {
                movie.genreIds.forEach { genreId ->
                    GenreChip(genre = "Genre $genreId")
                }
            }
        }

        Spacer(modifier = Modifier.height(AppSpacing.XL))
        Text(
            text = "Overview",
            style = MovieTypography.SectionTitle,
            color = MovieColors.TextPrimary,
            modifier = Modifier.semantics { heading() }
        )
        Spacer(modifier = Modifier.height(AppSpacing.S))
        Text(
            text = movie.overview.ifBlank { "No overview available yet." },
            style = MovieTypography.Body,
            color = MovieColors.TextSecondary
        )
        Spacer(modifier = Modifier.height(AppSpacing.XXXL))
    }
}

@Composable
private fun DetailMetadata(movie: Movie) {
    Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.S)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.S)
        ) {
            RatingBadge(rating = movie.voteAverage)
            MetadataText(text = movie.releaseYear())
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.S)
        ) {
            MetadataIcon(
                icon = Icons.Filled.Language,
                contentDescription = "Original language"
            )
            MetadataText(text = movie.originalLanguage.uppercase())
            MetadataIcon(
                icon = Icons.Filled.Star,
                contentDescription = "Vote count"
            )
            MetadataText(text = "${movie.voteCount} votes")
        }
    }
}

@Composable
private fun MetadataIcon(
    icon: ImageVector,
    contentDescription: String
) {
    Icon(
        imageVector = icon,
        contentDescription = contentDescription,
        tint = MovieColors.TextTertiary,
        modifier = Modifier.size(AppIconSize.S)
    )
}

@Composable
private fun CollapsingHeader(
    movie: Movie,
    collapseProgress: Float,
    backdropAlpha: Float,
    onBackPressed: () -> Unit
) {
    val toolbarHeight = lerp(
        start = AppMovieSize.DetailToolbarExpanded,
        stop = AppMovieSize.DetailToolbarCollapsed,
        fraction = collapseProgress
    )
    val titleAlpha by animateFloatAsState(
        targetValue = collapseProgress,
        animationSpec = AppAnimation.fastTween(),
        label = "detail_title_alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(toolbarHeight)
            .background(MovieColors.PrimaryBackground)
    ) {
        AsyncImage(
            model = movie.backdropPath.ifBlank { movie.posterPath },
            contentDescription = "${movie.title} backdrop",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(backdropAlpha)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(backdropAlpha)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MovieColors.Scrim.copy(alpha = 0.12f),
                            MovieColors.PrimaryBackground.copy(alpha = 0.45f),
                            MovieColors.PrimaryBackground
                        )
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(AppMovieSize.DetailToolbarCollapsed)
                .align(Alignment.TopCenter)
                .alpha(collapseProgress)
                .background(MovieColors.PrimaryBackground.copy(alpha = 0.92f))
        )
        DetailTopBar(
            title = movie.title,
            titleAlpha = titleAlpha,
            collapseProgress = collapseProgress,
            onBackPressed = onBackPressed
        )
    }
}

@Composable
private fun DetailTopBar(
    title: String,
    titleAlpha: Float,
    collapseProgress: Float,
    onBackPressed: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = AppSpacing.S),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackPressed,
            modifier = Modifier.semantics { contentDescription = "Navigate back" }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = lerpColor(Color.White, MovieColors.TextPrimary, collapseProgress)
            )
        }
        Text(
            text = title,
            style = MovieTypography.MovieSubtitle,
            color = MovieColors.TextPrimary,
            modifier = Modifier
                .padding(start = AppSpacing.S)
                .alpha(titleAlpha)
                .weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private fun Movie.releaseYear(): String {
    return releaseDate.takeIf { it.length >= 4 }?.take(4) ?: "TBA"
}

private fun lerp(
    start: androidx.compose.ui.unit.Dp,
    stop: androidx.compose.ui.unit.Dp,
    fraction: Float
): androidx.compose.ui.unit.Dp = start + (stop - start) * fraction

@Preview(showBackground = true)
@Composable
fun MovieDetailScreenPreview() {
    MovieDetailScreen(
        uiState = MovieDetailUiState.Success(
            movie = Movie(
                id = 1,
                title = "Inception",
                overview = "A thief who steals corporate secrets through dream-sharing technology is given the inverse task of planting an idea.",
                posterPath = "https://image.tmdb.org/t/p/w500/edv5CZv0jH9NXN2FU6N2gaDNBzH.jpg",
                backdropPath = "https://image.tmdb.org/t/p/w500/8ZTPjS7SBy96z99v9uURM9P9S9P.jpg",
                releaseDate = "2010-07-15",
                voteAverage = 8.4,
                voteCount = 34500,
                originalLanguage = "en",
                genreIds = listOf(28, 878, 12),
                isAdult = false,
                includeVideo = false,
                popularity = 8.0,
                originalTitle = "Inception"
            )
        ),
        onBackPressed = {},
        onToggleWishlist = {},
        isWishlisted = true,
        onRetry = {}
    )
}
