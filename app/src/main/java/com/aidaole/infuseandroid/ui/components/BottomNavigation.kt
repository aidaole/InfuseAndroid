package com.aidaole.infuseandroid.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.aidaole.infuseandroid.ui.navigation.Screen

data class BottomNavItem(
    val label: String,
    val icon: Int,
    val route: String
)

@Composable
fun InfuseBottomNavigation(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val items = listOf(
        BottomNavItem(
            label = "首页",
            icon = android.R.drawable.ic_menu_compass,
            route = Screen.Home.route
        ),
        BottomNavItem(
            label = "服务器",
            icon = android.R.drawable.ic_menu_manage,
            route = Screen.Servers.route
        ),
        BottomNavItem(
            label = "电影",
            icon = android.R.drawable.ic_menu_slideshow,
            route = Screen.Movies.route
        ),
        BottomNavItem(
            label = "电视剧",
            icon = android.R.drawable.ic_menu_view,
            route = Screen.TvShows.route
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(modifier = modifier) {
        items.forEach { item ->
            NavigationBarItem(
                icon = { 
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.label
                    )
                },
                label = { Text(text = item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(Screen.Home.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
} 