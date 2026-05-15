package com.example.playlistmaker.settings.ui.composable

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.example.playlistmaker.app.ui.theme.blue
import com.example.playlistmaker.app.ui.theme.grey
import com.example.playlistmaker.app.ui.theme.lightBlue
import com.example.playlistmaker.app.ui.theme.lightGrey

@Composable
fun CustomSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    thumbWidth: Dp = 16.dp,
    thumbHeight: Dp = 16.dp,
    trackHeight: Dp = 12.dp,
    checkedTrackColor: Color = lightBlue,
    uncheckedTrackColor: Color = lightGrey,
    checkedThumbColor: Color = blue,
    uncheckedThumbColor: Color = grey
) {
    val animationProgress = animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = tween(durationMillis = 200),
        label = "switchAnimation"
    )

    Box(
        modifier = Modifier
            .clickable(enabled = enabled) { onCheckedChange(!checked) }
            .size(
                width = thumbWidth * 2,
                height = maxOf(thumbHeight, trackHeight)
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        // Track
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(trackHeight)
                .clip(RoundedCornerShape(percent = 50))
                .background(
                    color = animateColorAsState(
                        targetValue = if (checked) checkedTrackColor else uncheckedTrackColor,
                        animationSpec = tween(durationMillis = 200),
                        label = "trackColor"
                    ).value
                )
        )

        // Thumb
        Box(
            modifier = Modifier
                .size(thumbWidth, thumbHeight)
                .offset(x = animationProgress.value * thumbWidth)
                .clip(CircleShape)
                .background(
                    color = animateColorAsState(
                        targetValue = if (checked) checkedThumbColor else uncheckedThumbColor,
                        animationSpec = tween(durationMillis = 200),
                        label = "trackColor"
                    ).value,
                    shape = CircleShape
                )
        )
    }
}

@Preview
@Composable
fun CustomSwitchPreview() {
    CustomSwitch(
        checked = false,
        onCheckedChange = { }
    )
}