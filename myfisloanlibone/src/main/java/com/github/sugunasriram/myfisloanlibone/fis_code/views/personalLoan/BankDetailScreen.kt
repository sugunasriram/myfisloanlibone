package com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.AgreementAnimation
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CheckBoxNoText
import com.github.sugunasriram.myfisloanlibone.fis_code.components.FixedTopBottomScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.components.HyperText
import com.github.sugunasriram.myfisloanlibone.fis_code.components.NumberFullWidthBorderCard
import com.github.sugunasriram.myfisloanlibone.fis_code.components.StartingText
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateApplyByCategoryScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateSignInPage
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToAccountDetailsScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToLoanProcessScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.BankAccount
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.BankDetail
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.BankDetailResponse
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.DataItem
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.GstBankDetail
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.PfBankDetail
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.gst.GstOfferConfirmResponse
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.pf.PfOfferConfirmResponse
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlueTitle
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.azureBlueColor
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.bold16Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal14Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal16Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal30Text700
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.personalLoan.AccountDetailViewModel

@SuppressLint("ResourceType")
@Composable
fun BankDetailScreen(navController: NavHostController, id: String, fromFlow: String) {
    val context = LocalContext.current
    val accountDetailViewModel: AccountDetailViewModel = viewModel()

    val gettingBank by accountDetailViewModel.gettingBank.collectAsState()
    val gotBank by accountDetailViewModel.gotBank.collectAsState()
    val bankAccount by accountDetailViewModel.bankAccount.collectAsState()
    val bankDetailCollecting by accountDetailViewModel.bankDetailCollecting.collectAsState()
    val bankDetailCollected by accountDetailViewModel.bankDetailCollected.collectAsState()
    val bankDetailResponse by accountDetailViewModel.bankDetailResponse.collectAsState()
    val gstBankDetailResponse by accountDetailViewModel.gstBankDetailResponse.collectAsState()
    val pfBankDetailResponse by accountDetailViewModel.pfBankDetailResponse.collectAsState()
    val showInternetScreen by accountDetailViewModel.showInternetScreen.observeAsState(false)
    val showTimeOutScreen by accountDetailViewModel.showTimeOutScreen.observeAsState(false)
    val showServerIssueScreen by accountDetailViewModel.showServerIssueScreen.observeAsState(false)
    val unexpectedErrorScreen by accountDetailViewModel.unexpectedError.observeAsState(false)
    val unAuthorizedUser by accountDetailViewModel.unAuthorizedUser.observeAsState(false)
    val middleLoan by accountDetailViewModel.middleLoan.observeAsState(false)
    val errorMessage by accountDetailViewModel.errorMessage.collectAsState()

    val navigationToSignIn by accountDetailViewModel.navigationToSignIn.collectAsState()

    val (selectedBankDetail, setSelectedBankDetail) = remember { mutableStateOf<DataItem?>(null) }
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
        navigationToSignIn -> navigateSignInPage(navController)
        showInternetScreen -> CommonMethods().ShowInternetErrorScreen(navController)
        showTimeOutScreen -> CommonMethods().ShowTimeOutErrorScreen(navController)
        showServerIssueScreen -> CommonMethods().ShowServerIssueErrorScreen(navController)
        unexpectedErrorScreen -> CommonMethods().ShowUnexpectedErrorScreen(navController)
        unAuthorizedUser -> CommonMethods().ShowUnAuthorizedErrorScreen(navController)
        middleLoan -> CommonMethods().ShowNoResponseFormLendersScreen(navController)
        else -> {
            if (gettingBank) {
                AgreementAnimation(text = "", image = R.raw.processing_please_wait)
            } else {
                if (gotBank) {
                    if (bankAccount?.data?.isEmpty() != true) {
                        if (bankDetailCollecting) {
                            AgreementAnimation(text = "", image = R.raw.processing_please_wait)
                        }
                        if (bankDetailCollected) {
                            onBankDetailsCollected(
                                fromFlow = fromFlow, navController = navController,
                                gstBankDetailResponse = gstBankDetailResponse,
                                pfBankDetailResponse = pfBankDetailResponse,
                                bankDetailResponse = bankDetailResponse
                            )
                        }
                        if (!bankDetailCollecting) {
                            BankDetailCollecting(
                                navController = navController,
                                selectedBankDetail = selectedBankDetail,
                                accountDetailViewModel = accountDetailViewModel,
                                fromFlow = fromFlow, context = context, id = id,
                                bankAccount = bankAccount,
                                setSelectedBankDetail = setSelectedBankDetail
                            )
                        }
                    } else {
                        navigateToAccountDetailsScreen(navController, id, fromFlow)
                    }
                } else {
                    accountDetailViewModel.getBankAccount(context)
                }
            }
        }
    }
}

@Composable
fun BankDetailCollecting(
    navController: NavHostController, selectedBankDetail: DataItem?,
    accountDetailViewModel: AccountDetailViewModel, fromFlow: String, context: Context,
    id: String, bankAccount: BankAccount?, setSelectedBankDetail: (DataItem?) -> Unit
) {
    var backPressedTime by remember { mutableLongStateOf(0L) }
    FixedTopBottomScreen(
        navController = navController,
        showBottom = true,
        isSelfScrollable = true,
        buttonText = stringResource(id = R.string.submit),
        backGroudColorChange = selectedBankDetail != null,
        onBackClick = {
            val currentTime = System.currentTimeMillis()
            if (currentTime - backPressedTime < 2000) {
                navigateApplyByCategoryScreen(navController)
            } else {
                CommonMethods().toastMessage(
                    context = context, toastMsg = "Press back again to go to the Home page"
                )
                backPressedTime = currentTime
            }
        },
        onClick = {
            onBankDetailsSubmit(
                selectedBankDetail = selectedBankDetail,
                accountDetailViewModel = accountDetailViewModel,
                fromFlow = fromFlow, context = context,
                navController = navController, id = id
            )
        }
    ) {
        StartingText(
            text = stringResource(id = R.string.add_account_details),
            textColor = appBlueTitle,
            alignment = Alignment.Center,
            start = 20.dp, end = 30.dp, top = 15.dp, bottom = 15.dp,
            style = normal30Text700,
            textAlign = TextAlign.Center,
        )
        StartingText(
            text = stringResource(id = R.string.adding_your_bank_account),
            start = 30.dp, end = 30.dp, bottom = 15.dp, top = 15.dp,
            style = normal14Text400,
            alignment = Alignment.Center,
            textAlign = TextAlign.Center,
        )
        StartingText(
            text = stringResource(id = R.string.account_details),
            start = 30.dp, end = 30.dp, bottom = 15.dp, top = 15.dp,
            style = bold16Text400,
        )
        BankDetailCard(
            context = context, id = id,
            accountDetailViewModel = accountDetailViewModel,
            selectedBankDetail = selectedBankDetail,
            bankAccount = bankAccount, fromFlow = fromFlow,
            navController = navController,
            onCheckedChange = { selectedDetail ->
                setSelectedBankDetail(selectedDetail)
            }
        )
        HyperText(
            text = stringResource(id = R.string.add_account_details_plus),
            alignment = Alignment.TopCenter, boxTop = 15.dp,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            navigateToAccountDetailsScreen(navController, id, fromFlow)
        }
    }
}

fun onBankDetailsCollected(
    fromFlow: String, navController: NavHostController,
    gstBankDetailResponse: GstOfferConfirmResponse?,
    pfBankDetailResponse: PfOfferConfirmResponse?,
    bankDetailResponse: BankDetailResponse?
) {
    if (fromFlow.equals("Personal Loan", ignoreCase = true)) {
        bankDetailResponse?.let { response ->
            response.data?.eNACHUrlObject?.txnId?.let { transactionId ->
                response.data?.eNACHUrlObject?.formUrl?.let { url ->
                    response.data.id?.let { id ->
                        navigateToLoanProcessScreen(
                            navController = navController, transactionId = transactionId,
                            statusId = 5,
                            responseItem = url, offerId = id, fromFlow = fromFlow
                        )
                    }
                }
            }
        }
    } else if(fromFlow.equals("Purchase Finance", ignoreCase = true)){
        pfBankDetailResponse?.let { response ->
            response.data?.eNACHUrlObject?.txnID?.let { transactionId ->
                response.data?.eNACHUrlObject?.fromURL?.let { eNachUrl ->
                    response.data.eNACHUrlObject.itemID?.let { offerId ->
                        navigateToLoanProcessScreen(
                            navController = navController,
                            statusId = 15, transactionId=transactionId,
                            responseItem = eNachUrl, offerId = offerId, fromFlow = fromFlow
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
                            navController = navController,
                            statusId = 15, transactionId=transactionId,
                            responseItem = eNachUrl, offerId = offerId, fromFlow = fromFlow
                        )
                    }
                }
            }
        }
    }
}

fun onBankDetailsSubmit(
    selectedBankDetail: DataItem?, accountDetailViewModel: AccountDetailViewModel,
    fromFlow: String, context: Context, navController: NavHostController, id: String
) {
    selectedBankDetail?.let { selectedDetail ->
        if (fromFlow.equals("Personal Loan", ignoreCase = true)) {
            accountDetailViewModel.addBankDetail(
                context,
                BankDetail(
                    accountNumber = selectedDetail.bankAccountNumber.toString(),
                    accountHolderName = selectedDetail.accountHolderName.toString(),
                    accountType = if (selectedDetail.accountType.equals(
                            "Savings",
                            ignoreCase = true
                        )
                    )
                        "saving" else "saving",
                    id = id,
                    ifscCode = selectedDetail.bankIfscCode.toString(),
                    loanType = "PERSONAL_LOAN"
                ),
                navController
            )
        } else if(fromFlow.equals("Purchase Finance", ignoreCase = true)){
            accountDetailViewModel.pfLoanEntityApproval(
                bankDetail = PfBankDetail(
                    accountNumber = selectedDetail.bankAccountNumber.toString(),
                    ifscCode = selectedDetail.bankIfscCode.toString(),
                    accountHolderName = selectedDetail.accountHolderName.toString(),
                    id = id,
                    loanType = "PURCHASE_FINANCE"
                ),
                context = context
            )
        } else {
            accountDetailViewModel.gstLoanEntityApproval(
                bankDetail = GstBankDetail(
                    accountNumber = selectedDetail.bankAccountNumber.toString(),
                    ifscCode = selectedDetail.bankIfscCode.toString(),
                    accountHolderName = selectedDetail.accountHolderName.toString(),
                    id = id,
                    loanType = "INVOICE_BASED_LOAN"
                ),
                context = context
            )
        }
    } ?: CommonMethods().toastMessage(context = context, toastMsg = "No bank detail selected")
}

@Composable
fun BankDetailCard(
    context: Context, id: String, accountDetailViewModel: AccountDetailViewModel,
    selectedBankDetail: DataItem?, bankAccount: BankAccount?, navController: NavHostController,
    fromFlow: String, onCheckedChange: ((DataItem?) -> Unit)
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        bankAccount?.data?.let { bankDetailsList ->
            items(bankDetailsList) { bankDetails ->
                val isChecked = selectedBankDetail == bankDetails
                NumberFullWidthBorderCard(borderColor = azureBlueColor, cardColor = Color.White) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onBankCardClicked(
                                    bankDetails = bankDetails, onCheckedChange = onCheckedChange,
                                    fromFlow = fromFlow, context = context,
                                    accountDetailViewModel = accountDetailViewModel,
                                    navController = navController, id = id
                                )
                            }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.bank_icon),
                            contentDescription = stringResource(id = R.string.bank_image),
                            modifier = Modifier.size(40.dp)
                        )

                        Column(
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.weight(1f)
                        ) {
                            bankDetails?.accountHolderName?.let { accountHolderName ->
                                StartingText(
                                    text = accountHolderName,
                                    start = 30.dp, end = 30.dp, bottom = 5.dp, top = 15.dp,
                                    style = bold16Text400,
                                    textAlign = TextAlign.Start,
                                )
                            }
                            bankDetails?.bankAccountNumber?.let { bankAccountNumber ->
                                StartingText(
                                    text = bankAccountNumber,
                                    start = 30.dp, end = 30.dp, bottom = 5.dp,
                                    style = normal16Text400,
                                    textAlign = TextAlign.Start,
                                )
                            }
                            bankDetails?.bankIfscCode?.let { bankIfscCode ->
                                StartingText(
                                    text = bankIfscCode,
                                    start = 30.dp, end = 30.dp, bottom = 15.dp,
                                    style = normal16Text400,
                                    textAlign = TextAlign.Start,
                                )
                            }
                        }
                        CheckBoxNoText(
                            boxState = isChecked,
                            onCheckedChange = { checked ->
                                if (checked) {
                                    onCheckedChange(bankDetails)
                                } else if (selectedBankDetail == bankDetails) {
                                    onCheckedChange(null)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

fun onBankCardClicked(
    bankDetails: DataItem?,
    onCheckedChange: (DataItem?) -> Unit,
    fromFlow: String,
    accountDetailViewModel: AccountDetailViewModel,
    context: Context,
    navController: NavHostController,
    id: String
) {
    onCheckedChange(bankDetails)
    bankDetails?.accountHolderName?.let { accountHolderName ->
        bankDetails.bankIfscCode?.let { bankIfscCode ->
            bankDetails.bankAccountNumber?.let { bankAccountNumber ->
                bankDetails.accountType?.let { accountType ->
                    if (fromFlow.equals("Personal Loan", ignoreCase = true)) {
                        val accountType = if (accountType.equals("Savings", ignoreCase = true))
                            "saving" else "current"
                        accountDetailViewModel.addBankDetail(
                            context = context,
                            bankDetail = BankDetail(
                                accountNumber = bankAccountNumber,
                                accountHolderName = accountHolderName,
                                accountType = accountType,
                                id = id,
                                ifscCode = bankIfscCode,
                                loanType = "PERSONAL_LOAN"
                            ),
                            navController = navController
                        )
                    } else {
                        accountDetailViewModel.gstLoanEntityApproval(
                            bankDetail = GstBankDetail(
                                accountNumber = bankAccountNumber,
                                ifscCode = bankIfscCode,
                                accountHolderName = accountHolderName,
                                id = id,
                                loanType = "INVOICE_BASED_LOAN"
                            ),
                            context = context
                        )
                    }
                }
            }
        }
    }
}

