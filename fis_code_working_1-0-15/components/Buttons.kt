package com.github.sugunasriram.myfisloanlibone.fis_code.components

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlue
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appWhite
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.azureBlue
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.azureBlueColor
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal16Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal20Text500
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.primaryBlue

@SuppressLint("SuspiciousIndentation")
@Composable
fun CurvedPrimaryButtonFull(
    text: String, modifier: Modifier = Modifier, top: Dp = 10.dp, bottom: Dp = 10.dp,
    start: Dp = 40.dp, end: Dp = 40.dp, style: TextStyle = normal20Text500,
    shape: RoundedCornerShape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
    textColor: Color = appWhite, backgroundColor: Color = appBlue, onClick: () -> Unit
) {
    val controller = LocalSoftwareKeyboardController.current
    Text(
        text = text, style = style, color = textColor, textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .background(color = backgroundColor, shape = shape)
            .clickable {
                onClick()
                controller?.hide()
            }
            .padding(top = top, bottom = bottom, start = start, end = end)
    )

}

@Composable
fun CurvedPrimaryButtonMultipleInRow(
    text: String, modifier: Modifier = Modifier, top: Dp = 10.dp, bottom: Dp = 10.dp,
    start: Dp = 40.dp, end: Dp = 40.dp, style: TextStyle = normal20Text500,
    shape: RoundedCornerShape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
    textColor: Color = appWhite, backgroundColor: Color = appBlue, onClick: () -> Unit
) {
    val controller = LocalSoftwareKeyboardController.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(color = backgroundColor, shape = shape)
            .clickable {
                onClick()
                controller?.hide()
            }
            .padding(top = top, bottom = bottom, start = start, end = end),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text, style = style, color = textColor, textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun WrapBorderButton(
    text: String, modifier: Modifier = Modifier, top: Dp = 10.dp, bottom: Dp = 10.dp,
    start: Dp = 17.dp, end: Dp = 17.dp, boxTop: Dp = 0.dp, style: TextStyle = normal20Text500,
    shape: RoundedCornerShape = RoundedCornerShape(15.dp), textColor: Color = primaryBlue,
    backgroundColor: Color = appWhite, alignment: Alignment = Alignment.TopEnd,
    onClick: () -> Unit
) {
    val controller = LocalSoftwareKeyboardController.current
    Box(
        contentAlignment = alignment,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = boxTop)
    ) {
        Text(
            text = text, style = style, color = textColor, textAlign = TextAlign.Center,
            modifier = modifier
                .clickable {
                    onClick()
                    controller?.hide()
                }
                .background(color = backgroundColor, shape = shape)
                .padding(top = top, bottom = bottom, start = start, end = end)
        )
    }
}

@Composable
fun ClickableTextWithIcon(
    text: String, @DrawableRes image: Int, textDecoration: TextDecoration? = null,
    backgroundColor: Color = Color(0xFFF0F0F0), borderColor: Color = Color(0xFFF0F0F0),
    style: TextStyle = TextStyle(
        fontStyle = FontStyle.Normal, fontWeight = FontWeight.Normal, fontSize = 12.sp,
        lineHeight = 22.sp,
    ), color: Color = Color(109, 114, 120, 255),
    modifier: Modifier = Modifier,
    imagStart: Dp = 10.dp, imageTop: Dp = 10.dp, imageEnd: Dp = 10.dp, imageBottom: Dp = 10.dp,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
    ) {
        Row(
            modifier = modifier
                .background(backgroundColor, shape = RoundedCornerShape(5.dp))
                .border(shape = RoundedCornerShape(5.dp), color = borderColor, width = 1.dp)
                .clickable { onClick() },
        ) {
            Image(
                painter = painterResource(id = image), contentDescription = "view all icon",
                modifier = modifier
                    .padding(start = imagStart, top = imageTop, bottom = imageBottom)
            )
            Text(
                text = text, textDecoration = textDecoration, style = style, color = color,
                textAlign = TextAlign.Justify,
                modifier = modifier
                    .padding(start = 10.dp, top = 10.dp, bottom = 10.dp, end = imageEnd),
            )
        }
    }
}

@Composable
fun ClickableText(
    text: String, textAlign: TextAlign = TextAlign.Justify, textDecoration: TextDecoration? = null,
    backgroundColor: Color = Color(0xFFF0F0F0), borderColor: Color = Color(0xFFF0F0F0),
    style: TextStyle = TextStyle(
        fontStyle = FontStyle.Normal, fontWeight = FontWeight.Normal, fontSize = 12.sp,
        lineHeight = 22.sp,
    ), color: Color = Color(109, 114, 120, 255),
    modifier: Modifier = Modifier, onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .padding(top = 30.dp)
    ) {
        Row(
            modifier = modifier
                .background(backgroundColor, shape = RoundedCornerShape(5.dp))
                .border(shape = RoundedCornerShape(5.dp), color = borderColor, width = 1.dp)
                .clickable { onClick() },
        ) {

            Text(
                text = text, textDecoration = textDecoration, style = style, color = color,
                textAlign = textAlign,
                modifier = modifier
                    .padding(start = 10.dp, top = 10.dp, bottom = 10.dp, end = 10.dp),
            )
        }
    }
}

@Composable
fun CurvedPrimaryButton(
    text: String, modifier: Modifier = Modifier, top: Dp = 10.dp, bottom: Dp = 10.dp,
    start: Dp = 60.dp, end: Dp = 60.dp, style: TextStyle = normal20Text500,
    shape: RoundedCornerShape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
    textColor: Color = appWhite, backgroundColor: Color = appBlue, onClick: () -> Unit
) {
    val controller = LocalSoftwareKeyboardController.current
    Text(
        text = text, style = style, color = textColor, textAlign = TextAlign.Center,
        modifier = modifier
            .background(color = backgroundColor, shape = shape)
            .clickable {
                onClick()
                controller?.hide()
            }
            .padding(top = top, bottom = bottom, start = start, end = end)
    )

}


@Preview
@Composable
fun ClickableTextPreview() {
    ClickableText(
        text = stringResource(id = R.string.raise_issue), textAlign = TextAlign.Justify,
        backgroundColor = Color.White, borderColor = azureBlueColor, color = azureBlueColor
    ) { }
}

/**
 * Preview for [CurvedPrimaryButtonFull]
 */
@Preview
@Composable
fun CurvedPrimaryButtonFullPreview() {
    CurvedPrimaryButtonFull(
        text = "Accept",
        modifier = Modifier.padding(
            start = 30.dp, end = 30.dp, top = 30.dp, bottom = 30.dp
        ),
        backgroundColor = azureBlue, textColor = Color.White
    ) { }
}

@Preview
@Composable
fun WrapBorderButtonPreview() {
    WrapBorderButton(
        text = stringResource(id = R.string.check_now),
        modifier = Modifier.padding(
            start = 30.dp, end = 30.dp, top = 30.dp, bottom = 30.dp
        ),
        backgroundColor = Color.Green.copy(alpha = 0.7f), textColor = Color.White
    ) { }
}

@Preview
@Composable
fun CurvedPrimaryButtonMultipleInRowPreview() {
    CurvedPrimaryButtonMultipleInRow(
        text = stringResource(id = R.string.view_loan_agreement).uppercase(),
        style = normal16Text400,
        start = 0.dp,
        end = 0.dp,
        top = 10.dp,
        bottom = 10.dp,
        modifier = Modifier
            .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp)
            .height(85.dp)

    ) { }
}
