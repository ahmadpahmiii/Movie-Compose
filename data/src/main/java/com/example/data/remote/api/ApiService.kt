package com.example.data.remote.api

import com.example.data.remote.dto.MoviesDto
import retrofit2.Response
import retrofit2.http.GET

/**
 * Created by Ahmad Pahmi on May 2026
 */

interface ApiService {
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(): Response<MoviesDto>
}