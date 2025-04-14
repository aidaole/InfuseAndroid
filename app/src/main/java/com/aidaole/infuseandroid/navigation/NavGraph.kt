package com.aidaole.infuseandroid.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aidaole.infuseandroid.ui.screen.main.MainNavGraph

object AppDestinations {
    const val MAIN = "main"
}

@Composable
fun AppNavGraph(appNavController: NavHostController) {
    NavHost(
        navController = appNavController,
        startDestination = AppDestinations.MAIN
    ) {
        composable(AppDestinations.MAIN) {
            MainNavGraph(appNavController)
        }
    }
}
