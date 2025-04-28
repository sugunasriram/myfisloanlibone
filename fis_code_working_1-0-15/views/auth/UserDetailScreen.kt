package com.github.sugunasriram.myfisloanlibone.fis_code.views.auth

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CenteredMoneyImage
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CurvedPrimaryButtonFull
import com.github.sugunasriram.myfisloanlibone.fis_code.components.InnerScreenWithHamburger
import com.github.sugunasriram.myfisloanlibone.fis_code.components.InputField
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToOtpScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.auth.UserDetailViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserDetailScreen(navController: NavHostController) {
    val userDetailViewModel: UserDetailViewModel = viewModel()
    val phNumber: String by userDetailViewModel.phNumber.observeAsState("")

    val (focusPhNumber) = FocusRequester.createRefs()

    InnerScreenWithHamburger(isSelfScrollable = false, navController = navController) {
        CenteredMoneyImage(imageSize = 200.dp, top = 30.dp, image = R.drawable.user_page_image)

        InputField(
            inputText = phNumber, hint = stringResource(id = R.string.borrower_phone_number),
            modifier = Modifier
                .focusRequester(focusPhNumber)
                .padding(top = 20.dp),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done, keyboardType = KeyboardType.Number
            ),
            leadingIcon = { Text(text = "+91") },
            onValueChange = { userDetailViewModel.onPhNumberChanged(it) }
        )
        CurvedPrimaryButtonFull(
            text = stringResource(id = R.string.get_otp),
            modifier = Modifier.padding(
                start = 30.dp, end = 30.dp, bottom = 20.dp, top = 100.dp
            )
        ) {
            navigateToOtpScreen(
                navController = navController, mobileNumber = "9876543210", orderId = "1234567890",
                fromScreen = "3"
            )
        }
    }
}
