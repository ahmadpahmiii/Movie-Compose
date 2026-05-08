package com.example.data.remote.api

import com.example.data.remote.dto.MovieDto
import com.example.data.remote.dto.MoviesDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Ahmad Pahmi on May 2026
 */

interface ApiService {
    @GET("api/movies")
    suspend fun getMovies(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
        @Query("q") query: String? = null
    ): Response<MoviesDto>

    @GET("api/movies/{id}")
    suspend fun getMovieById(
        @Path("id") id: String
    ): Response<MovieDto>

    @GET("api/movies/rand")
    suspend fun getRandomMovie(): Response<MovieDto>
}