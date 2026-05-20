package com.example.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Created by Ahmad Pahmi on May 2026
 */

/**
 * Defines a bottom navigation tab.
 *
 * Uses separate selected/unselected icons for a polished Material 3 feel.
 * The [graphRoute] is the root of the nested NavGraph for this tab.
 */

data class BottomNavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val graphRoute: String,
    val startRoute: String
)

val bottomNavItems = listOf(
    BottomNavItem(
        label = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        graphRoute = Screen.HomeGraph.route,
        startRoute = Screen.Home.route
    ),
    BottomNavItem(
        label = "Search",
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search,
        graphRoute = Screen.SearchGraph.route,
        startRoute = Screen.Search.route
    ),
    BottomNavItem(
        label = "Wishlist",
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.Favorite,
        graphRoute = Screen.WishlistGraph.route,
        startRoute = Screen.Wishlist.route
    ),
    BottomNavItem(
        label = "Profile",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        graphRoute = Screen.ProfileGraph.route,
        startRoute = Screen.Profile.route
    )
)