package com.example.data.remote

import com.example.core.common.AppException
import com.example.core.common.State
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import retrofit2.Response

/**
 * Created by Ahmad Pahmi on May 2026
 */

/**
 * Generic safe API call wrapper.
 *
 * Wraps a Retrofit [Response] in a [State], mapping HTTP errors
 * and network exceptions to typed [AppException] subclasses.
 *
 * This function centralizes all API error handling, keeping
 * repository implementations clean and DRY.
 */

suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): State<T> {
    return try {
        val response = apiCall()
        when {
            response.isSuccessful -> {
                val body = response.body()
                if (body != null) {
                    State.Success(body)
                } else {
                    State.Error(AppException.ParseException(message = "Response body is null"))
                }
            }

            response.code() == 404 -> State.Error(AppException.NotFoundException)
            response.code() in 400..499 -> State.Error(
                AppException.ApiException(response.code(), response.message())
            )

            response.code() in 500..599 -> State.Error(
                AppException.ApiException(response.code(), response.message())
            )

            else -> State.Error(AppException.UnknownException(message = "Unexpected ${response.code()}"))
        }
    } catch (_: UnknownHostException) {
        State.Error(AppException.NoInternetException)
    } catch (_: SocketTimeoutException) {
        State.Error(AppException.TimeoutException)
    } catch (e: Exception) {
        State.Error(AppException.UnknownException(cause = e))
    }
}