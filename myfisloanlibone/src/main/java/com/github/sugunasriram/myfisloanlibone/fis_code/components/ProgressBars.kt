package com.github.sugunasriram.myfisloanlibone.fis_code.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlue

@Composable
fun CenterProgress(modifier: Modifier = Modifier,top: Dp = 0.dp) {
    Column(
        modifier = modifier.fillMaxSize().padding(top = top),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = appBlue,
            modifier = Modifier
                .padding(all = 8.dp)
                .size(30.dp)
        )
    }
}

@Composable
fun CenterProgressFixedHeight(modifier: Modifier = Modifier, top: Dp = 10.dp) {
    Column(
        modifier = modifier.padding(top = top).fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = appBlue,
            modifier = Modifier
                .padding(all = 8.dp)
                .size(30.dp)
        )
    }
}



