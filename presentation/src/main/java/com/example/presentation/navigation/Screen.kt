package com.example.presentation.navigation

/**
 * Created by Ahmad Pahmi on May 2026
 */

sealed class Screen(val route: String) {
    data object MovieList : Screen("movie_list")
    data object MovieDetail : Screen("movie_detail/{movieId}") {
        const val ARG_MOVIE_ID = "movieId"
        fun createRoute(movieId: String) = "movie_detail/$movieId"
    }
}