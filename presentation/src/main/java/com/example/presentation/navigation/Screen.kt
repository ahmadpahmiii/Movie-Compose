package com.example.presentation.navigation

/**
 * Created by Ahmad Pahmi on May 2026
 */

/**
 * Sealed hierarchy of all navigation destinations.
 *
 * Architectural Decision: Using a sealed class hierarchy provides
 * compile-time exhaustiveness checking. When a new screen is added,
 * the compiler forces you to handle it everywhere — no silent omissions.
 */

sealed class Screen(val route: String) {
    // ─── Top-level (Tab) routes ───────────────────────────────────────────
    data object HomeGraph : Screen("home_graph")
    data object SearchGraph : Screen("search_graph")
    data object WishlistGraph : Screen("wishlist_graph")
    data object ProfileGraph : Screen("profile_graph")

    // ─── Home tab ─────────────────────────────────────────────────────────
    data object Home : Screen("home")

    // ─── Movie Detail (shared across tabs) ────────────────────────────────
    data object MovieDetail : Screen("movie_detail/{movieId}") {
        const val ARG_MOVIE_ID = "movieId"
        fun createRoute(movieId: String) = "movie_detail/$movieId"
    }

    // ─── Search tab ───────────────────────────────────────────────────────
    data object Search : Screen("search")

    // ─── Wishlist tab ─────────────────────────────────────────────────────
    data object Wishlist : Screen("wishlist")
}