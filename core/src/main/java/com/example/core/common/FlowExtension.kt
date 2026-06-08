package com.example.core.common

import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/**
 * Created by Ahmad Pahmi on May 2026
 */

/**
 * Wraps a Flow<T> into a Flow<Result<T>>.
 * Emits [State.Loading] on start, [State.Success] for each item,
 * and [State.Error] on any exception.
 */


fun <T> Flow<T>.asResult(): Flow<State<T>> {
    return this
        .map<T, State<T>> { State.Success(it) }
        .onStart { emit(State.Loading) }
        .catch { emit(State.Error(it.toAppException())) }
}

/**
 * Converts a generic [Throwable] to a typed [AppException].
 */
fun Throwable.toAppException(): AppException {
    return when (this) {
        is AppException -> this
        is UnknownHostException -> AppException.NoInternetException()
        is SocketTimeoutException -> AppException.TimeoutException()
        is IOException -> AppException.UnknownException(cause = this)
        else -> AppException.UnknownException(cause = this, message = message ?: "Unknown error")
    }
}