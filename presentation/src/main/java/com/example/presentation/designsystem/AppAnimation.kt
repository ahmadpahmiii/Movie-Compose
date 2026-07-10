package com.example.presentation.designsystem

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween

object AppAnimation {
    const val Fast = 150
    const val Normal = 300
    const val Slow = 500

    fun <T> fastTween() = tween<T>(durationMillis = Fast, easing = FastOutSlowInEasing)
    fun <T> normalTween() = tween<T>(durationMillis = Normal, easing = FastOutSlowInEasing)
    fun <T> slowTween() = tween<T>(durationMillis = Slow, easing = FastOutSlowInEasing)
}
