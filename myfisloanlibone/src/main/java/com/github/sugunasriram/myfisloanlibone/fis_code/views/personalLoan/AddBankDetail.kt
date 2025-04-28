package com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.AgreementAnimation
import com.github.sugunasriram.myfisloanlibone.fis_code.components.DropDownTextField
import com.github.sugunasriram.myfisloanlibone.fis_code.components.FixedTopBottomScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.components.InputField
import com.github.sugunasriram.myfisloanlibone.fis_code.components.RegisterText
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.AppScreens
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateApplyByCategoryScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateSignInPage
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToLoanProcessScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.BankDetail
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.BankDetailResponse
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.gst.GstOfferConfirmResponse
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal24Text700
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.personalLoan.AccountDetailViewModel
import java.util.Locale

@SuppressLint("ResourceType")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddBankDetailScreen(navController: NavHostController, id: String, fromFlow: String) {

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val accountDetailViewModel: AccountDetailViewModel = viewModel()
    val accountHolder: String by accountDetailViewModel.accountHolder.observeAsState("")
    val accountNumber: String by accountDetailViewModel.accountNumber.observeAsState("")
    val ifscCode: String by accountDetailViewModel.ifscCode.observeAsState("")

    val bankDetailCollecting by accountDetailViewModel.bankDetailCollecting.collectAsState()
    val bankDetailCollected by accountDetailViewModel.bankDetailCollected.collectAsState()
    val bankDetailResponse by accountDetailViewModel.bankDetailResponse.collectAsState()
    val gstBankDetailResponse by accountDetailViewModel.gstBankDetailResponse.collectAsState()

    val accountHolderError: String? by accountDetailViewModel.accountHolderError.observeAsState("")
    val accountNumberError: String? by accountDetailViewModel.accountNumberError.observeAsState("")
    val ifscCodeError: String? by accountDetailViewModel.ifscCodeError.observeAsState("")
    val showInternetScreen by accountDetailViewModel.showInternetScreen.observeAsState(false)
    val showTimeOutScreen by accountDetailViewModel.showTimeOutScreen.observeAsState(false)
    val showServerIssueScreen by accountDetailViewModel.showServerIssueScreen.observeAsState(false)
    val unexpectedErrorScreen by accountDetailViewModel.unexpectedError.observeAsState(false)
    val unAuthorizedUser by accountDetailViewModel.unAuthorizedUser.observeAsState(false)
    val shouldShowKeyboard by accountDetailViewModel.shouldShowKeyboard.observeAsState(false)
    val middleLoan by accountDetailViewModel.middleLoan.observeAsState(false)
    val errorMessage by accountDetailViewModel.errorMessage.collectAsState()
    val navigationToSignIn by accountDetailViewModel.navigationToSignIn.collectAsState()

    LaunchedEffect(shouldShowKeyboard) {
        if (shouldShowKeyboard) {
            keyboardController?.show()
            accountDetailViewModel.resetKeyboardRequest()
        }
    }

    val (focusAccountHolder) = FocusRequester.createRefs()
    val (focusAccountNumber) = FocusRequester.createRefs()
    val (focusIfscCode) = FocusRequester.createRefs()
    val (focusAccountType) = FocusRequester.createRefs()
    var backPressedTime by remember { mutableLongStateOf(0L) }
    BackHandler {
        val currentTime = System.currentTimeMillis()
        if (currentTime - backPressedTime < 2000) {
            navigateApplyByCategoryScreen(navController)
        } else {
            CommonMethods().toastMessage(
                context = context, toastMsg = "Press back again to go to the Home page"
            )
            backPressedTime = currentTime
        }
    }


    when {
        navigationToSignIn -> navigateSignInPage (navController)
        showInternetScreen -> CommonMethods().ShowInternetErrorScreen(navController)
        showTimeOutScreen -> CommonMethods().ShowTimeOutErrorScreen(navController)
        showServerIssueScreen -> CommonMethods().ShowServerIssueErrorScreen(navController)
        unexpectedErrorScreen -> CommonMethods().ShowUnexpectedErrorScreen(navController)
        unAuthorizedUser -> CommonMethods().ShowUnAuthorizedErrorScreen(navController)
        middleLoan -> CommonMethods().ShowNoResponseFormLendersScreen(navController)
        else -> {
            AddBankDetailView(
                bankDetailCollecting = bankDetailCollecting, fromFlow = fromFlow,
                bankDetailCollected = bankDetailCollected, context = context,
                bankDetailResponse = bankDetailResponse, ifscCodeError = ifscCodeError,
                gstBankDetailResponse = gstBankDetailResponse, navController = navController,
                accountHolder = accountHolder, accountNumber = accountNumber, ifscCode = ifscCode,
                accountHolderError = accountHolderError, accountNumberError = accountNumberError,
                id = id, accountDetailViewModel = accountDetailViewModel,
                focusAccountHolder = focusAccountHolder, focusAccountNumber = focusAccountNumber,
                focusAccountType = focusAccountType, focusIfscCode = focusIfscCode,
            )
        }
    }
}

@SuppressLint("ResourceType")
@Composable
fun AddBankDetailView(
    bankDetailCollecting: Boolean, bankDetailCollected: Boolean, fromFlow: String,
    bankDetailResponse: BankDetailResponse?, gstBankDetailResponse: GstOfferConfirmResponse?,
    navController: NavHostController, context: Context, accountHolder: String, id: String,
    accountNumber: String, ifscCode: String, accountHolderError: String?,
    accountNumberError: String?, accountDetailViewModel: AccountDetailViewModel,
    focusAccountHolder: FocusRequester, focusAccountNumber: FocusRequester,
    focusAccountType: FocusRequester, focusIfscCode: FocusRequester, ifscCodeError: String?,
) {

    var accountTypeExpand by remember { mutableStateOf(false) }
    val accountTypeList = listOf("Current", "Saving")
    var accountSelectedText by remember { mutableStateOf("") }
    val onAccountDismiss: () -> Unit = { accountTypeExpand = false }
    val onAccountSelected: (String) -> Unit = { selectedText -> accountSelectedText = selectedText }

    if (bankDetailCollecting) {
        AgreementAnimation(
            text = stringResource(id = R.string.submitting_bank_details),
            image = R.raw.submitting_bank_details
        )
    } else {
        if (bankDetailCollected) {
            onBankDetailCollected(
                navController = navController, gstBankDetailResponse = gstBankDetailResponse,
                bankDetailResponse = bankDetailResponse, fromFlow = fromFlow
            )
        } else {
            FixedTopBottomScreen(
                navController = navController, showBottom = true, showBackButton = true,
                isSelfScrollable = false, onBackClick = {
                    navController.navigate(AppScreens.ApplyBycategoryScreen.route) {
                        popUpTo(AppScreens.AccountDetailsScreen.route) { inclusive = false }
                    }
                },
                buttonText = stringResource(id = R.string.submit),
                onClick = {
                    onBankSubmit(
                        accountDetailViewModel = accountDetailViewModel,
                        accountHolder = accountHolder, accountSelectedText = accountSelectedText,
                        accountNumber = accountNumber, context = context, ifscCode = ifscCode,
                        focusIfscCode = focusIfscCode, focusAccountType = focusAccountType,
                        focusAccountNumber = focusAccountNumber, fromFlow = fromFlow,
                        focusAccountHolder = focusAccountHolder, id = id,
                        navController = navController,
                    )
                }
            ) {
                RegisterText(
                    text = stringResource(id = R.string.enter_account_details),
                    style = normal24Text700, top = 30.dp, start = 20.dp, end = 20.dp
                )

                AccountHolderFeild(
                    accountHolder = accountHolder, focusAccountHolder = focusAccountHolder,
                    focusAccountType = focusAccountType,
                    accountDetailViewModel = accountDetailViewModel,
                    accountHolderError = accountHolderError
                )

                if (fromFlow.equals("Personal Loan", ignoreCase = true)) {
                    DropDownTextField(
                        hint = stringResource(id = R.string.account_type),
                        expand = accountTypeExpand, start = 40.dp, end = 40.dp, top = 20.dp,
                        selectedText = accountSelectedText, focus = focusAccountType,
                        onNextFocus = focusAccountNumber, setExpand = { accountTypeExpand = it },
                        itemList = accountTypeList, onDismiss = onAccountDismiss,
                        onItemSelected = onAccountSelected
                    )
                }
                AccountNuberFeild(
                    accountNumber = accountNumber, focusAccountNumber = focusAccountNumber,
                    focusIfscCode = focusIfscCode, accountDetailViewModel = accountDetailViewModel,
                    accountNumberError = accountNumberError,
                )
                IfscCodeFeild(
                    accountDetailViewModel = accountDetailViewModel, focusIfscCode = focusIfscCode,
                    ifscCode = ifscCode, ifscCodeError = ifscCodeError
                )
            }
        }
    }
}

@Composable
fun IfscCodeFeild(
    ifscCode: String, ifscCodeError: String?, accountDetailViewModel: AccountDetailViewModel,
    focusIfscCode: FocusRequester
) {
    InputField(
        inputText = ifscCode,
        modifier = Modifier.focusRequester(focusIfscCode),
        hint = stringResource(id = R.string.bank_ifsc_code),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.Characters
        ),
        keyboardActions = KeyboardActions(onNext = {}),
        onValueChange = {
            accountDetailViewModel.onIfscCodeChanged(it)
            accountDetailViewModel.updateIfscCodeError(null)
        },
        error = ifscCodeError

    )
}

@Composable
fun AccountHolderFeild(
    accountHolder: String, focusAccountHolder: FocusRequester, focusAccountType: FocusRequester,
    accountDetailViewModel: AccountDetailViewModel, accountHolderError: String?
) {
    InputField(
        inputText = accountHolder,
        modifier = Modifier.focusRequester(focusAccountHolder),
        hint = stringResource(id = R.string.account_holder_name),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(onNext = {
            focusAccountType.requestFocus()
        }),
        onValueChange = {
            accountDetailViewModel.onAccountHolderChanged(it)
            accountDetailViewModel.updateAccountHolderError(null)
        },
        error = accountHolderError

    )
}

@Composable
fun AccountNuberFeild(
    accountNumber: String, focusAccountNumber: FocusRequester, focusIfscCode: FocusRequester,
    accountDetailViewModel: AccountDetailViewModel, accountNumberError: String?,
) {
    InputField(
        inputText = accountNumber,
        modifier = Modifier.focusRequester(focusAccountNumber),
        hint = stringResource(id = R.string.bank_account_number),
        keyboardActions = KeyboardActions(onNext = {
            focusIfscCode.requestFocus()
        }),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        onValueChange = {
            accountDetailViewModel.onAccountNumberChanged(it)
            accountDetailViewModel.updateAccountNumberError(null)
        },
        error = accountNumberError
    )
}

fun onBankDetailCollected(
    bankDetailResponse: BankDetailResponse?, fromFlow: String, navController: NavHostController,
    gstBankDetailResponse: GstOfferConfirmResponse?
) {
    if (fromFlow.equals("Personal Loan", ignoreCase = true)) {
        bankDetailResponse?.let { response ->
            response.data?.eNACHUrlObject?.txnId?.let { transactionId ->
                response.data?.eNACHUrlObject?.formUrl?.let { enachUrl ->
                    response.data.id?.let { id ->
                        navigateToLoanProcessScreen(
                            navController, transactionId=transactionId,5, enachUrl, id, fromFlow = fromFlow
                        )
                    }
                }
            }
        }
    } else {
        gstBankDetailResponse?.let { response ->
            response.data?.eNACHUrlObject?.txnID?.let { transactionId ->
            response.data?.eNACHUrlObject?.fromURL?.let { eNachUrl ->
                response.data.eNACHUrlObject.itemID?.let { offerId ->
                    navigateToLoanProcessScreen(
                        navController, transactionId = transactionId,15, eNachUrl, offerId,
                        fromFlow = fromFlow
                    )
                }
            }
            }
        }
    }
}

fun onBankSubmit(
    fromFlow: String, navController: NavHostController, accountSelectedText: String,
    accountDetailViewModel: AccountDetailViewModel, accountHolder: String, accountNumber: String,
    focusIfscCode: FocusRequester, focusAccountType: FocusRequester, context: Context,
    ifscCode: String, focusAccountNumber: FocusRequester, id: String,
    focusAccountHolder: FocusRequester
) {
    if (fromFlow.equals("Personal Loan", ignoreCase = true)) {
        val accountType = accountSelectedText.lowercase(Locale.ROOT)
        accountDetailViewModel.accountDetailValidation(
            context = context, accountNumber = accountNumber, accountHolder = accountHolder,
            ifscCode = ifscCode, focusAccountHolder = focusAccountHolder,
            focusAccountNumber = focusAccountNumber, focusIfscCode = focusIfscCode,
            accountSelectedText = accountSelectedText, focusAccountType = focusAccountType,
            bankDetail = BankDetail(
                accountNumber = accountNumber, accountHolderName = accountHolder,
                ifscCode = ifscCode, accountType = accountType, id = id,
                loanType = "PERSONAL_LOAN"
            ),
            navController
        )
    } else {
        accountDetailViewModel.accountDetailValidation(
            context = context, accountNumber = accountNumber, accountHolder = accountHolder,
            focusAccountNumber = focusAccountNumber, focusIfscCode = focusIfscCode, id = id,
            ifscCode = ifscCode, focusAccountHolder = focusAccountHolder
        )
    }
}

@Preview
@Composable
fun AccountDetailsScreenPreview() {
    AddBankDetailScreen(
        navController = NavHostController(LocalContext.current), id = "1",
        fromFlow = "Personal Loan"
    )
}
