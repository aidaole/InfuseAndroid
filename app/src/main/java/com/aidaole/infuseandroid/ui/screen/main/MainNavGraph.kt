package com.aidaole.infuseandroid.ui.screen.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aidaole.infuseandroid.ui.screen.home.HomeScreen
import com.aidaole.infuseandroid.ui.screen.search.SearchScreen
import com.aidaole.infuseandroid.ui.screen.servers.ServersNavGraph
import com.aidaole.infuseandroid.ui.screen.settings.SettingNavGraph
import com.aidaole.infuseandroid.ui.theme.Orange

object MainScreenDestinations {
    const val HOME = "home"
    const val SEARCH = "search"
    const val SERVER = "server"
    const val SETTINGS = "settings"
}

// 主屏幕
@Composable
fun MainNavGraph(appNavController: NavHostController) {
    val mainNavController = rememberNavController()

    Scaffold(bottomBar = {
        CustomBottomNavigationBar(navController = mainNavController)
    }) { padding ->
        NavHost(
            navController = mainNavController,
            startDestination = MainScreenDestinations.HOME,
            modifier = Modifier.padding(padding)
        ) {
            composable(MainScreenDestinations.HOME) {
                HomeScreen(mainNavController)
            }
            composable(MainScreenDestinations.SEARCH) {
                SearchScreen(mainNavController)
            }
            composable(MainScreenDestinations.SERVER) {
                ServersNavGraph(mainNavController)
            }
            composable(MainScreenDestinations.SETTINGS) {
                SettingNavGraph(mainNavController)
            }
        }
    }
}

// 自定义底部导航栏
@Composable
fun CustomBottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomCenter
    ) {
        // 底部导航栏主体
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .shadow(
                    elevation = 8.dp, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                ),
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .selectableGroup(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // 首页按钮
                val homeSelected = currentRoute == MainScreenDestinations.HOME
                NavigationItem(icon = if (homeSelected) Icons.Filled.PlayArrow else Icons.Outlined.PlayArrow,
                    label = "首页",
                    selected = homeSelected,
                    onClick = {
                        if (currentRoute != MainScreenDestinations.HOME) {
                            navigateWithNoAnimation(navController, MainScreenDestinations.HOME)
                        }
                    })

                // 搜索按钮
                val searchSelected = currentRoute == MainScreenDestinations.SEARCH
                NavigationItem(
                    icon = if (searchSelected) Icons.Filled.Search else Icons.Outlined.Search,
                    label = "搜索",
                    selected = searchSelected,
                    onClick = {
                        if (currentRoute != MainScreenDestinations.SEARCH) {
                            navigateWithNoAnimation(navController, MainScreenDestinations.SEARCH)
                        }
                    },
                )

                // 文件按钮
                val serverSelected = currentRoute == MainScreenDestinations.SERVER
                NavigationItem(
                    icon = if (serverSelected) Icons.Filled.Folder else Icons.Outlined.Folder,
                    label = "文件",
                    selected = serverSelected,
                    onClick = {
                        if (currentRoute != MainScreenDestinations.SERVER) {
                            navigateWithNoAnimation(navController, MainScreenDestinations.SERVER)
                        }
                    },
                )

                // 设置按钮
                val settingsSelected = currentRoute == MainScreenDestinations.SETTINGS
                NavigationItem(
                    icon = if (settingsSelected) Icons.Filled.Settings else Icons.Outlined.Settings,
                    label = "设置",
                    selected = settingsSelected,
                    onClick = {
                        if (currentRoute != MainScreenDestinations.SETTINGS) {
                            navigateWithNoAnimation(navController, MainScreenDestinations.SETTINGS)
                        }
                    },
                )
            }
        }
    }
}

@Composable
fun NavigationItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier
    ) {
        IconButton(
            onClick = onClick, modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (selected) Orange else Color.Gray,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

fun navigateWithNoAnimation(navController: NavHostController, route: String) {
    navController.navigate(route) {
        launchSingleTop = true
        anim {
            this.enter = 0
            this.exit = 0
            this.popEnter = 0
            this.popExit = 0
        }
    }
}