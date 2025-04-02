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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.aidaole.infuseandroid.ui.screen.servers.SmbServiceScreen
import com.aidaole.infuseandroid.ui.screen.settings.SettingScreen
import com.aidaole.infuseandroid.ui.theme.Orange


// 主屏幕
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(bottomBar = {
        CustomBottomNavigationBar(navController = navController)
    }) { padding ->
        NavHost(
            navController = navController, startDestination = "home", modifier = Modifier.padding(padding)
        ) {
            composable("home") {
                HomeScreen(navController)
            }
            composable("search") {
                SearchScreen()
            }
            composable("server") {
                SmbServiceScreen()
            }
            composable("settings") {
                SettingScreen()
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
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
            color = Color.White,
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
                val homeSelected = currentRoute == "home"
                NavigationItem(icon = if (homeSelected) Icons.Filled.PlayArrow else Icons.Outlined.PlayArrow,
                    label = "首页",
                    selected = homeSelected,
                    onClick = {
                        if (currentRoute != "home") {
                            navigateWithNoAnimation(navController, "home")
                        }
                    })

                // 搜索按钮
                val searchSelected = currentRoute == "search"
                NavigationItem(
                    icon = if (searchSelected) Icons.Filled.Search else Icons.Outlined.Search,
                    label = "搜索",
                    selected = searchSelected,
                    onClick = {
                        if (currentRoute != "search") {
                            navigateWithNoAnimation(navController, "search")
                        }
                    },
                )

                // 文件按钮
                val serverSelected = currentRoute == "server"
                NavigationItem(
                    icon = if (serverSelected) Icons.Filled.Folder else Icons.Outlined.Folder,
                    label = "文件",
                    selected = serverSelected,
                    onClick = {
                        if (currentRoute != "server") {
                            navigateWithNoAnimation(navController, "server")
                        }
                    },
                )

                // 设置按钮
                val settingsSelected = currentRoute == "settings"
                NavigationItem(
                    icon = if (settingsSelected) Icons.Filled.Settings else Icons.Outlined.Settings,
                    label = "设置",
                    selected = settingsSelected,
                    onClick = {
                        if (currentRoute != "settings") {
                            navigateWithNoAnimation(navController, "settings")
                        }
                    },
                )
            }
        }
    }
}

@Composable
fun NavigationItem(
    icon: ImageVector, label: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier
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
//        popUpTo(navController.graph.startDestinationId)
        launchSingleTop = true
        anim {
            this.enter = 0
            this.exit = 0
            this.popEnter = 0
            this.popExit = 0
        }
    }
}