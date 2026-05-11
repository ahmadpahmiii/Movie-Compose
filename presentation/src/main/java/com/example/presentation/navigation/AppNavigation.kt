package com.example.presentation.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.presentation.ui.moviedetail.MovieDetailRoute
import com.example.presentation.ui.movielist.MovieListRoute

/**
 * Created by Ahmad Pahmi on May 2026
 */

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.MovieList.route
    ) {
        composable(
            route = Screen.MovieList.route,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            MovieListRoute(
                onMovieClick = { movie ->
                    navController.navigate(Screen.MovieDetail.createRoute(movie.id))
                }
            )
        }

        composable(
            route = Screen.MovieDetail.route,
            arguments = listOf(
                navArgument(Screen.MovieDetail.ARG_MOVIE_ID) {
                    type = NavType.StringType
                }
            ),
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            MovieDetailRoute(
                onBackPressed = { navController.popBackStack() }
            )
        }
    }
}