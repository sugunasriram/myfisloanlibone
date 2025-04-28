package com.github.sugunasriram.myfisloanlibone.fis_code.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlack
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal14Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.primaryBlue

@Composable
fun CheckBoxText(
    boxState: Boolean, textColor: Color = appBlack, modifier: Modifier = Modifier,
    style: TextStyle = normal14Text400, start: Dp = 10.dp, end: Dp = 10.dp, bottom: Dp = 10.dp,
    top: Dp = 0.dp, boxStart: Dp = 10.dp, text: String, onCheckedChange: ((Boolean) -> Unit)
) {
    Row(
        modifier = modifier.padding(start = start, bottom = bottom, end = end, top = top),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = boxState, onCheckedChange = onCheckedChange,
            modifier = Modifier.padding(start = boxStart),
            colors = CheckboxDefaults.colors(
                checkedColor = primaryBlue,
                uncheckedColor = primaryBlue,
            ),
        )
        Text(
            text = text, style = style, color = appBlack, textAlign = TextAlign.Justify,
            modifier = modifier
                .clickable { onCheckedChange(!boxState) }
                .padding(end = 20.dp, top = 4.dp, bottom = 4.dp),
        )
    }
}

@Composable
fun CheckBoxNoText(
    boxState: Boolean, modifier: Modifier = Modifier, start: Dp = 30.dp, end: Dp = 10.dp,
    bottom: Dp = 10.dp, top: Dp = 0.dp, onCheckedChange: ((Boolean) -> Unit)
) {
    Row(
        modifier = modifier.padding(start = start, bottom = bottom, end = end, top = top),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = boxState, onCheckedChange = onCheckedChange,
            modifier = Modifier.padding(start = 10.dp),
            colors = CheckboxDefaults.colors(
                checkedColor = primaryBlue,
                uncheckedColor = primaryBlue,
            ),
        )
    }
}

@Composable
fun CheckBoxText(
    boxState: Boolean, modifier: Modifier = Modifier, start: Dp = 30.dp, end: Dp = 10.dp,
    bottom: Dp = 10.dp, top: Dp = 0.dp, onCheckedChange: ((Boolean) -> Unit)
) {
    Row(
        modifier = modifier.padding(start = start, bottom = bottom, end = end, top = top),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = boxState, onCheckedChange = onCheckedChange,
            modifier = Modifier.padding(start = 10.dp),
            colors = CheckboxDefaults.colors(
                checkedColor = primaryBlue,
                uncheckedColor = primaryBlue,
            ),
        )
    }
}
