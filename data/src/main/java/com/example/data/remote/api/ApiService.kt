package com.example.data.remote.api

import com.example.data.remote.dto.MovieDetailDto
import com.example.data.remote.dto.MovieDto
import com.example.data.remote.dto.MoviesDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Ahmad Pahmi on May 2026
 */

interface ApiService {
    @GET("api/movies")
    suspend fun getMovies(): Response<MoviesDto>

    @GET("api/movies/{id}")
    suspend fun getMovieById(
        @Path("id") id: String
    ): Response<MovieDetailDto>

    @GET("api/movies/rand")
    suspend fun getRandomMovie(): Response<MovieDto>
}