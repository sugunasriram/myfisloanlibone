package com.github.sugunasriram.myfisloanlibone.fis_code.views.auth

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalClipboardManager
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
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToOtpVerifyScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToRegisterScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.UserRole
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlack
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlueTitle
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appDarkTeal
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appGray
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appRed
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal16Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal20Text500
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal32Text700
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.auth.OtpViewModel
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.auth.RegisterViewModel
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.auth.SignInViewModel
import kotlinx.coroutines.delay
import androidx.compose.ui.platform.LocalConfiguration
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.DeviceInfo


@SuppressLint("UnrememberedMutableState")
@Composable
fun OtpScreen(
    navController: NavHostController, number: String?, orderId: String?, fromScreen: String
) {
    val textList =
        List(4) { remember { mutableStateOf(TextFieldValue(text = "", selection = TextRange(0))) } }
    val requesterList = List(4) { remember { FocusRequester() } }

    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val otpViewModel: OtpViewModel = viewModel()
    val signInViewModel: SignInViewModel = viewModel()


    val showInternetScreen by otpViewModel.showInternetScreen.observeAsState(false)
    val showTimeOutScreen by otpViewModel.showTimeOutScreen.observeAsState(false)
    val showServerIssueScreen by otpViewModel.showServerIssueScreen.observeAsState(false)
    val unexpectedErrorScreen by otpViewModel.unexpectedError.observeAsState(false)

//    val forgotPasswordOtpVerifying by otpViewModel.forgotPasswordOtpVerifying.collectAsState()
//    val forgotPasswordOtpVerified by otpViewModel.forgotPasswordOtpVerified.collectAsState()
//    val forgotResponse by signInViewModel.forgotResponse.collectAsState()
//    val passwordLoading by signInViewModel.passwordLoading.collectAsState()
//    val passwordLoaded by signInViewModel.passwordLoaded.collectAsState()

    val isSignUpOtpLoading by otpViewModel.isSignUpOtpLoading.collectAsState()
    val isSignUpOtpLoadingSucess by otpViewModel.isSignUpOtpLoadingSuccess.collectAsState()
    val isLoginOtpLoading by otpViewModel.isLoginOtpLoading.collectAsState()
    val isLoginOtpLoadingSuccess by otpViewModel.isLoginOtpLoadingSuccess.collectAsState()
    val navigationToSignIn by otpViewModel.navigationToSignIn.collectAsState()

    val registerViewModel: RegisterViewModel = viewModel()
    val resendingOtp by registerViewModel.resendingOtp.collectAsState()
    val reSendedOtp by registerViewModel.reSendedOtp.collectAsState()
    val resendOtpResponse by registerViewModel.resendOtpResponse.collectAsState()

    val clipboardManager = LocalClipboardManager.current


    // Timer for OTP expiry
    var count by remember { mutableStateOf(59) }
    var expired by remember { mutableStateOf(false) }
    var reSendTrigged by remember { mutableStateOf(false) }
    var reloadTimer by remember { mutableStateOf(false) }

    val deviceInfo = otpViewModel.getDeviceInfo(context,configuration)

    LaunchedEffect(reSendTrigged, reloadTimer) {
        if (navigationToSignIn) {
            navigateSignInPage(navController)
        } else {
            count = 59
            expired = false
            reloadTimer = false
            reSendTrigged = false // Reset the trigger

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
                navigateSignInPage(navController)
            }
        }
    }


    if (!showInternetScreen && !showTimeOutScreen && !showServerIssueScreen && !unexpectedErrorScreen) {
        if (isLoginOtpLoading || resendingOtp || isSignUpOtpLoading) {
            CenterProgress()
        } else {
            if (isLoginOtpLoadingSuccess || isSignUpOtpLoadingSucess) {
                when (fromScreen) {
                    "1" -> {
                        navigateToOtpVerifyScreen(navController, fromScreen)
                    }

                    "2" -> {
                        navigateToOtpVerifyScreen(navController, fromScreen)
                    }
                }
            } else {
                FixedTopBottomScreen(
                    isSelfScrollable = false, navController = navController, showBackButton = true,
                    buttonText = stringResource(id = R.string.submit),
                    onBackClick = {
                        when (fromScreen) {
                            "1" -> navigateToRegisterScreen(navController)
                            "2" -> navigateSignInPage(navController)
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
                                            otpViewModel.signUpOtpValidation(
                                                enteredOtp = otp, orderId = otpId,
                                                context = context, navController = navController
                                            )
                                        }
                                        expired = false

                                    } else {
                                        orderId?.let { id ->
                                            val otp = textList.joinToString("") { it.value.text }
                                            otpViewModel.signUpOtpValidation(
                                                enteredOtp = otp, orderId = id, context = context,
                                                navController = navController
                                            )
                                        }
                                    }
                                }

                                "2" -> {
                                    if (reSendTrigged) {
                                        submitOTP(
                                            orderId,
                                            textList,
                                            otpViewModel,
                                            context,
                                            navController,
                                            deviceInfo
                                        )
                                        expired = false

                                    } else {
                                        submitOTP(
                                            orderId,
                                            textList,
                                            otpViewModel,
                                            context,
                                            navController,
                                            deviceInfo
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

                    OtpView(textList = textList, requestList = requesterList, pastedEvent =  {
                        val annotatedString = clipboardManager.getText()
                        annotatedString?.let {
                            extractOtp(it.text)?.let {
                                textList.forEachIndexed { key, textView ->
                                    textView.value = TextFieldValue("${annotatedString.text[key]}")
                                }
                                submitOTP(
                                    orderId,
                                    textList,
                                    otpViewModel,
                                    context,
                                    navController,
                                    deviceInfo
                                )
                            }
                        }
                    })

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
                                    orderId?.let { id ->
                                        val otp = textList.joinToString("") { it.value.text }
                                        otpViewModel.loginOtpValidation(
                                            enteredOtp = otp, orderId = id, context = context,
                                            navController = navController,
                                            deviceInfo=deviceInfo
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
                                textColor = if (count > 0) appGray else appBlueTitle,
                                resendOtpColor = if (count > 0) appGray else appRed,
                            ) {
                                if (expired) {
                                    signInViewModel.getUserRole(
                                        number.toString(), context.getString(R.string.country_code),
                                        context
                                    )
                                    reSendTrigged = true
                                    expired = false
                                    reloadTimer = true
                                } else {
                                    if(count>0){
                                    CommonMethods().toastMessage(
                                        context = context, toastMsg = "Wait For $count Seconds"
                                    )}
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

private fun submitOTP(
    orderId: String?,
    textList: List<MutableState<TextFieldValue>>,
    otpViewModel: OtpViewModel,
    context: Context,
    navController: NavHostController,
    deviceInfo: DeviceInfo
) {
    orderId?.let { id ->
        val otp = textList.joinToString("") { it.value.text }
        otpViewModel.loginOtpValidation(
            enteredOtp = otp, orderId = id, context = context,
            navController = navController,
            deviceInfo = deviceInfo
        )
    }
}

fun extractOtp(input: String): String? {
    val regex = Regex("\\b\\d{4}\\b")
    return regex.find(input)?.value
}

@Preview
@Composable
fun OtpScreenPreview() {
    OtpScreen(
        navController = rememberNavController(), orderId = "1111", number = "11111",
        fromScreen = "2222"
    )
}
