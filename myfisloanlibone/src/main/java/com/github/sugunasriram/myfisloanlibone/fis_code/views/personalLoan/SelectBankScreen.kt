package com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CenterProgress
import com.github.sugunasriram.myfisloanlibone.fis_code.components.FixedTopBottomScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.components.SpaceTextWithRadio
import com.github.sugunasriram.myfisloanlibone.fis_code.components.StartingText
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateSignInPage
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToAccountAgreegatorScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToSelectAccountAgreegatorScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.BankList
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlueTitle
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal14Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal32Text700
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.personalLoan.AccountDetailViewModel

@Composable
fun SelectBankScreen(navController: NavHostController, purpose: String, fromFlow: String) {

    BackHandler {
        if (fromFlow.equals("Invoice Loan", ignoreCase = true)) {
            navigateToAccountAgreegatorScreen(
                navController = navController, purpose = purpose, fromFlow = fromFlow
            )
        }
    }
    val (selectedOption, onOptionSelected) = remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    val accountDetailViewModel: AccountDetailViewModel = viewModel()
    val bankList by accountDetailViewModel.getBankList.collectAsState()
    val inProgress by accountDetailViewModel.inProgress.collectAsState()
    val isCompleted by accountDetailViewModel.isCompleted.collectAsState()
    val showInternetScreen by accountDetailViewModel.showInternetScreen.observeAsState(false)
    val showTimeOutScreen by accountDetailViewModel.showTimeOutScreen.observeAsState(false)
    val showServerIssueScreen by accountDetailViewModel.showServerIssueScreen.observeAsState(false)
    val unexpectedErrorScreen by accountDetailViewModel.unexpectedError.observeAsState(false)
    val unAuthorizedUser by accountDetailViewModel.unAuthorizedUser.observeAsState(false)
    val navigationToSignIn by accountDetailViewModel.navigationToSignIn.collectAsState()

    when {
        navigationToSignIn -> navigateSignInPage (navController)
        showInternetScreen -> CommonMethods().ShowInternetErrorScreen(navController)
        showTimeOutScreen -> CommonMethods().ShowTimeOutErrorScreen(navController)
        showServerIssueScreen -> CommonMethods().ShowServerIssueErrorScreen(navController)
        unexpectedErrorScreen -> CommonMethods().ShowUnexpectedErrorScreen(navController)
        unAuthorizedUser -> CommonMethods().ShowUnAuthorizedErrorScreen(navController)
        else -> {
            SelectBankView(
                inProgress = inProgress, isCompleted = isCompleted,
                navController = navController, selectedOption = selectedOption,
                context = context, accountDetailViewModel = accountDetailViewModel,
                fromFlow = fromFlow, purpose = purpose, bankList = bankList,
                onOptionSelected = onOptionSelected,
            )
        }
    }
}

@Composable
fun SelectBankView(
    inProgress: Boolean,
    isCompleted: Boolean,
    navController: NavHostController,
    selectedOption: String?,
    onOptionSelected: (String?) -> Unit,
    context: Context,
    accountDetailViewModel: AccountDetailViewModel,
    fromFlow: String,
    purpose: String,
    bankList: BankList?
) {
    if (inProgress) {
        CenterProgress()
    } else {
        if (isCompleted) {
            FixedTopBottomScreen(
                navController,
                buttonText = stringResource(id = R.string.next),
                modifier = Modifier.background(Color.White),
                backGroudColorChange = selectedOption != null,
                onClick = {
                    onSelectBankClick(
                        selectedOption = selectedOption, navController = navController,
                        fromFlow = fromFlow, purpose = purpose, context = context
                    )
                },
                onBackClick = {
                    navigateToAccountAgreegatorScreen(
                        navController = navController, purpose = purpose, fromFlow = fromFlow
                    )
                }
            ) {
                ScreenTopSection(
                    headerText = stringResource(id = R.string.select_your_bank),
                    subHeaderText = stringResource(id = R.string.choose_your_bank)
                )
                bankList?.let { bankList ->
                    bankList.data?.let { bankItem ->
                        SpaceTextWithRadio(
                            radioOptions = bankItem,
                            selectedOption = selectedOption,
                            onOptionSelected = onOptionSelected
                        )
                    }
                }
            }
        } else {
            accountDetailViewModel.getBankList(context, navController)
        }
    }
}

fun onSelectBankClick(
    selectedOption: String?,
    navController: NavHostController,
    fromFlow: String,
    purpose: String,
    context: Context
) {
    if (selectedOption != null) {
        navigateToSelectAccountAgreegatorScreen(navController, purpose, fromFlow)
    } else {
        CommonMethods().toastMessage(
            context = context, toastMsg = context.getString(R.string.please_select_your_bank)
        )
    }
}

@Composable
fun ScreenTopSection(headerText: String, subHeaderText: String) {
    StartingText(
        text = headerText,
        textColor = appBlueTitle,
        start = 30.dp, end = 30.dp, top = 10.dp, bottom = 5.dp,
        style = normal32Text700
    )
    StartingText(
        text = subHeaderText,
        start = 30.dp, end = 30.dp, bottom = 20.dp,
        style = normal14Text400,
        textAlign = TextAlign.Start,
    )
}


@Preview
@Composable
private fun SelectBankPreview() {
    SelectBankScreen(rememberNavController(),"","Purchase Finance")
}




