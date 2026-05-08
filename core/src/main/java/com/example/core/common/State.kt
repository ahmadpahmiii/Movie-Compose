package com.example.core.common

/**
 * Created by Ahmad Pahmi on May 2026
 */

sealed class State<out T> {
    data class Success<out T>(val data: T) : State<T>()
    data class Error(val exception: AppException) : State<Nothing>()
    data object Loading : State<Nothing>()

    val isSuccess: Boolean get() = this is Success

    val isError: Boolean get() = this is Error

    fun getOrNull(): T? = if (this is Success) data else null

    fun exceptionOrNull(): AppException? = if (this is Error) exception else null
}

/**
 * Transforms the data inside a [State.Success] using [transform].
 * Passes through [State.Error] and [State.Loading] unchanged.
 */
inline fun <T, R> State<T>.map(transform: (T) -> R): State<R> {
    return when (this) {
        is State.Success -> State.Success(transform(data))
        is State.Error -> this
        is State.Loading -> this
    }
}

/**
 * Executes [onSuccess] if this is [State.Success].
 * Returns the original result unchanged for chaining.
 */

inline fun <T> State<T>.onSuccess(onSuccess: (T) -> Unit): State<T> {
    if (this is State.Success) onSuccess(data)
    return this
}

/**
 * Executes [onError] if this is [State.Error].
 * Returns the original result unchanged for chaining.
 */