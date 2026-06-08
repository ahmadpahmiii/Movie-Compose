package com.example.data.di

import com.example.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by Ahmad Pahmi on June 2026
 */
class NetworkInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val requestBuilder = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer ${BuildConfig.API_KEY}")
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .method(originalRequest.method, originalRequest.body)

        return chain.proceed(requestBuilder.build())
    }
}