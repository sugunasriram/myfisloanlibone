package com.github.sugunasriram.myfisloanlibone.fis_code.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.greenCard
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.lightGray
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.lightishGray
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.newBlueColor
import com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan.TableRow

@Composable
fun FullWidthRoundShapedCard(
    modifier: Modifier = Modifier, start: Dp = 15.dp, end: Dp = 15.dp, top: Dp = 15.dp,
    shapeSize: Dp = 12.dp, bottomPadding: Dp = 20.dp, cardColor: Color = greenCard,
    onClick: () -> Unit, alignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    bottom: Dp = 15.dp, content: @Composable () -> Unit
) {
    Column(
        horizontalAlignment = alignment,
        modifier = modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .padding(start = start, end = end, top = top, bottom = bottom)
            .background(
                shape = RoundedCornerShape(shapeSize),
                color = cardColor
            )
            .padding(bottomPadding),
    ) {
        content()
    }
}

@Composable
fun NumberFullWidthCard(
    modifier: Modifier = Modifier, cardColor: Color = greenCard, start: Dp = 20.dp, end: Dp = 20.dp,
    top: Dp = 0.dp, bottom: Dp = 0.dp, content: @Composable () -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = start, end = end, top = top, bottom = bottom)
            .background(
                shape = RoundedCornerShape(3.dp),
                color = cardColor
            )
    ) {
        content()
    }

}

@Composable
fun NumberFullWidthBorderCard(
    modifier: Modifier = Modifier, cardColor: Color = greenCard, borderColor: Color = lightGray,
    start: Dp = 20.dp, end: Dp = 20.dp, top: Dp = 0.dp, bottom: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = start, end = end, top = top, bottom = bottom)
            .background(
                shape = RoundedCornerShape(3.dp),
                color = cardColor
            )
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(5.dp))
    ) {
        content()
    }
}

@Composable
fun ClickableFullWidthBorderCard(
    modifier: Modifier = Modifier, cardColor: Color = greenCard, borderColor: Color = lightGray,
    start: Dp = 20.dp, end: Dp = 20.dp, top: Dp = 0.dp, bottom: Dp = 0.dp,
    onClick: () -> Unit, content: @Composable () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .padding(start = start, end = end, top = top, bottom = bottom)
            .background(
                shape = RoundedCornerShape(3.dp),
                color = cardColor
            )
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(5.dp))
    ) {
        content()
    }
}

@Composable
fun HeaderCard(
    modifier: Modifier = Modifier, cardColor: Color = newBlueColor, borderColor: Color = lightGray,
    start: Dp = 0.dp, end: Dp = 0.dp, top: Dp = 0.dp, bottom: Dp = 0.dp, bottomStart: Dp = 20.dp,
    bottomEnd: Dp = 20.dp, topStart: Dp = 0.dp, topEnd: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = start, end = end, top = top, bottom = bottom)
            .background(
                shape = RoundedCornerShape(
                    bottomStart = bottomStart, bottomEnd = bottomEnd, topStart = topStart,
                    topEnd = topEnd
                ),
                color = cardColor
            )
            .border(
                width = 1.dp, color = borderColor,
                shape = RoundedCornerShape(
                    bottomStart = bottomStart, bottomEnd = bottomEnd, topStart = topStart,
                    topEnd = topEnd
                )
            )
    ) {
        content()
    }
}


@Preview
@Composable
fun FullWidthRoundShapedCardPreview() {
    FullWidthRoundShapedCard(
        onClick = { }, cardColor = lightishGray,
        start = 0.dp, end = 0.dp, top = 0.dp, bottom = 0.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            TableRow(
                emiNumber = "1", dueDate = "20-July-2024", amount = "88846.00", status = "NOT-PAID"
            )
        }
    }

}

