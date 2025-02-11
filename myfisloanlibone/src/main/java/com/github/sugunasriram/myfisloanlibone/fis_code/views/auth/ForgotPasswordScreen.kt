package com.github.sugunasriram.myfisloanlibone.fis_code.views.auth

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CenterProgress
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CenteredMoneyImage
import com.github.sugunasriram.myfisloanlibone.fis_code.components.FixedTopBottomScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.components.InputField
import com.github.sugunasriram.myfisloanlibone.fis_code.components.RegisterText
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateSignInPage
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToOtpScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.ForgotPassword
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appDarkTeal
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal18Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.semiBold24Text700
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.auth.SignInViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ForgotPasswordScreen(navController: NavHostController) {

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val signInViewModel: SignInViewModel = viewModel()
    val mobileNumber: String by signInViewModel.mobileNumber.observeAsState("")
    val mobileNumberError: String? by signInViewModel.mobileNumberError.observeAsState("")

    val passwordLoading by signInViewModel.passwordLoading.collectAsState()
    val passwordLoaded by signInViewModel.passwordLoaded.collectAsState()
    val forgotResponse by signInViewModel.forgotResponse.collectAsState()

    val showInternetScreen by signInViewModel.showInternetScreen.observeAsState(false)
    val showTimeOutScreen by signInViewModel.showTimeOutScreen.observeAsState(false)
    val showServerIssueScreen by signInViewModel.showServerIssueScreen.observeAsState(false)
    val unexpectedErrorScreen by signInViewModel.unexpectedError.observeAsState(false)
    val shouldShowKeyboard by signInViewModel.shouldShowKeyboard.observeAsState(false)

    LaunchedEffect(shouldShowKeyboard) {
        if (shouldShowKeyboard) {
            keyboardController?.show()
            signInViewModel.resetKeyboardRequest()
        }
    }
    val (focusPhNumber) = FocusRequester.createRefs()

    BackHandler { navigateSignInPage(navController) }
    if (!showInternetScreen && !showTimeOutScreen && !showServerIssueScreen && !unexpectedErrorScreen) {
        ForgotPasswordScreenView(
            mobileNumber = mobileNumber, focusPhNumber = focusPhNumber,context = context,
            mobileNumberError = mobileNumberError, signInViewModel = signInViewModel,
            navController = navController, passwordLoading = passwordLoading,
            passwordLoaded = passwordLoaded, forgotResponse = forgotResponse
        )
    } else {
        CommonMethods().HandleErrorScreens(
            navController = navController, showInternetScreen = showInternetScreen,
            showTimeOutScreen = showTimeOutScreen, showServerIssueScreen = showServerIssueScreen,
            unexpectedErrorScreen = unexpectedErrorScreen
        )
    }
}

@Composable
fun ForgotPasswordScreenView(
    mobileNumber: String, focusPhNumber: FocusRequester, mobileNumberError: String?,
    signInViewModel: SignInViewModel, navController: NavHostController, context: Context,
    passwordLoading: Boolean, passwordLoaded: Boolean, forgotResponse: ForgotPassword?
) {
    if (passwordLoading) {
        CenterProgress()
    } else {
        if (passwordLoaded) {
            forgotResponse?.data?.orderId?.let { orderId ->
                navigateToOtpScreen(navController, mobileNumber, orderId, "2")
            }
        } else {
            FixedTopBottomScreen(
                isSelfScrollable = false, navController = navController, showBackButton = true,
                buttonText = stringResource(id = R.string.send_otp).uppercase(),
                onBackClick = { navigateSignInPage(navController) },
                onClick = {
                    signInViewModel.forgotButtonValidation(
                        mobileNumber, focusPhNumber, context
                    )
                }
            ) {
                RegisterText(
                    text = stringResource(id = R.string.forgot_passwords),
                    textColor = appDarkTeal, style = semiBold24Text700, top = 15.dp
                )
                CenteredMoneyImage(
                    image = R.drawable.forgot_password_icon, imageSize = 300.dp, top = 10.dp,
                    contentScale = ContentScale.Fit
                )
                InputField(
                    inputText = mobileNumber, modifier = Modifier.focusRequester(focusPhNumber),
                    hint = stringResource(id = R.string.enter_register_phnumber),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done, keyboardType = KeyboardType.Number
                    ),
                    error = mobileNumberError,
                    leadingIcon = {
                        Text(
                            text = "+91", style = normal18Text400, color = Color.Black
                        )
                    },
                    onValueChange = {
                        signInViewModel.onMobileNumberChanged(it)
                        signInViewModel.updateMobileNumberError(null)
                    }
                )
            }
        }

    }
}

@Preview
@Composable
fun ForgotPasswordScreenPreview() {
    ForgotPasswordScreen(rememberNavController())
}