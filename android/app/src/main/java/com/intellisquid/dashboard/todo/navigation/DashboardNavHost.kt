package com.intellisquid.dashboard.todo.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.intellisquid.dashboard.todo.ui.DashboardViewModel
import com.intellisquid.dashboard.todo.ui.screens.ArchiveScreen
import com.intellisquid.dashboard.todo.ui.screens.BackupScreen
import com.intellisquid.dashboard.todo.ui.screens.DashboardScreen

private enum class Destination(val route: String, val label: String) {
    Dashboard("dashboard", "To-Do"),
    Archive("archive", "Archive"),
    Backup("backup", "Backup"),
}

@Composable
fun DashboardApp(viewModel: DashboardViewModel) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val message by viewModel.message.collectAsState()

    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.consumeMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination

            NavigationBar {
                Destination.values().forEach { destination ->
                    NavigationBarItem(
                        selected = currentRoute?.hierarchy?.any { it.route == destination.route } == true,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            val icon = when (destination) {
                                Destination.Dashboard -> Icons.Filled.Checklist
                                Destination.Archive -> Icons.Filled.Archive
                                Destination.Backup -> Icons.Filled.CloudSync
                            }
                            Icon(icon, contentDescription = destination.label)
                        },
                        label = { Text(destination.label) },
                    )
                }
            }
        },
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = Destination.Dashboard.route) {
            composable(Destination.Dashboard.route) { DashboardScreen(viewModel, innerPadding) }
            composable(Destination.Archive.route) { ArchiveScreen(viewModel, innerPadding) }
            composable(Destination.Backup.route) { BackupScreen(viewModel, innerPadding) }
        }
    }
}
