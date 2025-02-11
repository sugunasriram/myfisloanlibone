package com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.ClickableTextWithIcon
import com.github.sugunasriram.myfisloanlibone.fis_code.components.StartingText
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToLoanDetailScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.azureBlueColor
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.errorGray
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal14Text500
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.semiBold24Text700
import kotlinx.coroutines.delay

@Composable
fun PrePaymentStatusScreen(
    navController: NavHostController,
    headerText: String = stringResource(id = R.string.repayment_successful),
    hintText: String = stringResource(id = R.string.repayment_successfully_processed),
    image: Painter = painterResource(id = R.drawable.successful_purchase_image),
    showButton: Boolean = false,
    orderId:String,
    fromFlow:String,
    onClick: () -> Unit
) {
    LaunchedEffect (Unit){
        delay(3000)
        navigateToLoanDetailScreen(navController = navController, orderId = orderId, fromFlow = fromFlow)
    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = image,
                contentDescription = "",
                contentScale = ContentScale.Crop,
            )

            StartingText(
                text = headerText,
                textColor = Color.Black,
                start = 30.dp, end = 30.dp, top = 10.dp, bottom = 5.dp,
                style = semiBold24Text700,
                alignment = Alignment.TopCenter
            )
            if (!showButton){
                StartingText(
                    text = hintText,
                    textColor = errorGray,
                    start = 30.dp, end = 30.dp, top = 10.dp, bottom = 5.dp,
                    style = normal14Text500,
                    alignment = Alignment.TopCenter
                )
            }

            if (showButton){
                ClickableTextWithIcon(
                    text = stringResource(id = R.string.retry),
                    image = R.drawable.refresh_blue_icon,
                    backgroundColor = Color.White,
                    borderColor = azureBlueColor,
                    color = azureBlueColor, imagStart = 20.dp, imageEnd = 20.dp
                ) { onClick() }
            }
        }
    }
}