package com.shelf.personal.book.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.shelf.personal.book.ui.screens.collections.CollectionsScreen
import com.shelf.personal.book.ui.screens.details.BookDetailsScreen
import com.shelf.personal.book.ui.screens.home.HomeScreen
import com.shelf.personal.book.ui.screens.library.LibraryScreen
import com.shelf.personal.book.ui.screens.search.SearchScreen
import com.shelf.personal.book.ui.screens.stats.StatsScreen

sealed class Screen(val route: String, val title: String) {
    data object Home : Screen("home", "Home")
    data object Search : Screen("search", "Search")
    data object Library : Screen("library", "Library")
    data object Collections : Screen("collections", "Collections")
    data object Stats : Screen("stats", "Intelligence")
    data object BookDetails : Screen("details/{bookId}", "Details") {
        fun createRoute(bookId: String) = "details/$bookId"
    }
}

@Composable
fun ShelfNavigation(
    navController: NavHostController = rememberNavController()
) {
    val items = listOf(Screen.Home, Screen.Search, Screen.Library, Screen.Collections, Screen.Stats)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in items.map { it.route }) {
                NavigationBar {
                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Text(screen.title.take(1)) },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding: PaddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onBookClick = { bookId ->
                        navController.navigate(Screen.BookDetails.createRoute(bookId))
                    }
                )
            }
            composable(Screen.Search.route) {
                SearchScreen(
                    onBookClick = { bookId ->
                        navController.navigate(Screen.BookDetails.createRoute(bookId))
                    }
                )
            }
            composable(Screen.Library.route) {
                LibraryScreen(
                    onBookClick = { bookId ->
                        navController.navigate(Screen.BookDetails.createRoute(bookId))
                    }
                )
            }
            composable(Screen.Collections.route) {
                CollectionsScreen()
            }
            composable(Screen.Stats.route) {
                StatsScreen()
            }
            composable(
                route = Screen.BookDetails.route,
                arguments = listOf(navArgument("bookId") { type = NavType.StringType })
            ) { backStackEntry ->
                val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
                BookDetailsScreen(
                    bookId = bookId,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
