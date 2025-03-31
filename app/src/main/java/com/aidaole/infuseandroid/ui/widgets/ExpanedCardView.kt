package com.aidaole.infuseandroid.ui.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun ExpanedCardView(
    title: String,
    content: String
) {
    var isExpaned by remember { mutableStateOf(false) }
    var rotateState = animateFloatAsState(
        targetValue = if (isExpaned) {
            0f
        } else {
            180f
        }, animationSpec = tween(durationMillis = 300)
    )

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Title", modifier = Modifier.weight(1f)
            )
            IconButton(onClick = {
                isExpaned = !isExpaned
            }) {
                Icon(
                    modifier = Modifier.rotate(rotateState.value),
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "down")
            }
        }
        AnimatedVisibility(
            visible = isExpaned,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Text(text = "content".repeat(100))
        }

    }
}

@Preview
@Composable
fun defaultPreview() {
    ExpanedCardView(
        "Title",
        "this is content".repeat(100)
    )
}