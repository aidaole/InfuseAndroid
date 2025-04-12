package com.aidaole.infuseandroid.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.aidaole.infuseandroid.R
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import com.aidaole.infuseandroid.ui.widgets.MainScreenTitle

@OptIn(
    androidx.compose.foundation.ExperimentalFoundationApi::class
)
@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // 顶部标题
        HomeTopBar()

        // 主要内容区域
        MainContentSection()

        // 最近添加
        RecentlyAddedSection()

        // 电影类型
        MovieCategoriesSection()
    }
}

@Composable
fun HomeTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MainScreenTitle("主屏幕")
        Row {
            IconButton(onClick = { /* TODO */ }) {
                Icon(Icons.Default.Done, contentDescription = "下载")
            }
            IconButton(onClick = { /* TODO */ }) {
                Icon(Icons.Default.Settings, contentDescription = "设置")
            }
        }
    }
}

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun MainContentSection() {
    // 假设有 3 个页面
    val pageCount = 3
    val pagerState = rememberPagerState(pageCount = { pageCount })

    HorizontalPager(state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // Pager 的高度
            .padding(horizontal = 16.dp), // 左右留边距，如果需要全宽则移除
        key = { it } // 为每个页面提供唯一的 key
    ) { page ->
        Image(
            painter = painterResource(id = R.drawable.playceholder), // 可以根据 page 动态选择图片
            contentDescription = "主要内容 Page ${page + 1}",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(20.dp)),
        )
    }
}

@Composable
fun RecentlyAddedSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "最近添加", fontSize = 20.sp, fontWeight = FontWeight.Bold
            )
            TextButton(onClick = { /* TODO */ }) {
                Text("查看全部")
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(4) { index ->
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(180.dp)
                        .background(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }
    }
}

@Composable
fun MovieCategoriesSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "电影类型", fontSize = 20.sp, fontWeight = FontWeight.Bold
            )
            TextButton(onClick = { /* TODO */ }) {
                Text("查看全部")
            }
        }

        // 使用 LazyRow 替代 Row 以支持水平滚动
        LazyRow(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 添加更多项目以演示滚动
            items(6) { index -> // 例如，显示 6 个类别
                CategoryItem(
                    // modifier = Modifier.weight(1f), // LazyRow 中的项目通常不使用 weight
                    modifier = Modifier.width(150.dp), // 为项目设置固定宽度
                    title = "类别 ${index + 1}" // 使用示例标题
                )
            }
        }
    }
}

@Composable
fun CategoryItem(
    modifier: Modifier = Modifier, title: String
) {
    Box(
        modifier = modifier
            .height(100.dp)
            .background(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold
        )
    }
}

