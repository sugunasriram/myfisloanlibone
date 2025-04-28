package com.github.sugunasriram.myfisloanlibone.fis_code.views.sidemenu

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CenterProgress
import com.github.sugunasriram.myfisloanlibone.fis_code.components.FullWidthRoundShapedCard
import com.github.sugunasriram.myfisloanlibone.fis_code.components.InnerScreenWithHamburger
import com.github.sugunasriram.myfisloanlibone.fis_code.components.TextHyphenValueInARow
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateApplyByCategoryScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToLoanDetailScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.CustomerLoanList
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.personaLoan.OfferResponseItem
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.azureBlueColor
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.deepGreenColor
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.negativeGray
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal20Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.whiteGreenColor
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.personalLoan.LoanAgreementViewModel
import com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan.loanAmountValue
import java.util.Locale

@Composable
fun LoanStatusScreen(navController: NavHostController) {
    val tabs = listOf("Active Loans", "Inactive Loans")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val context = LocalContext.current
    val loanAgreementViewModel: LoanAgreementViewModel = viewModel()
    val loanListLoading by loanAgreementViewModel.loanListLoading.collectAsState()
    val loanListLoaded by loanAgreementViewModel.loanListLoaded.collectAsState()
    val loanList by loanAgreementViewModel.loanList.collectAsState()

    val showInternetScreen by loanAgreementViewModel.showInternetScreen.observeAsState(false)
    val showTimeOutScreen by loanAgreementViewModel.showTimeOutScreen.observeAsState(false)
    val showServerIssueScreen by loanAgreementViewModel.showServerIssueScreen.observeAsState(false)
    val unexpectedErrorScreen by loanAgreementViewModel.unexpectedError.observeAsState(false)
    val unAuthorizedUser by loanAgreementViewModel.unAuthorizedUser.observeAsState(false)
    val middleLoan by loanAgreementViewModel.middleLoan.observeAsState(false)
    val errorMessage by loanAgreementViewModel.errorMessage.collectAsState()

    BackHandler {
        navigateApplyByCategoryScreen(navController)
    }

    InnerScreenWithHamburger(isSelfScrollable = true, navController = navController) {
        Column {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier
                    .padding(top = 30.dp, start = 15.dp, end = 15.dp)
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(color = Color(0xFF8C8C8C), shape = RoundedCornerShape(5.dp)),
                backgroundColor = Color(0xFF8C8C8C),
                contentColor = Color.Black,
                indicator = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    val isSelected = selectedTabIndex == index
                    Tab(
                        selected = isSelected,
                        onClick = { selectedTabIndex = index },
                        modifier = Modifier
                            .background(
                                color = if (isSelected) Color(0xFF305084) else Color(0xFF8C8C8C),
                                shape = RoundedCornerShape(5.dp)
                            )
                            .padding(8.dp)
                    ) {
                        Text(
                            text = title,
                            color = if (isSelected) Color.White else Color.Black,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
            when (selectedTabIndex) {
                0 -> ActiveLoanScreen(
                    navController = navController, showInternetScreen = showInternetScreen,
                    showTimeOutScreen = showTimeOutScreen, middleLoan = middleLoan,
                    showServerIssueScreen = showServerIssueScreen, context = context,
                    unexpectedErrorScreen = unexpectedErrorScreen, loanList = loanList,
                    unAuthorizedUser = unAuthorizedUser, loanListLoading = loanListLoading,
                    loanListLoaded = loanListLoaded, errorMessage = errorMessage,
                    loanAgreementViewModel = loanAgreementViewModel, showActiveLoanScreen = true
                )

                1 -> ActiveLoanScreen(
                    navController = navController, showInternetScreen = showInternetScreen,
                    showTimeOutScreen = showTimeOutScreen, middleLoan = middleLoan,
                    showServerIssueScreen = showServerIssueScreen, context = context,
                    unexpectedErrorScreen = unexpectedErrorScreen, loanList = loanList,
                    unAuthorizedUser = unAuthorizedUser, loanListLoading = loanListLoading,
                    loanListLoaded = loanListLoaded, errorMessage = errorMessage,
                    loanAgreementViewModel = loanAgreementViewModel, showActiveLoanScreen = false
                )
            }
        }
    }
}

@Composable
fun ActiveLoanScreen(
    navController: NavHostController, showInternetScreen: Boolean, showTimeOutScreen: Boolean,
    middleLoan: Boolean, showServerIssueScreen: Boolean, unexpectedErrorScreen: Boolean,
    unAuthorizedUser: Boolean, loanListLoading: Boolean, loanListLoaded: Boolean,
    errorMessage: String, loanAgreementViewModel: LoanAgreementViewModel, context: Context,
    loanList: CustomerLoanList?, showActiveLoanScreen: Boolean
) {

    when {
        showInternetScreen -> CommonMethods().ShowInternetErrorScreen(navController)
        showTimeOutScreen -> CommonMethods().ShowTimeOutErrorScreen(navController)
        showServerIssueScreen -> CommonMethods().ShowServerIssueErrorScreen(navController)
        unexpectedErrorScreen -> CommonMethods().ShowUnexpectedErrorScreen(navController)
        unAuthorizedUser -> CommonMethods().ShowUnAuthorizedErrorScreen(navController)
        middleLoan -> CommonMethods().ShowNoResponseFormLendersScreen(navController)
        else -> {
            ActiveLoanScreenView(
                loanListLoading = loanListLoading, loanListLoaded = loanListLoaded,
                loanAgreementViewModel = loanAgreementViewModel, context = context,
                loanList = loanList, navController = navController,
                showActiveLoanScreen = showActiveLoanScreen
            )
        }
    }
}

@Composable
fun ActiveLoanScreenView(
    loanListLoading: Boolean, loanListLoaded: Boolean,
    loanAgreementViewModel: LoanAgreementViewModel, context: Context,
    loanList: CustomerLoanList?, navController: NavHostController, showActiveLoanScreen: Boolean
) {
    if (loanListLoading) {
        CenterProgress()
    } else {
        if (loanListLoaded) {
            ShowLoans(
                loanList = loanList, navController = navController,
                showActiveLoanScreen = showActiveLoanScreen
            )
        } else {
            loanAgreementViewModel.completeLoanList(context)
        }
    }
}

@Composable
fun ShowLoans(
    loanList: CustomerLoanList?, navController: NavHostController, showActiveLoanScreen: Boolean
) {
    loanList?.data?.let {
        if(it.isEmpty()){
            EmptyLoanStatusScreen()
        }else{
            loanList?.data?.forEach { data ->
                data.fulfillments?.forEach { fulfilment ->
                    fulfilment?.state?.let { state ->
                        state.descriptor?.name?.let { loanStatus ->
                            val statusColor =
                                if (loanStatus.equals("Loan Disbursed", ignoreCase = true)) {
                                    deepGreenColor
                                } else if (loanStatus.equals("closed", ignoreCase = true)) {
                                    Color.Red
                                } else {
                                    azureBlueColor
                                }
                            if (loanStatus.contains("closed", ignoreCase = true) && !showActiveLoanScreen) {
                                DisplayLoanStatusCard(
                                    data = data, navController = navController,
                                    statusColor = statusColor, loanStatus = loanStatus
                                )
                            } else if (showActiveLoanScreen) {
                                DisplayLoanStatusCard(
                                    data = data, navController = navController,
                                    statusColor = statusColor, loanStatus = loanStatus
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
private fun EmptyLoanStatusScreen() {
    Column(modifier = Modifier.fillMaxSize().offset(y = (-30).dp), verticalArrangement = Arrangement.Center) {

        Image(
            painter = painterResource(id = R.drawable.loan_status),
            contentDescription = "loan Status",
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxWidth().size(200.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = stringResource(R.string.no_existing_loans),
            textAlign = TextAlign.Center, fontSize = 32.sp, color = negativeGray,
            fontFamily = FontFamily(Font(R.font.robotocondensed_regular)),
            fontWeight = FontWeight(800),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun DisplayLoanStatusCard(
    data: OfferResponseItem, navController: NavHostController, statusColor: Color,
    loanStatus: String
) {
    data.itemDescriptor?.let { itemDescriptor ->
        itemDescriptor.name?.let { loanType ->
            FullWidthRoundShapedCard(
                onClick = {
                    data.id?.let { orderId ->
                        navigateToLoanDetailScreen(
                            navController = navController, orderId = orderId, fromFlow = loanType
                        )
                    }
                },
                cardColor = statusColor,
                bottomPadding = 0.dp,
                modifier = Modifier.alpha(0.5f)
            ) {
                InnerCardView(
                    data = data, navController = navController, loanType = loanType,
                    loanStatus = loanStatus, statusColor = statusColor
                )
            }
        }
    }
}

@Composable
fun InnerCardView(
    data: OfferResponseItem, navController: NavHostController, loanType: String, loanStatus: String,
    statusColor: Color
) {
    FullWidthRoundShapedCard(
        start = 8.dp, end = 8.dp, top = 5.dp, bottom = 5.dp, cardColor = whiteGreenColor,
        onClick = {
            data.id?.let { orderId ->
                navigateToLoanDetailScreen(
                    navController = navController, orderId = orderId, fromFlow = loanType
                )
            }
        }
    ) {
        TextHyphenValueInARow(
            textHeader = stringResource(id = R.string.loan_type),
            textValue = loanType,
            style = normal20Text400
        )
        data.providerDescriptor?.name?.let { lenderName ->
            TextHyphenValueInARow(
                textHeader = stringResource(id = R.string.lender),
                textValue = lenderName,
                style = normal20Text400
            )
        }
        //Sugu - TODO
        data.quoteBreakUp?.forEach { quoteBreakUp ->
            quoteBreakUp?.let {
                it.title?.let { title ->
//                    if (title.toLowerCase(Locale.ROOT).contains("principal")) {
                    if (title.toLowerCase(Locale.ROOT).equals("principal")) {
                        it.value?.let { value ->
                            loanAmountValue = value
                            TextHyphenValueInARow(
                                textHeader = stringResource(id = R.string.loan_amount),
                                textValue = value,
                                style = normal20Text400,
                            )
                        }
                    }
                }
            }
        }


        data.itemPrice?.value?.let { totalPayAmount ->
            TextHyphenValueInARow(
                textHeader = stringResource(id = R.string.total_payable_amount),
                textValue = totalPayAmount,
                style = normal20Text400,
            )
        }
        TextHyphenValueInARow(
            textHeader = stringResource(id = R.string.status),
            textValue = loanStatus,
            style = normal20Text400,
            textColorValue = statusColor
        )
    }
}


@Preview
@Composable
private fun LoanStatusPreview() {
//    LoanStatusScreen(rememberNavController())
    Surface {
        EmptyLoanStatusScreen()
    }

}


