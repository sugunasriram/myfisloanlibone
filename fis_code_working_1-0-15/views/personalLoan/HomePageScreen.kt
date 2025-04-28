package com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CenteredMoneyImage
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CurvedPrimaryButtonFull
import com.github.sugunasriram.myfisloanlibone.fis_code.components.InnerScreenWithHamburger
import com.github.sugunasriram.myfisloanlibone.fis_code.components.SignUpText
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToRegisterScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateUserDetailScreen

@Composable
fun HomePageScreen(navController: NavHostController,fromFlow:String) {
    InnerScreenWithHamburger(isSelfScrollable = false, navController = navController) {
        CenteredMoneyImage(image = R.drawable.home_image, start = 20.dp, end = 20.dp, imageSize =
        280.dp, top = 5.dp,
            contentScale = ContentScale.Fit)

        CurvedPrimaryButtonFull(
            text = stringResource(id = R.string.borrower_register),
            modifier = Modifier.padding(horizontal = 30.dp, vertical = 30.dp)
        ) {
            navigateToRegisterScreen(navController)
        }
        CurvedPrimaryButtonFull(
            text = stringResource(id = R.string.loan_request),
            modifier = Modifier.padding(horizontal = 30.dp)
        ) {
            navigateUserDetailScreen(navController)
        }
        SignUpText(
            text = stringResource(id = R.string.home_page_text),
            start = 40.dp, end = 40.dp, top = 70.dp, bottom = 30.dp
            ) {}
    }
}

@Preview
@Composable
fun HomePageScreenPreview() {
    HomePageScreen(navController = NavHostController(LocalContext.current),"Personal")
}
