package com.github.sugunasriram.myfisloanlibone.fis_code.views.auth

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CenterProgress
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CenteredMoneyImage
import com.github.sugunasriram.myfisloanlibone.fis_code.components.FixedTopBottomScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.components.MultipleColorText
import com.github.sugunasriram.myfisloanlibone.fis_code.components.OtpView
import com.github.sugunasriram.myfisloanlibone.fis_code.components.RegisterText
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateSignInPage
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToForgotPasswordScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToOtpVerifyScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToRegisterScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToResetPasswordScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlack
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlueTitle
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appDarkTeal
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appRed
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal16Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal20Text500
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal32Text700
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.auth.OtpViewModel
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.auth.RegisterViewModel
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.auth.SignInViewModel
import kotlinx.coroutines.delay

@SuppressLint("UnrememberedMutableState")
@Composable
fun OtpScreen(
    navController: NavHostController, number: String?, orderId: String?, fromScreen: String
) {
    val textList =
        List(4) { remember { mutableStateOf(TextFieldValue(text = "", selection = TextRange(0))) } }
    val requesterList = List(4) { remember { FocusRequester() } }

    val context = LocalContext.current
    val otpViewModel: OtpViewModel = viewModel()
    val signInViewModel: SignInViewModel = viewModel()
    val showInternetScreen by otpViewModel.showInternetScreen.observeAsState(false)
    val showTimeOutScreen by otpViewModel.showTimeOutScreen.observeAsState(false)
    val showServerIssueScreen by otpViewModel.showServerIssueScreen.observeAsState(false)
    val unexpectedErrorScreen by otpViewModel.unexpectedError.observeAsState(false)

    val forgotPasswordOtpVerifying by otpViewModel.forgotPasswordOtpVerifying.collectAsState()
    val forgotPasswordOtpVerified by otpViewModel.forgotPasswordOtpVerified.collectAsState()
    val forgotResponse by signInViewModel.forgotResponse.collectAsState()
    val passwordLoading by signInViewModel.passwordLoading.collectAsState()
    val passwordLoaded by signInViewModel.passwordLoaded.collectAsState()

    val isLoading by otpViewModel.isLoading.collectAsState()
    val isLoadingSucess by otpViewModel.isLoadingSucess.collectAsState()
    val navigationToSignIn by otpViewModel.navigationToSignIn.collectAsState()


    val registerViewModel: RegisterViewModel = viewModel()
    val resendingOtp by registerViewModel.resendingOtp.collectAsState()
    val reSendedOtp by registerViewModel.reSendedOtp.collectAsState()
    val resendOtpResponse by registerViewModel.resendOtpResponse.collectAsState()


    // Timer for OTP expiry
    var count by remember { mutableStateOf(59) }
    var expired by remember { mutableStateOf(false) }
    var reSendTrigged by remember { mutableStateOf(false) }
    var reloadTimer by remember { mutableStateOf(false) }


    LaunchedEffect(passwordLoaded, forgotResponse, reSendedOtp, reloadTimer) {
        if (navigationToSignIn) {
            navigateSignInPage (navController)
        }else if (reSendTrigged && forgotResponse != null) {
            reSendTrigged = false
            count = 59
            expired = false
            while (count > 0) {
                delay(1000)
                count--
            }
            expired = true
        } else if (!passwordLoaded && forgotResponse == null) {
            count = 59
            expired = false
            while (count > 0) {
                delay(1000)
                count--
            }
            expired = true
        } else if (!reSendedOtp && resendOtpResponse == null) {
            count = 59
            expired = false
            while (count > 0) {
                delay(1000)
                count--
            }
            expired = true
        } else if (reloadTimer) {
            count = 59
            expired = false
            reloadTimer = false
            while (count > 0) {
                delay(1000)
                count--
            }
            expired = true
        }
    }


    when (fromScreen) {
        "1" -> {
            BackHandler {
                navigateToRegisterScreen(navController)
            }
        }

        "2" -> {
            BackHandler {
                navigateToForgotPasswordScreen(navController)
            }
        }
    }


    if (!showInternetScreen && !showTimeOutScreen && !showServerIssueScreen && !unexpectedErrorScreen) {
        if (passwordLoading || resendingOtp || isLoading || forgotPasswordOtpVerifying) {
            CenterProgress()
        } else {
            if (isLoadingSucess || forgotPasswordOtpVerified) {
                when (fromScreen) {
                    "1" -> {
                        navigateToOtpVerifyScreen(navController)
                    }

                    "2" -> {
                        number?.let { mobileNumber ->
                            navigateToResetPasswordScreen(navController, mobileNumber)
                        }
                    }
                }
            } else {
                FixedTopBottomScreen(
                    isSelfScrollable = false, navController = navController, showBackButton = true,
                    buttonText = stringResource(id = R.string.submit),
                    onBackClick = {
                        when (fromScreen) {
                            "1" -> {
                                navigateToRegisterScreen(navController)
                            }

                            "2" -> {
                                navigateToForgotPasswordScreen(navController)
                            }
                        }
                    },
                    onClick = {
                        if (expired) {
                            CommonMethods().toastMessage(
                                context = context,
                                toastMsg = context.getString(R.string.time_expired_click_on_resend_otp)
                            )
                        } else {
                            when (fromScreen) {
                                "1" -> {
                                    if (reSendTrigged) {
                                        resendOtpResponse?.orderId?.let { otpId ->
                                            val otp = textList.joinToString("") { it.value.text }
                                            otpViewModel.otpValidation(
                                                enteredOtp = otp, orderId =  otpId,
                                                context =  context,navController = navController
                                            )
                                        }
                                        expired = false

                                    } else {
                                        orderId?.let { id ->
                                            val otp = textList.joinToString("") { it.value.text }
                                            otpViewModel.otpValidation(
                                                enteredOtp = otp, orderId = id, context = context,
                                                navController = navController
                                            )
                                        }
                                    }
                                }

                                "2" -> {
                                    orderId?.let { id ->
                                        val otp = textList.joinToString("") { it.value.text }
                                        otpViewModel.forgotPasswordOtpValidation(
                                            otp = otp, orderId = orderId, context = context
                                        )
                                    }
                                }
                            }
                        }

                    }
                ) {
                    CenteredMoneyImage(
                        image = R.drawable.otp_page_image, imageSize = 150.dp, top = 30.dp,
                        bottom = 30.dp
                    )

                    RegisterText(
                        text = stringResource(id = R.string.enter_otp), textColor = appDarkTeal,
                        style = normal32Text700
                    )
                    RegisterText(
                        text = stringResource(id = R.string.otp_sent_number),
                        size = 0.dp, textColor = appBlack, style = normal16Text400, top = 10.dp
                    )
                    number?.let {
                        val textToShow = "******" + it.takeLast(4)
                        RegisterText(
                            text = textToShow, size = 0.dp, textColor = appBlack,
                            style = normal16Text400
                        )
                    }

                    OtpView(textList = textList, requestList = requesterList)

                    RegisterText(
                        text = if (expired) stringResource(id = R.string.time_expired) else "$count seconds",
                        size = 20.dp, textColor = appBlueTitle, style = normal20Text500,
                        bottom = 10.dp
                    )
                    when (fromScreen) {
                        "1" -> {
                            MultipleColorText(
                                text = stringResource(id = R.string.resend_otp),
                                textColor = appBlueTitle, resendOtpColor = appRed,
                            ) {
                                if (expired) {
                                    orderId?.let { orderId ->
                                        registerViewModel.authResendOTP(
                                            orderId = orderId, context = context,
                                            navController = navController
                                        )
                                        reSendTrigged = true
                                        expired = false
                                        reloadTimer = true

                                    }
                                } else {
                                    CommonMethods().toastMessage(
                                        context = context, toastMsg = "Wait For $count Seconds"
                                    )
                                }
                            }
                        }

                        "2" -> {
                            MultipleColorText(
                                text = stringResource(id = R.string.resend_otp),
                                textColor = appBlueTitle, resendOtpColor = appRed,
                            ) {
                                if (expired) {
                                    number?.let { mobileNumber ->
                                        signInViewModel.forgotPassword(
                                            mobileNumber = mobileNumber,
                                            countryCode = context.getString(R.string.country_code),
                                            context = context
                                        )
                                        reSendTrigged = true
                                        expired = false
                                        reloadTimer = true

                                    }
                                } else {
                                    CommonMethods().toastMessage(
                                        context = context, toastMsg = "Wait For $count Seconds"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    } else {
        CommonMethods().HandleErrorScreens(
            navController = navController, showInternetScreen = showInternetScreen,
            showTimeOutScreen = showTimeOutScreen, showServerIssueScreen = showServerIssueScreen,
            unexpectedErrorScreen = unexpectedErrorScreen
        )
    }
}

@Preview
@Composable
fun OtpScreenPreview() {
    OtpScreen(
        navController = rememberNavController(), orderId = "1111", number = "11111",
        fromScreen = "2222"
    )
}