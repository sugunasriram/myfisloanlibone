package com.github.sugunasriram.myfisloanlibone.fis_code.views.auth

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appDarkTeal
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.semiBold24Text700
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.auth.SignInViewModel

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ResetPasswordScreen(navController: NavHostController, mobileNumber: String) {

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val signInViewModel: SignInViewModel = viewModel()

    val password: String by signInViewModel.password.observeAsState("")
    val confirmPassword: String by signInViewModel.confirmPassword.observeAsState("")
    val resetting by signInViewModel.resetting.collectAsState()
    val reSetted by signInViewModel.reSetted.collectAsState()
    val upDated by signInViewModel.upDated.collectAsState()
    val shownMsg by signInViewModel.shownMsg.collectAsState()

    val passwordError: String? by signInViewModel.passwordError.observeAsState("")
    val confirmPasswordError: String? by signInViewModel.confirmPasswordError.observeAsState("")
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

    val (focusPassword) = FocusRequester.createRefs()
    val (focusConfirmPassword) = FocusRequester.createRefs()



    BackHandler { navigateSignInPage(navController) }

    if (!showInternetScreen && !showTimeOutScreen && !showServerIssueScreen && !unexpectedErrorScreen) {
        ResetPasswordScreenView(
            resetting = resetting, reSetted = reSetted, navController = navController,
            signInViewModel = signInViewModel, password = password, context = context,
            confirmPassword = confirmPassword, focusPassword = focusPassword,
            focusConfirmPassword = focusConfirmPassword, mobileNumber = mobileNumber,
            passwordError = passwordError, confirmPasswordError = confirmPasswordError,
            shownMsg = shownMsg, upDated = upDated
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
fun ResetPasswordScreenView(
    resetting: Boolean, reSetted: Boolean, navController: NavHostController, context: Context,
    signInViewModel: SignInViewModel, password: String, confirmPassword: String,
    focusPassword: FocusRequester, focusConfirmPassword: FocusRequester, mobileNumber: String,
    passwordError: String?, confirmPasswordError: String?,shownMsg: Boolean, upDated: Boolean
) {
    if (resetting) {
        CenterProgress()
    } else {
        if (reSetted) {
            if (upDated && !shownMsg) {
                CommonMethods().toastMessage(
                    context = context, toastMsg = stringResource(id = R.string.password_updated)
                )
                signInViewModel.updateShownMsg(true)
                navigateSignInPage(navController)
            }
        } else {
            FixedTopBottomScreen(
                isSelfScrollable = false, navController = navController, showBackButton = true,
                buttonText = stringResource(id = R.string.submit),
                onBackClick = { navigateSignInPage(navController) },
                onClick = {
                    signInViewModel.resetButtonValidation(
                        password = password, confirmPassword = confirmPassword,
                        focusPassword = focusPassword, focusConfirmPassword = focusConfirmPassword,
                        context = context, mobileNumber = mobileNumber
                    )
                }
            ) {
                RegisterText(
                    text = stringResource(id = R.string.reset_password),
                    textColor = appDarkTeal, style = semiBold24Text700, top = 15.dp
                )
                CenteredMoneyImage(
                    image = R.drawable.reset_password_icon, imageSize = 300.dp, top = 10.dp,
                    contentScale = ContentScale.Fit
                )
                EnterPassword(
                    password = password, focusPassword = focusPassword,
                    signInViewModel = signInViewModel, focusConfirmPassword = focusConfirmPassword,
                    passwordError = passwordError
                )
                ConfirmPassword(
                    confirmPassword = confirmPassword, focusConfirmPassword = focusConfirmPassword,
                    signInViewModel = signInViewModel, confirmPasswordError = confirmPasswordError
                )
            }
        }
    }
}

@Composable
fun ConfirmPassword(
    confirmPassword: String, focusConfirmPassword: FocusRequester, signInViewModel: SignInViewModel,
    confirmPasswordError: String?
) {
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }
    val confirmImage =
        if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
    InputField(
        inputText = confirmPassword,
        hint = stringResource(id = R.string.confirm_password),
        modifier = Modifier.focusRequester(focusConfirmPassword),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done, keyboardType = KeyboardType.Password
        ),
        onValueChange = {
            signInViewModel.onConfirmPasswordChanged(it)
            signInViewModel.updateConfirmPasswordError(null)
        },
        error = confirmPasswordError,
        trailingIcon = {
            IconButton(onClick = {
                confirmPasswordVisible = !confirmPasswordVisible
                signInViewModel.updateGeneralError(null)
            }) {
                Icon(
                    imageVector = confirmImage,
                    stringResource(id = R.string.password_icon)
                )
            }
        },
        leadingIcon = {
            IconButton(onClick = {
                confirmPasswordVisible = !confirmPasswordVisible
                signInViewModel.updateGeneralError(null)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.lock),
                    stringResource(id = R.string.lock_icon)
                )
            }

        },
        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
    )
}

@Composable
fun EnterPassword(
    password: String, focusPassword: FocusRequester, signInViewModel: SignInViewModel,
    focusConfirmPassword: FocusRequester, passwordError: String?
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
    InputField(
        inputText = password, hint = stringResource(id = R.string.set_your_password),
        modifier = Modifier.focusRequester(focusPassword),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next, keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(onNext = {
            focusConfirmPassword.requestFocus()
        }),
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
        leadingIcon = {
            IconButton(onClick = {
                passwordVisible = !passwordVisible
                signInViewModel.updateGeneralError(null)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.lock),
                    stringResource(id = R.string.lock_icon)
                )
            }

        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
    )
}

@Preview
@Composable
fun ResetPasswordScreenPreview() {
    ResetPasswordScreen(rememberNavController(), "9876543210")
}