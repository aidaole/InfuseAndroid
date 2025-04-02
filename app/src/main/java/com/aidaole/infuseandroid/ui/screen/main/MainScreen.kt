package com.aidaole.infuseandroid.ui.screen.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aidaole.infuseandroid.ui.screen.display.DisplayScreen
import com.aidaole.infuseandroid.ui.screen.servers.SmbServiceScreen
import com.aidaole.infuseandroid.ui.screen.user.UserScreen


// 主屏幕
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { padding ->
        // 使用自定义的无动画NavHost
        NoAnimationNavHost(
            navController = navController,
            startDestination = "display",
            modifier = Modifier.padding(padding)
        ) {
            composable("display") {
                DisplayScreen()
            }
            composable("server") {
                SmbServiceScreen()
            }
            composable("user") {
                UserScreen()
            }
        }
    }
}

// 自定义无动画NavHost
@Composable
fun NoAnimationNavHost(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier,
    builder: NavGraphBuilder.() -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        builder = builder
    )
}

// 底部导航栏组件
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.name) },
                label = { Text(item.name) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            // 防止重复点击同一项时堆栈重复
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                            // 禁用动画
                            anim {
                                this.enter = 0
                                this.exit = 0
                                this.popEnter = 0
                                this.popExit = 0
                            }
                        }
                    }
                }
            )
        }
    }
}

val bottomNavItems = listOf(
    BottomNavItem(
        name = "display",
        route = "display",
        icon = Icons.Default.Favorite
    ),
    BottomNavItem(
        name = "Server",
        route = "server",
        icon = Icons.Default.Share
    ),
    BottomNavItem(
        name = "user",
        route = "user",
        icon = Icons.Default.Person
    )
)


data class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector
)