package com.example.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.model.Movie
import com.example.presentation.designsystem.AppElevation
import com.example.presentation.designsystem.AppShape
import com.example.presentation.designsystem.AppSpacing
import com.example.presentation.designsystem.MovieColors
import com.example.presentation.designsystem.MovieTypography

@Composable
fun MovieCard(
    movie: Movie,
    onClick: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(CardWidth)
            .semantics {
                role = Role.Button
                contentDescription = "${movie.title}, rated ${"%.1f".format(movie.voteAverage)}"
            }
            .clickable { onClick(movie) },
        shape = AppShape.Large,
        elevation = CardDefaults.cardElevation(defaultElevation = AppElevation.Card),
        colors = CardDefaults.cardColors(containerColor = MovieColors.CardBackground)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(AppShape.Large)
            ) {
                MoviePoster(
                    imageUrl = movie.posterPath,
                    title = movie.title,
                    modifier = Modifier.fillMaxWidth()
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MovieColors.CardOverlay
                                ),
                                startY = 220f
                            )
                        )
                )
                RatingBadge(
                    rating = movie.voteAverage,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(AppSpacing.S)
                )
            }

            Column(modifier = Modifier.padding(AppSpacing.M)) {
                Text(
                    text = movie.title,
                    style = MovieTypography.MovieSubtitle,
                    color = MovieColors.TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(AppSpacing.S))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    MetadataText(text = movie.releaseDate.take(4).ifBlank { "TBA" })
                    Spacer(modifier = Modifier.width(AppSpacing.S))
                    MetadataText(text = movie.originalLanguage.uppercase())
                }
            }
        }
    }
}

private val CardWidth = 156.dp

@Preview
@Composable
private fun MovieCardPreview() {
    MovieCard(
        movie = Movie(
            id = 1,
            title = "Inception",
            originalTitle = "Inception",
            originalLanguage = "en",
            overview = "A thief who enters dreams.",
            releaseDate = "2010-07-15",
            genreIds = listOf(28, 878),
            popularity = 120.5,
            voteAverage = 8.4,
            voteCount = 34500,
            posterPath = "",
            backdropPath = "",
            isAdult = false,
            includeVideo = false
        ),
        onClick = {}
    )
}
