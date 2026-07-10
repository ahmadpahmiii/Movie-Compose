package com.example.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.example.presentation.designsystem.AppShape

@Composable
fun MoviePoster(
    imageUrl: String,
    title: String,
    modifier: Modifier = Modifier,
    aspectRatio: Float = 2f / 3f,
    contentScale: ContentScale = ContentScale.Crop
) {
    var isLoading by remember(imageUrl) { mutableStateOf(true) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(aspectRatio)
            .clip(AppShape.Large)
            .semantics { contentDescription = "$title poster" }
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "$title poster",
            contentScale = contentScale,
            onState = { state -> isLoading = state is AsyncImagePainter.State.Loading },
            modifier = Modifier.fillMaxSize()
        )
        AnimatedVisibility(
            visible = isLoading,
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(shimmerBrush())
            )
        }
    }
}

@Preview
@Composable
private fun MoviePosterPreview() {
    MoviePoster(imageUrl = "", title = "Inception")
}
