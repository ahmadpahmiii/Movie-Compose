package com.example.presentation.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

object MovieTypography {
    val AppTitle: TextStyle
        @Composable get() = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Black)
    val MovieTitle: TextStyle
        @Composable get() = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
    val MovieSubtitle: TextStyle
        @Composable get() = MaterialTheme.typography.titleMedium
    val SectionTitle: TextStyle
        @Composable get() = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
    val Metadata: TextStyle
        @Composable get() = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
    val Body: TextStyle
        @Composable get() = MaterialTheme.typography.bodyMedium
    val Caption: TextStyle
        @Composable get() = MaterialTheme.typography.bodySmall
    val Rating: TextStyle
        @Composable get() = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
}
