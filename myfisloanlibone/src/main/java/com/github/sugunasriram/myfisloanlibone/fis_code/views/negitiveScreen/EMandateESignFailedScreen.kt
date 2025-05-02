package com.github.sugunasriram.myfisloanlibone.fis_code.views.negitiveScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.StartingText
import com.github.sugunasriram.myfisloanlibone.fis_code.components.TopBar
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateApplyByCategoryScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.negativeGray
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.robotoSerifNormal24Text500

@Composable
fun EMandateESignFailedScreen(navController : NavHostController,title : String) {
    BackHandler {
        navigateApplyByCategoryScreen(navController)
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        // Top bar
        TopBar(navController = navController, ifErrorFlow = true)

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.form_submission_failed_image),
                    contentDescription = "", contentScale = ContentScale.Crop,
                )

                Spacer(modifier = Modifier.padding(vertical = 20.dp))

                StartingText(
                    text = title,
                    textColor = negativeGray,
                    start = 30.dp, end = 30.dp, top = 10.dp, bottom = 5.dp,
                    style = robotoSerifNormal24Text500,
                    alignment = Alignment.Center
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.ondc_icon),
            contentDescription = stringResource(id = R.string.ondc_icon),
            Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
                .size(height = 50.dp, width = 200.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Preview
@Composable
private fun KYCFailedPReviewScreen() {
    Surface  {
        EMandateESignFailedScreen(rememberNavController(),/*stringResource(R.string.emandate_failed)*/stringResource(R.string.esign_failed))
    }
}