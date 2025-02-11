package com.github.sugunasriram.myfisloanlibone.fis_code.views.auth

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CenteredMoneyImage
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CurvedPrimaryButtonFull
import com.github.sugunasriram.myfisloanlibone.fis_code.components.InnerScreenWithHamburger
import com.github.sugunasriram.myfisloanlibone.fis_code.components.RegisterText
import com.github.sugunasriram.myfisloanlibone.fis_code.components.SucessImage
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateSignInPage
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.midNightBlue
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal36Text500

@Composable
fun OtpVerifiedScreen(navController: NavHostController) {
    InnerScreenWithHamburger(isSelfScrollable = false, navController = navController) {
        CenteredMoneyImage(imageSize = 200.dp)
        RegisterText(
            text = stringResource(id = R.string.otp_verified), textColor = midNightBlue,
            style = normal36Text500
        )
        SucessImage(top = 50.dp)
        RegisterText(
            text = stringResource(id = R.string.user_registered), textColor = midNightBlue,
            modifier = Modifier.padding(20.dp), style = normal36Text500
        )
        CurvedPrimaryButtonFull(
            text = stringResource(id = R.string.done),
            modifier = Modifier.padding(start = 30.dp, end = 30.dp, bottom = 20.dp, top = 30.dp)
        ) { navigateSignInPage(navController) }
    }
}