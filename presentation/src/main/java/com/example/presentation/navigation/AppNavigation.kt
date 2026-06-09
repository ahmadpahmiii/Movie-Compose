package com.example.presentation.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.presentation.ui.moviedetail.MovieDetailRoute
import com.example.presentation.ui.movielist.HomeScreenRoute
import com.example.presentation.ui.search.SearchRoute
import com.example.presentation.ui.wishlist.WishlistRoute

/**
 * Created by Ahmad Pahmi on May 2026
 */

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val shouldShowBottomBar = currentDestination?.route != Screen.MovieDetail.route

    Scaffold(
        snackbarHost = { snackbarHostState },
        bottomBar = {
            if (shouldShowBottomBar) {
                MovieBottomBar(navController, currentDestination)
            }
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            snackbarHostState = snackbarHostState,
            modifier = Modifier.padding(innerPadding)
        )
    }
}


@Composable
private fun AppNavHost(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeGraph.route,
        modifier = modifier,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() }
    ) {
        // Home Graph
        navigation(
            route = Screen.HomeGraph.route,
            startDestination = Screen.Home.route
        ) {
            composable(route = Screen.Home.route) {
                HomeScreenRoute(
                    onMovieClick = { movie ->
                        navController.navigate(Screen.MovieDetail.createRoute(movie.id))
                    }
                )
            }
            movieDetailComposable(navController)
        }

        // Search Graph
        navigation(
            route = Screen.SearchGraph.route,
            startDestination = Screen.Search.route
        ) {
            composable(route = Screen.Search.route) {
                SearchRoute(
                    onMovieClick = { movie ->
                        navController.navigate(Screen.MovieDetail.createRoute(movie.id))
                    }
                )
            }

            movieDetailComposable(navController)
        }

        // Wishlist Graph
        navigation(
            route = Screen.WishlistGraph.route,
            startDestination = Screen.Wishlist.route
        ) {
            composable(route = Screen.Wishlist.route) {
                WishlistRoute(onMovieClick = { movie ->
                    navController.navigate(Screen.MovieDetail.createRoute(movie.id))
                }, snackbarHostState = snackbarHostState)
            }

            movieDetailComposable(navController)
        }
    }
}

/**
 * Extension function to avoid duplicating the MovieDetail composable
 * across every nested graph.
 *
 * Architectural Decision: Movie detail is a shared destination reachable
 * from multiple tabs. We declare it in each nested graph rather than
 * at the root level so back navigation returns to the correct tab.
 */
private fun NavGraphBuilder.movieDetailComposable(
    navController: NavHostController
) {
    composable(
        route = Screen.MovieDetail.route,
        arguments = listOf(
            navArgument(Screen.MovieDetail.ARG_MOVIE_ID) {
                type = NavType.IntType
            }
        ),
        enterTransition = { slideInHorizontally { it } + fadeIn() },
        exitTransition = { slideOutHorizontally { it } + fadeOut() },
        popEnterTransition = { slideInHorizontally { -it } + fadeIn() },
        popExitTransition = { slideOutHorizontally { -it } + fadeOut() }
    ) {
        MovieDetailRoute(
            onBackPressed = { navController.popBackStack() }
        )
    }
}

@Composable
private fun MovieBottomBar(
    navController: NavController,
    currentDestination: NavDestination?
) {
    NavigationBar {
        bottomNavItems.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any {
                it.route == item.graphRoute
            } == true

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) },
                selected = isSelected,
                onClick = {
                    navController.navigate(item.graphRoute) {
                        // Pop up to the start destination of the graph to avoid
                        // building up a large back stack across multiple tab switches.
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination
                        launchSingleTop = true
                        // Restore state when navigating back to a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun MovieBottomBarPreview() {
    MovieBottomBar(navController = rememberNavController(), currentDestination = null)
}