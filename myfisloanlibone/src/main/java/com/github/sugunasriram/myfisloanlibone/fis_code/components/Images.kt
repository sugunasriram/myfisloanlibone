package com.github.sugunasriram.myfisloanlibone.fis_code.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.sugunasriram.myfisloanlibone.R

@Composable
fun SucessImage(
    modifier: Modifier = Modifier, imageSize: Dp = 60.dp, top: Dp = 0.dp, bottom: Dp = 0.dp,
    start: Dp = 0.dp, end: Dp = 0.dp
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = top, bottom = bottom, start = start, end = end)
    ) {
        Image(
            painter = painterResource(id = R.drawable.otp_sucess_icon),
            contentDescription = stringResource(id = R.string.otp_sucess_icon),
            modifier.size(imageSize)
        )
    }

}

@Composable
fun CenteredMoneyImage(
    imageSize: Dp = 0.dp, @DrawableRes image: Int = R.drawable.home_page_logo,
    bottom: Dp = 0.dp, start: Dp = 0.dp, end: Dp = 0.dp, top: Dp = 0.dp,
    modifier: Modifier = Modifier, contentScale: ContentScale = ContentScale.Crop
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = start, end = end, top = top, bottom = bottom)
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = stringResource(id = R.string.home_page_image),
            modifier = Modifier
                .size(imageSize)
                .fillMaxSize(),
            contentScale = contentScale
        )
    }
}
