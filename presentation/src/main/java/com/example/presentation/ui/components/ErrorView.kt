package com.example.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.presentation.designsystem.AppIconSize
import com.example.presentation.designsystem.AppSpacing
import com.example.presentation.designsystem.MovieColors
import com.example.presentation.designsystem.MovieTypography

/**
 * Created by Ahmad Pahmi on May 2026
 */

@Composable
fun ErrorView(
    title: String,
    description: String,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(AppSpacing.XXL),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.ErrorOutline,
            contentDescription = null,
            modifier = Modifier
                .padding(bottom = AppSpacing.L)
                .size(AppIconSize.XL),
            tint = MovieColors.ErrorColor
        )
        Spacer(modifier = Modifier.height(AppSpacing.L))
        Text(
            text = title,
            style = MovieTypography.SectionTitle,
            textAlign = TextAlign.Center,
            color = MovieColors.TextPrimary
        )
        Spacer(modifier = Modifier.height(AppSpacing.S))
        Text(
            text = description,
            style = MovieTypography.Body,
            textAlign = TextAlign.Center,
            color = MovieColors.TextSecondary
        )
        if (onRetry != null) {
            Spacer(modifier = Modifier.height(AppSpacing.L))
            PrimaryMovieButton(text = "Retry", onClick = onRetry)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorViewPreview() {
    ErrorView(
        title = "Something went wrong",
        description = "Please try again later",
        onRetry = {}
    )
}
