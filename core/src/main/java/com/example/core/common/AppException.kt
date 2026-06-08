package com.example.core.common

/**
 * Created by Ahmad Pahmi on May 2026
 */

/**
 * Sealed class representing domain-specific errors.
 *
 * Architectural Decision: Mapping raw exceptions to domain exceptions at the
 * repository layer isolates network/IO concerns from business logic.
 * The domain layer never sees Retrofit or IO exceptions directly.
 */

sealed class AppException(
    override val message: String? = null,
    override val cause: Throwable? = null
) : Exception(message, cause) {

    /** HTTP error from the remote API (4xx, 5xx). */
    data class ApiException(
        val code: Int,
        override val message: String
    ) : AppException(message)

    /** Device has no internet connectivity. */
    data class NoInternetException(
        override val message: String = "No internet connection available"
    ) : AppException(message)

    /** Request timed out. */
    data class TimeoutException(
        override val message: String = "Request timed out"
    ) : AppException(message)

    /** The requested resource was not found (404). */
    data class NotFoundException(
        override val message: String = "Resource not found"
    ) : AppException(message)

    /** Unexpected/unknown error. */
    data class UnknownException(
        override val cause: Throwable? = null,
        override val message: String = "An unexpected error occurred"
    ) : AppException(message, cause)

    /** Serialization/parsing error. */
    data class ParseException(
        override val message: String = "Failed to parse server response",
        override val cause: Throwable? = null
    ) : AppException(message, cause)
}