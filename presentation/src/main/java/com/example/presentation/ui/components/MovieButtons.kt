package com.example.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.example.presentation.designsystem.AppShape
import com.example.presentation.designsystem.AppSpacing
import com.example.presentation.designsystem.MovieColors
import com.example.presentation.designsystem.MovieTypography

@Composable
fun PrimaryMovieButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = AppShape.Pill,
        contentPadding = PaddingValues(horizontal = AppSpacing.L, vertical = AppSpacing.M),
        colors = ButtonDefaults.buttonColors(
            containerColor = MovieColors.MovieAccent,
            contentColor = MovieColors.TextPrimary
        )
    ) {
        if (icon != null) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier)
        }
        Text(text = text, style = MovieTypography.Metadata)
    }
}

@Composable
fun SecondaryMovieButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = AppShape.Pill,
        contentPadding = PaddingValues(horizontal = AppSpacing.L, vertical = AppSpacing.M),
        border = BorderStroke(width = AppSpacing.XXS, color = MovieColors.DividerColor),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MovieColors.CardOverlay,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        if (icon != null) {
            Icon(imageVector = icon, contentDescription = null)
        }
        Text(text = text, style = MovieTypography.Metadata)
    }
}

@Preview
@Composable
private fun PrimaryMovieButtonPreview() {
    PrimaryMovieButton(text = "Watch", onClick = {}, icon = Icons.Filled.PlayArrow)
}
