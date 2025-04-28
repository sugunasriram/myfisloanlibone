package com.github.sugunasriram.myfisloanlibone.fis_code.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appGray84
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appSilver

@Composable
fun VerticalDivider(
    color: Color = appGray84, start: Dp = 24.dp, end: Dp = 0.dp, top: Dp = 0.dp, bottom: Dp = 0.dp,
    height: Dp = 35.dp, width: Dp = 1.dp
) {
    Divider(
        color = color,
        modifier = Modifier
            .padding(start = start, end = end, top = top, bottom = bottom)
            .height(height)
            .width(width)
    )
}

@Composable
fun HorizontalDivider(
    color: Color = appSilver, start: Dp = 24.dp, end: Dp = 24.dp, top: Dp = 0.dp,
    modifier: Modifier = Modifier.padding(start = start, end = end, top = top)
) {
    Divider(
        color = color, modifier = modifier.padding(vertical = 8.dp)
    )
}
