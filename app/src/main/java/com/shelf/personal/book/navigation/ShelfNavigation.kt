package com.shelf.personal.book.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
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

sealed class Screen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    data object Home : Screen(
        route = "home",
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )
    data object Search : Screen(
        route = "search",
        title = "Search",
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search
    )
    data object Library : Screen(
        route = "library",
        title = "Library",
        selectedIcon = Icons.Filled.Book,
        unselectedIcon = Icons.Outlined.Book
    )
    data object Collections : Screen(
        route = "collections",
        title = "Shelves",
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.FavoriteBorder
    )
    data object Stats : Screen(
        route = "stats",
        title = "Stats",
        selectedIcon = Icons.Filled.DateRange,
        unselectedIcon = Icons.Outlined.DateRange
    )
    data object BookDetails : Screen(
        route = "details/{bookId}",
        title = "Details",
        selectedIcon = Icons.Filled.Book,
        unselectedIcon = Icons.Outlined.Book
    ) {
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
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    items.forEach { screen ->
                        val selected = currentRoute == screen.route
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (selected) screen.selectedIcon else screen.unselectedIcon,
                                    contentDescription = screen.title
                                )
                            },
                            label = {
                                Text(
                                    text = screen.title,
                                    style = MaterialTheme.typography.labelSmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            selected = selected,
                            alwaysShowLabel = true,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
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
