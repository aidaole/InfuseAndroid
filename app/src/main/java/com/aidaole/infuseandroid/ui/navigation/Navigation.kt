package com.aidaole.infuseandroid.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Servers : Screen("servers")
    object Movies : Screen("movies")
    object TvShows : Screen("tvshows")
    object VideoPlayer : Screen("player/{videoPath}") {
        fun createRoute(videoPath: String): String = "player/$videoPath"
    }
}

@Composable
fun InfuseNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Home.route) {
            // 首页，显示最近播放和收藏内容
            HomeScreen(navController = navController)
        }
        
        composable(Screen.Servers.route) {
            // SMB服务器管理页面
            ServersScreen(navController = navController)
        }
        
        composable(Screen.Movies.route) {
            // 电影列表页面
            MoviesScreen(navController = navController)
        }
        
        composable(Screen.TvShows.route) {
            // 电视剧列表页面
            TvShowsScreen(navController = navController)
        }
        
        composable(Screen.VideoPlayer.route) { backStackEntry ->
            // 视频播放器页面
            val videoPath = backStackEntry.arguments?.getString("videoPath") ?: ""
            VideoPlayerScreen(videoPath = videoPath, navController = navController)
        }
    }
}

// 这些是占位函数，后面会一一实现
@Composable
fun HomeScreen(navController: NavHostController) {
    // TODO: 实现首页
}

@Composable
fun ServersScreen(navController: NavHostController) {
    // TODO: 实现服务器管理页面
}

@Composable
fun MoviesScreen(navController: NavHostController) {
    // TODO: 实现电影列表页面
}

@Composable
fun TvShowsScreen(navController: NavHostController) {
    // TODO: 实现电视剧列表页面
}

@Composable
fun VideoPlayerScreen(videoPath: String, navController: NavHostController) {
    // TODO: 实现视频播放器页面
} 