package com.example.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import com.example.domain.model.Movie
import com.example.presentation.designsystem.AppSpacing
import com.example.presentation.designsystem.MovieColors
import com.example.presentation.designsystem.MovieTypography

fun LazyListScope.movieSection(
    title: String,
    movies: List<Movie>,
    onMovieClick: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    if (movies.isEmpty()) return

    item(key = "section-$title", contentType = "movie-section") {
        MovieSection(
            title = title,
            movies = movies,
            onMovieClick = onMovieClick,
            modifier = modifier
        )
    }
}

@Composable
fun MovieSection(
    title: String,
    movies: List<Movie>,
    onMovieClick: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.L),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MovieTypography.SectionTitle,
                color = MovieColors.TextPrimary,
                modifier = Modifier.semantics { heading() }
            )
        }
        LazyRow(
            contentPadding = PaddingValues(horizontal = AppSpacing.L, vertical = AppSpacing.M),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.M)
        ) {
            items(
                items = movies,
                key = { movie -> movie.id },
                contentType = { "movie-card" }
            ) { movie ->
                MovieCard(movie = movie, onClick = onMovieClick)
            }
        }
    }
}
