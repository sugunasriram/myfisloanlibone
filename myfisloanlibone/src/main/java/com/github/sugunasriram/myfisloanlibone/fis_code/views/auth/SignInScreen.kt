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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CenterProgress
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CenteredMoneyImage
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CurvedPrimaryButtonFull
import com.github.sugunasriram.myfisloanlibone.fis_code.components.HyperText
import com.github.sugunasriram.myfisloanlibone.fis_code.components.InnerScreenWithHamburger
import com.github.sugunasriram.myfisloanlibone.fis_code.components.InputField
import com.github.sugunasriram.myfisloanlibone.fis_code.components.NotRegisteredText
import com.github.sugunasriram.myfisloanlibone.fis_code.components.SignUpText
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateApplyByCategoryScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToForgotPasswordScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToRegisterScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlue
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal18Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal32Text500
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.slideActiveColor
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.textBlack
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.auth.SignInViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignInScreen(navController: NavHostController) {

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val signInViewModel: SignInViewModel = viewModel()

    val mobileNumber: String by signInViewModel.mobileNumber.observeAsState("")
    val password: String by signInViewModel.password.observeAsState("")
    val emailError: String? by signInViewModel.emailError.observeAsState("")
    val passwordError by signInViewModel.passwordError.observeAsState("")
    val isLoginSuccess = signInViewModel.isLoginSuccess.collectAsState()
    val isLoginInProgress = signInViewModel.isLoginInProgress.collectAsState()
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
    val (focusPassword) = FocusRequester.createRefs()
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
        InnerScreenWithHamburger(
            isHamBurgerVisible = false, isSelfScrollable = false, navController = navController
        ) {
            CenteredMoneyImage(
                image = R.drawable.sign_in_screen_image, imageSize = 280.dp, start = 70.dp,
                end = 70.dp
            )
            PhoneNumberFeild(
                mobileNumber = mobileNumber, focusPhNumber = focusPhNumber, emailError = emailError,
                focusPassword = focusPassword, signInViewModel = signInViewModel
            )

            PasswordFeild(
                password = password, focusPassword = focusPassword, passwordError = passwordError,
                signInViewModel = signInViewModel
            )

            HyperText(text = stringResource(id = R.string.forgot_password)) {
                navigateToForgotPasswordScreen(navController)
            }

            if (isLoginInProgress.value) {
                CenterProgress()
            } else {
                if (isLoginSuccess.value) {
                    navigateApplyByCategoryScreen(navController)
                } else {
                    CurvedPrimaryButtonFull(
                        text = stringResource(id = R.string.sign_in),
                        modifier = Modifier.padding(
                            start = 40.dp, end = 40.dp, bottom = 20.dp, top = 30.dp
                        )
                    ) {
                        signInViewModel.signInButtonValidation(
                            mobileNumber = mobileNumber, password = password, context = context,
                            focusPassword = focusPassword, focusPhNumber = focusPhNumber,
                        )
                    }
                }
            }

            NotRegisteredText(
                text = stringResource(id = R.string.register_user), padding = 0.dp
            )
            SignUpText(
                text = stringResource(id = R.string.sign_up),
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.robotocondensed_bold)),
                    fontWeight = FontWeight(800)
                ),
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .focusRequester(focusSignInButton)

            ) {
                navigateToRegisterScreen(navController)
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
fun PasswordFeild(
    password: String, focusPassword: FocusRequester, signInViewModel: SignInViewModel,
    passwordError: String?,
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

    InputField(
        inputText = password, hint = stringResource(id = R.string.enter_password),
        modifier = Modifier.focusRequester(focusPassword),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done, keyboardType = KeyboardType.Password
        ),
        onValueChange = {
            signInViewModel.onPasswordChanged(it)
            signInViewModel.updatePasswordError(null)
        },
        error = passwordError,
        trailingIcon = {
            IconButton(onClick = {
                passwordVisible = !passwordVisible
                signInViewModel.updateGeneralError(null)
            }) {
                Icon(
                    imageVector = image, stringResource(id = R.string.password_icon)
                )
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
    )
}

@Composable
fun PhoneNumberFeild(
    mobileNumber: String, focusPhNumber: FocusRequester, focusPassword: FocusRequester,
    emailError: String?, signInViewModel: SignInViewModel
) {
    InputField(
        inputText = mobileNumber, hint = stringResource(id = R.string.enter_register_phnumber),
        modifier = Modifier.focusRequester(focusPhNumber),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next, keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(onNext = { focusPassword.requestFocus() }),
        error = emailError,
        leadingIcon = {
            Text(
                text = "+91", style = normal18Text400, color = Color.Black
            )
        },
        onValueChange = {
            signInViewModel.onMobileNumberChanged(it)
            signInViewModel.updateEmailError(null)
        }
    )
}

@Preview
@Composable
fun SignInScreenPreview() {
    SignInScreen(navController = NavHostController(LocalContext.current))

}




