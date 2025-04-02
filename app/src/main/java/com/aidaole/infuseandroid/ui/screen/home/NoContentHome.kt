package com.aidaole.infuseandroid.ui.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aidaole.infuseandroid.R

@Composable
fun NoContentHome() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            tint = Color.Unspecified,
            painter = painterResource(R.drawable.ic_infuse),
            contentDescription = "infuse icon"
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            "欢迎使用Infuse Android",
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            "现在让我们把一些视频添加到您的媒体库中",
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(60.dp))
        Button(
            onClick = { /*TODO*/ }, modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("新增文件来源")
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
@Preview
private fun NoContentHomePreview() {
    NoContentHome()
}
