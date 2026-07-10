package com.example.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.designsystem.AppIconSize
import com.example.presentation.designsystem.AppShape
import com.example.presentation.designsystem.AppSpacing
import com.example.presentation.designsystem.MovieColors
import com.example.presentation.designsystem.MovieTypography

@Composable
fun RatingBadge(
    rating: Double,
    modifier: Modifier = Modifier,
    showIcon: Boolean = true
) {
    Row(
        modifier = modifier
            .clip(AppShape.Pill)
            .background(MovieColors.CardOverlay)
            .padding(horizontal = AppSpacing.S, vertical = AppSpacing.XS)
            .semantics { contentDescription = "Rating ${"%.1f".format(rating)} out of 10" },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.XS)
    ) {
        if (showIcon) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = MovieColors.RatingColor,
                modifier = Modifier.size(AppIconSize.S)
            )
        }
        Text(
            text = "%.1f".format(rating),
            style = MovieTypography.Rating,
            color = MovieColors.TextPrimary
        )
    }
}

@Composable
fun MetadataText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MovieTypography.Metadata,
        color = MovieColors.TextSecondary,
        modifier = modifier
    )
}

@Composable
fun GenreChip(genre: String, modifier: Modifier = Modifier) {
    AssistChip(
        onClick = {},
        label = { Text(text = genre, style = MovieTypography.Caption) },
        modifier = modifier,
        shape = AppShape.Pill,
        colors = AssistChipDefaults.assistChipColors(
            containerColor = MovieColors.SurfaceVariant,
            labelColor = MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(1.dp, MovieColors.DividerColor)
    )
}

@Preview
@Composable
private fun RatingBadgePreview() {
    RatingBadge(rating = 8.4)
}
