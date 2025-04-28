package com.github.sugunasriram.myfisloanlibone.fis_code.views.auth

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CenterProgress
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CenteredMoneyImage
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CurvedPrimaryButtonFull
import com.github.sugunasriram.myfisloanlibone.fis_code.components.InnerScreenWithHamburger
import com.github.sugunasriram.myfisloanlibone.fis_code.components.InputField
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToOtpScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlue
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal18Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal32Text500
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.slideActiveColor
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.textBlack
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.auth.RegisterViewModel
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.auth.SignInViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignInScreen(navController: NavHostController) {

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val signInViewModel: SignInViewModel = viewModel()
    val registerViewModel: RegisterViewModel = viewModel()

    val mobileNumber: String by signInViewModel.mobileNumber.observeAsState("")
    val mobileNumberError: String? by signInViewModel.mobileNumberError.observeAsState("")
    val isLoginSuccess = signInViewModel.isLoginSuccess.collectAsState()
    val isLoginInProgress = signInViewModel.isLoginInProgress.collectAsState()
//    val loginResponse = signInViewModel.loginSuccessData.collectAsState()
    val generatedOtpData = signInViewModel.generatedOtpData.collectAsState()

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
    val (focusSignInButton) = FocusRequester.createRefs()

    var showExitDialog by remember { mutableStateOf(false) }
    val activity = LocalContext.current as Activity

    BackHandler {
        if (navController.currentBackStackEntry?.destination?.route == "sign_in_screen") {
            showExitDialog = true
        } else {
            navController.popBackStack()
        }
    }

    if (!showInternetScreen && !showTimeOutScreen && !showServerIssueScreen && !unexpectedErrorScreen) {
        if (isLoginInProgress.value) {
            CenterProgress()
        } else {
            if (isLoginSuccess.value) {
                navigateToOtpScreen(
                    navController,
                    mobileNumber,
                    generatedOtpData.value?.data?.orderId.toString(),
                    "2"
                )
            } else {
                InnerScreenWithHamburger(
                    isHamBurgerVisible = false,
                    isSelfScrollable = false,
                    navController = navController
                ) {

                    CenteredMoneyImage(
                        image = R.drawable.sign_in_screen_image, imageSize = 300.dp, start = 70.dp,
                        end = 70.dp, top = 70.dp
                    )
                    PhoneNumberField(
                        mobileNumber = mobileNumber,
                        focusPhNumber = focusPhNumber,
                        nextFocus = focusSignInButton,
                        mobileNumberError = mobileNumberError,
                        signInViewModel = signInViewModel
                    )
                    CurvedPrimaryButtonFull(
                        text = stringResource(id = R.string.get_otp),
                        modifier = Modifier.padding(
                            start = 40.dp, end = 40.dp, bottom = 20.dp, top = 30.dp
                        ).focusRequester(focusSignInButton),
                    ) {
                        signInViewModel.signInValidation(
                            navController,
                            mobileNumber = mobileNumber, mobileNumberFocus = focusPhNumber,
                            context = context,
                        )
                    }
//                    NotRegisteredText(
//                        text = stringResource(id = R.string.register_user),
//                        padding = 0.dp
//                    )
//                    SignUpText(
//                        text = stringResource(id = R.string.sign_up),
//                        style = TextStyle(
//                            fontFamily = FontFamily(Font(R.font.robotocondensed_bold)),
//                            fontWeight = FontWeight(800)
//                        ),
//                        modifier = Modifier
//                            .padding(bottom = 10.dp)
//                            .focusRequester(focusSignInButton)
//
//                    ) {
//                        navigateToRegisterScreen(navController)
//                    }
                }
            }

            if (showExitDialog) {
                AlertDialog(
                    onDismissRequest = { showExitDialog = false },
                    confirmButton = {
                        Button(
                            onClick = {
                                showExitDialog = false; activity?.finish()
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = appBlue)
                        ) {
                            Text("Yes", color = Color.White)
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                showExitDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = appBlue)
                        ) {
                            Text("No", color = Color.White)
                        }
                    },
                    title = {
                        Text(
                            text = stringResource(id = R.string.exit_app), style = normal32Text500,
                            modifier = Modifier, color = textBlack
                        )
                    },
                    text = { Text(stringResource(id = R.string.are_you_sure_you_want_to_exit)) },
                    modifier = Modifier
                        .border(2.dp, slideActiveColor)
                        .padding(2.dp)
                )
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


@Composable
fun PhoneNumberField(
    mobileNumber: String,
    focusPhNumber: FocusRequester,
    nextFocus: FocusRequester,
    mobileNumberError: String?,
    signInViewModel: SignInViewModel
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    InputField(
        top = 20.dp,
        inputText = mobileNumber,
        hint = stringResource(id = R.string.enter_phone_number),
        modifier = Modifier.focusRequester(focusPhNumber),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()  // Hides the keyboard when the user presses "Done"
            nextFocus.requestFocus()  // Moves focus to the "Get OTP" button
        }),
        error = mobileNumberError,
        leadingIcon = { Text(text = "+91", style = normal18Text400, color = Color.Black) },
        onValueChange = {
            signInViewModel.onMobileNumberChanged(it)
            signInViewModel.updateMobileNumberError(null)
            if (it.length == 10) {
                keyboardController?.hide()
                nextFocus.requestFocus()
            }
        }
    )
}




@Preview
@Composable
fun SignInScreenPreview() {
    SignInScreen(navController = NavHostController(LocalContext.current))

}




