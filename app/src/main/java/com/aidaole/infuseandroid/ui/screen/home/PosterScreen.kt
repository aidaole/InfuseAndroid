package com.aidaole.infuseandroid.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aidaole.infuseandroid.ui.theme.Orange
import com.aidaole.infuseandroid.ui.widgets.MainScreenTitle

@Composable
fun PosterScreen() {
    // 分类选项卡
    var selectedTabIndex by remember { mutableStateOf(0) }
    MainScreenTitle(
        "发现"
    )
    TabRow(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
        selectedTabIndex = selectedTabIndex,
        containerColor = Color.Transparent,
        contentColor = Orange,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                height = 3.dp,
                color = Orange
            )
        }) {
        Tab(
            selected = selectedTabIndex == 0,
            onClick = { selectedTabIndex = 0 },
            text = { Text("电影") },
            selectedContentColor = Orange,
            unselectedContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Tab(
            selected = selectedTabIndex == 1,
            onClick = { selectedTabIndex = 1 },
            text = { Text("电视剧") },
            selectedContentColor = Orange,
            unselectedContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }

    Spacer(modifier = Modifier.height(8.dp))

    // 海报网格
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 120.dp),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize().padding(start = 16.dp, end = 16.dp)
    ) {
        // TODO: 替换为实际数据
        items((1..20).toList()) { index ->
            PosterCard(title = "示例标题 $index",
                imageUrl = "https://via.placeholder.com/300x450",
                onClick = { /* TODO: 处理点击事件 */ })
        }
    }
}