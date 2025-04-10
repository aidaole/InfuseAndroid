package com.aidaole.infuseandroid.ui.screen.files

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Text
import com.aidaole.infuseandroid.data.model.FavoriteFolder

@Composable
@Preview
fun preview() {
    FilesScreen()
}

@Composable
fun FilesScreen(

) {
    val favoriteFolders = listOf<FavoriteFolder>()

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {

        item {
            Column(
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                Text("文件", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(5.dp))
                HorizontalDivider()
            }
        }
        items(favoriteFolders) {

        }
    }
}

@Composable
fun FolderItemView() {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {

    }
}
