package com.aidaole.infuseandroid.ui.screen.smb

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aidaole.infuseandroid.R

@Composable
fun AddServerItem(
    text: String = "ServerName",
    serverIcon: Painter = painterResource(R.drawable.ic_smb),
    onClicked: () -> Unit = {}
) {
    Row(
        modifier = Modifier.clickable { onClicked() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            tint = Color.Unspecified,
            painter = serverIcon,
            contentDescription = "Add Server",
            modifier = Modifier.size(25.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier.height(50.dp)
        ) {
            Spacer(modifier = Modifier.height(17.dp))
            Row {
                Text(
                    text = text,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Add Server",
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFE0E0E1),
                thickness = 1.dp
            )
        }

    }
}

@Composable
@Preview
fun addviewitemPreview() {
    AddServerItem()
}