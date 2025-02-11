package com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CenterProgress
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CenteredMoneyImage
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CurvedPrimaryButtonFull
import com.github.sugunasriram.myfisloanlibone.fis_code.components.DropDown
import com.github.sugunasriram.myfisloanlibone.fis_code.components.InnerScreenWithHamburger
import com.github.sugunasriram.myfisloanlibone.fis_code.components.NumberFullWidthBorderCard
import com.github.sugunasriram.myfisloanlibone.fis_code.components.RegisterText
import com.github.sugunasriram.myfisloanlibone.fis_code.components.SpaceBetweenText
import com.github.sugunasriram.myfisloanlibone.fis_code.components.TextInputLayout
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateSignInPage
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToLoanProcessScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToReviewDetailsScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlue
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlueTitle
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appWhite
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.cursorColor
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.customBlueColor
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.disableColor
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal20Text500
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal32Text700
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CustomSnackBar
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.showSnackError
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.personalLoan.AnnualIncomeViewModel
import kotlin.math.roundToInt

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AnnualIncomeScreen(navController: NavHostController,fromFlow:String) {
    val context = LocalContext.current

    val annualIncomeViewModel: AnnualIncomeViewModel = viewModel()
    val income: Int by annualIncomeViewModel.income.observeAsState(0)
    val generalError by annualIncomeViewModel.generalError.observeAsState("")
    val selectedPurpose: String by annualIncomeViewModel.selectedPurpose.observeAsState("Purpose")
    val sliderPosition: Float by annualIncomeViewModel.sliderPosition.observeAsState(0f)
    val updatingIncome by annualIncomeViewModel.updatingIncome.collectAsState()
    val updatedIncome by annualIncomeViewModel.updatedIncome.collectAsState()
    val updateResponseData by annualIncomeViewModel.updatedIncomeResponse.collectAsState()
    val showInternetScreen by annualIncomeViewModel.showInternetScreen.observeAsState(false)
    val showTimeOutScreen by annualIncomeViewModel.showTimeOutScreen.observeAsState(false)
    val showServerIssueScreen by annualIncomeViewModel.showServerIssueScreen.observeAsState(false)
    val unexpectedErrorScreen by annualIncomeViewModel.unexpectedError.observeAsState(false)
    val unAuthorizedUser by annualIncomeViewModel.unAuthorizedUser.observeAsState(false)

    val navigationToSignIn by annualIncomeViewModel.navigationToSignIn.collectAsState()

    val focusManager = LocalFocusManager.current
    val (incomeFocus) = FocusRequester.createRefs()
    val (purposeFocus) = FocusRequester.createRefs()

    val snackState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var mExpanded by rememberSaveable { mutableStateOf(false) }

    val minRange = 30000f
    val maxRange = 2000000f
    val stepSize = 5000f
    val numberOfSteps = ((maxRange - minRange) / stepSize).toInt() - 1


    val purpose = listOf(
        stringResource(id = R.string.travel),
        stringResource(id = R.string.education),
        stringResource(id = R.string.health),
        stringResource(id = R.string.consumer_durable_purchase),
        stringResource(id = R.string.other_consumption_purpose)
    )

    val onItemSelected: (String) -> Unit = { selectedText ->
        annualIncomeViewModel.updateSelectedPurpose(selectedText)
        mExpanded = false
        purposeFocus.requestFocus()
    }

    val onDismiss: () -> Unit = { mExpanded = false }

    DisposableEffect(income) {
        val incomeWithoutSymbol = income
        val initialIncome = incomeWithoutSymbol
//        val newPosition = ((initialIncome - 30000) * 100f / 1970000).coerceIn(0f, 100f)

        val roundedValue = (initialIncome / stepSize).roundToInt() * stepSize.toFloat()

//        annualIncomeViewModel.updateSliderPosition(newPosition,context)
        annualIncomeViewModel.updateSliderPosition(roundedValue,context)
        onDispose { }
    }

    BackHandler {
        navigateToLoanProcessScreen(navController, transactionId="Sugu", 1, context.getString(R
            .string
            .loan), "1234",fromFlow = "Personal Loan")
    }

    when {
        navigationToSignIn -> navigateSignInPage (navController)
        showInternetScreen -> CommonMethods().ShowInternetErrorScreen(navController)
        showTimeOutScreen -> CommonMethods().ShowTimeOutErrorScreen(navController)
        showServerIssueScreen -> CommonMethods().ShowServerIssueErrorScreen(navController)
        unexpectedErrorScreen -> CommonMethods().ShowUnexpectedErrorScreen(navController)
        unAuthorizedUser -> CommonMethods().ShowUnAuthorizedErrorScreen(navController)
        else -> {
            InnerScreenWithHamburger(isSelfScrollable = false, navController = navController) {
                CenteredMoneyImage(imageSize = 150.dp, top = 10.dp)
                RegisterText(
                    text = stringResource(id = R.string.annual_income),
                    bottom = 0.dp,
                    textColor = appBlueTitle, style = normal32Text700
                )

                val formattedIncome = annualIncomeViewModel.formatIncome(income)

                TextInputLayout(
                    textFieldVal = TextFieldValue(
                        text = formattedIncome, selection = TextRange(formattedIncome.length)
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number
                    ),
                    keyboardActions = KeyboardActions(onDone = {}),
                    onTextChanged = { newText ->
                        annualIncomeViewModel.onIncomeChanged(context = context, newText.text)
                        if (newText.text.replace("₹", "").replace(",", "").isEmpty()) {
                            annualIncomeViewModel.updateGeneralError(null)
                        }
                    },
                    onLostFocusValidation = { newText ->
                        annualIncomeViewModel.validateInputText(context = context, newText.text)
                    },
                    hintText = stringResource(id = R.string.rupee),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp, end = 30.dp)
                        .focusRequester(incomeFocus),
                    readOnly = true
                )




                Slider(
                    value = sliderPosition,
//            onValueChange = { newValue ->
//                annualIncomeViewModel.updateSliderPosition(newValue,context)
//            },
                    onValueChange = { newValue ->
                        val roundedValue = (newValue / stepSize).roundToInt() * stepSize.toFloat()
                        annualIncomeViewModel.updateSliderPosition(roundedValue, context)
                    },
//            valueRange = 0f..100f, // Since slider goes from 0 to 100
                    valueRange = 30000f..2000000f, // Since slider goes from 0 to 100
                    steps = numberOfSteps,
                    colors = SliderDefaults.colors(
                        thumbColor = appBlue,
                        activeTickColor = appBlue,
                        inactiveTrackColor = customBlueColor,
                    ),
                    modifier = Modifier.padding(start = 30.dp, end = 30.dp, top = 10.dp)
                )

                SpaceBetweenText(
                    text = stringResource(id = R.string.tewnty_k),
                    value = stringResource(id = R.string.tewnty_lakh),
                    start = 35.dp,
                    end = 35.dp,
                    top = 0.dp
                )

                NumberFullWidthBorderCard(
                    cardColor = appWhite,
                    borderColor = cursorColor,
                    start = 30.dp, end = 30.dp, top = 20.dp,
                ) {
                    Row(
                        modifier = Modifier
                            .padding(15.dp)
                            .fillMaxWidth()
                            .focusRequester(purposeFocus)
                            .clickable {
                                mExpanded = !mExpanded
                                if (mExpanded) {
                                    focusManager.clearFocus()
                                    purposeFocus.requestFocus()
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = selectedPurpose,
                                style = normal20Text500,
                            )
                        }
                        Image(
                            painter = painterResource(id = R.drawable.down_ward_image),
                            contentDescription = stringResource(id = R.string.down_ward_image),
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .size(32.dp)
                                .onFocusChanged { focusState ->
                                    if (focusState.isFocused) {
                                        purposeFocus.requestFocus()
                                    }
                                }
                        )
                    }
                    DropDown(
                        mExpanded = mExpanded,
                        purpose = purpose,
                        onItemSelected = onItemSelected,
                        onDismiss = onDismiss,
                        modifier = Modifier
                            .padding(15.dp)
                            .fillMaxWidth()
                            .focusRequester(purposeFocus)
                            .onFocusChanged { focusState ->
                                if (focusState.isFocused) {
                                    purposeFocus.requestFocus()
                                }
                            }
                    )
                }

                if (!generalError.isNullOrEmpty()) {
                    showSnackError(coroutineScope, snackState, generalError)
                    SnackbarHost(hostState = snackState) {
                        generalError?.let {
                            CustomSnackBar(message = it, containerColor = Color.Red, top = 10.dp)
                        }
                    }
                }
                if (updatingIncome){
                    CenterProgress(top = 50.dp)
                } else {
                    if (updatedIncome){
                        navigateToReviewDetailsScreen(navController, selectedPurpose,fromFlow)
                    } else {
                        CurvedPrimaryButtonFull(
                            text = stringResource(id = R.string.next),
                            modifier = Modifier.padding(start = 30.dp, end = 30.dp, bottom = 20.dp, top = 50.dp),
                            backgroundColor = if(selectedPurpose.isEmpty() || selectedPurpose.equals("purpose",ignoreCase = true)) disableColor else appBlue,
                        ) {
                            annualIncomeViewModel.onNextClicked(context,selectedPurpose,income)
                        }
                    }
                }
            }
        }
    }
}


