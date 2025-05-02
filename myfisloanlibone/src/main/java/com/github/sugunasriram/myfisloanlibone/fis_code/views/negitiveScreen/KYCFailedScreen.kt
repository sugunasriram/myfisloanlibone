package com.github.sugunasriram.myfisloanlibone.fis_code.views.negitiveScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.StartingText
import com.github.sugunasriram.myfisloanlibone.fis_code.components.TopBar
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateApplyByCategoryScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.failureRed
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.negativeGray
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normalSerif32Text500
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.robotoSerifNormal24Text500
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.semibold32Text500

@Composable
fun KYCFailedScreen(navController: NavHostController) {

    BackHandler {
        navigateApplyByCategoryScreen(navController)
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        // Top bar
        TopBar(navController = navController, ifErrorFlow = true)
        Column(
            modifier = Modifier.fillMaxSize().offset(y = (-100).dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            StartingText(
            text = stringResource(R.string.kyc_failed),
            textColor = failureRed,
            start = 30.dp, end = 30.dp, top = 10.dp, bottom = 30.dp,
            style = normalSerif32Text500,
            alignment = Alignment.Center
        )
            Image(
                painter = painterResource(id = R.drawable.kyc_failed_image),
                contentDescription = "", contentScale = ContentScale.Crop,
            )

        }
    }
}

@Preview
@Composable
private fun KYCFailedPReviewScreen() {
    Surface {
        KYCFailedScreen(rememberNavController())
    }
}