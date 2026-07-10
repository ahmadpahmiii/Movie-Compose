package com.example.presentation.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.designsystem.AppAnimation
import com.example.presentation.designsystem.AppShape
import com.example.presentation.designsystem.AppSpacing
import com.example.presentation.designsystem.MovieColors

/**
 * Created by Ahmad Pahmi on May 2026
 */

/**
 * Creates a shimmer brush animation for loading placeholders.
 * Reusable across any composable that needs a loading skeleton.
 */

@Composable
fun shimmerBrush(showShimmer: Boolean = true): Brush {
    val shimmerColors = listOf(
        MovieColors.SurfaceVariant.copy(alpha = 0.95f),
        Color.White.copy(alpha = 0.08f),
        MovieColors.SurfaceVariant.copy(alpha = 0.95f)
    )

    return if (showShimmer) {
        val transition = rememberInfiniteTransition(label = "shimmer")
        val translateAnimation by transition.animateFloat(
            initialValue = 0f,
            targetValue = 1000f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = AppAnimation.Slow * 2),
                repeatMode = RepeatMode.Restart
            ),
            label = "shimmer_translate"
        )
        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset.Zero,
            end = Offset(x = translateAnimation, y = translateAnimation)
        )
    } else Brush.linearGradient(colors = listOf(Color.Transparent, Color.Transparent))
}

@Preview(showBackground = true)
@Composable
fun MovieCardShimmer(modifier: Modifier = Modifier) {
    val brush = shimmerBrush()
    Column(modifier = modifier.width(156.dp)) {
        Box(
            modifier = Modifier
                .width(156.dp)
                .aspectRatio(2f / 3f)
                .clip(AppShape.Large)
                .background(brush)
        )
        Spacer(modifier = Modifier.height(AppSpacing.S))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(AppSpacing.L)
                .clip(AppShape.Small)
                .background(brush)
        )
        Spacer(modifier = Modifier.height(AppSpacing.XS))
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(AppSpacing.M)
                .clip(AppShape.Small)
                .background(brush)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MovieListShimmer(modifier: Modifier = Modifier) {
    LazyRow(modifier = modifier.padding(horizontal = AppSpacing.L)) {
        items(6) {
            MovieCardShimmer(modifier = Modifier.padding(end = AppSpacing.M))
        }
    }
}
