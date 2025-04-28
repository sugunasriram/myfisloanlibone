package com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.AgreementAnimation
import com.github.sugunasriram.myfisloanlibone.fis_code.components.FixedTopBottomScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.components.FullWidthRoundShapedCard
import com.github.sugunasriram.myfisloanlibone.fis_code.components.HeaderNextRowValue
import com.github.sugunasriram.myfisloanlibone.fis_code.components.HorizontalDivider
import com.github.sugunasriram.myfisloanlibone.fis_code.components.ImageTextButtonRow
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateApplyByCategoryScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToLoanDetailScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.CustomerLoanList
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.personaLoan.OfferResponseItem
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.personaLoan.UpdateConsentHandlerBody
import com.github.sugunasriram.myfisloanlibone.fis_code.network.sse.SSEViewModel
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlack
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appWhite
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.azureBlueColor
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.greenColour
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal12Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.whiteGreenColor
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.personalLoan.LoanAgreementViewModel
import com.github.sugunasriram.myfisloanlibone.fis_code.views.invalid.LoanNotApprovedScreen
import kotlinx.serialization.json.Json
import java.util.Locale

private val json = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
}
var loanAmt = ""

@SuppressLint("ResourceType")
@Composable
fun LoanSummaryScreen(
    navController: NavHostController,
    id: String,
    consentHandler: String,
    fromFlow: String
) {

    val context = LocalContext.current
    val loanAgreementViewModel: LoanAgreementViewModel = viewModel()

    val loanList by loanAgreementViewModel.loanList.collectAsState()
    val loanListLoading by loanAgreementViewModel.loanListLoading.collectAsState()
    val loanListLoaded by loanAgreementViewModel.loanListLoaded.collectAsState()
    val consentHandling by loanAgreementViewModel.consentHandling.collectAsState()

    val showInternetScreen by loanAgreementViewModel.showInternetScreen.observeAsState(false)
    val showTimeOutScreen by loanAgreementViewModel.showTimeOutScreen.observeAsState(false)
    val showServerIssueScreen by loanAgreementViewModel.showServerIssueScreen.observeAsState(false)
    val unexpectedErrorScreen by loanAgreementViewModel.unexpectedError.observeAsState(false)
    val unAuthorizedUser by loanAgreementViewModel.unAuthorizedUser.observeAsState(false)
    val middleLoan by loanAgreementViewModel.middleLoan.observeAsState(false)
    val errorMessage by loanAgreementViewModel.errorMessage.collectAsState()

    BackHandler {}
    when {
        showInternetScreen -> CommonMethods().ShowInternetErrorScreen(navController)
        showTimeOutScreen -> CommonMethods().ShowTimeOutErrorScreen(navController)
        showServerIssueScreen -> CommonMethods().ShowServerIssueErrorScreen(navController)
        unexpectedErrorScreen -> CommonMethods().ShowUnexpectedErrorScreen(navController)
        unAuthorizedUser -> CommonMethods().ShowUnAuthorizedErrorScreen(navController)
        middleLoan -> CommonMethods().ShowMiddleLoanErrorScreen(navController, errorMessage)
        else -> {
            LoanSummaryDetail(
                loanListLoading = loanListLoading,
                loanListLoaded = loanListLoaded,
                consentHandling = consentHandling,
                navController = navController,
                id = id,
                consentHandler = consentHandler,
                context = context,
                fromFlow = fromFlow,
                loanAgreementViewModel = loanAgreementViewModel,
                loanList = loanList
            )
        }
    }
}

@SuppressLint("ResourceType")
@Composable
fun LoanSummaryDetail(
    loanListLoading: Boolean, loanListLoaded: Boolean, consentHandling: Boolean,fromFlow: String,
    navController: NavHostController, id: String, consentHandler: String, context: Context,
    loanAgreementViewModel: LoanAgreementViewModel, loanList: CustomerLoanList?,
) {
    if (loanListLoading || consentHandling) {
        AgreementAnimation(text = "Processing Please Wait...", image = R.raw.processing_wait)
    } else {
        if (loanListLoaded) {
            if (loanList != null) {
                DashBoardView(
                    navController = navController, context = context, loanList = loanList,
                    fromFlow = fromFlow
                )
            } else {
                LoanNotApprovedScreen(navController)
            }

            //Sugu - Stop listening for SSE
            val sseViewModel: SSEViewModel = viewModel()
            sseViewModel.stopListening()
            //Sugu - End

        } else {
            ConsentHandling(
                loanAgreementViewModel = loanAgreementViewModel, id = id, context = context,
                consentHandler = consentHandler, fromFlow = fromFlow
            )
        }
    }
}

@Composable
fun ConsentHandling(
    consentHandler: String, fromFlow: String, loanAgreementViewModel: LoanAgreementViewModel,
    id: String, context: Context
) {
    if (consentHandler.equals("2")) {
        if (fromFlow.equals("Personal Loan", ignoreCase = true)) {
            loanAgreementViewModel.updateConsentHandler(
                updateConsentHandlerBody = UpdateConsentHandlerBody(
                    subType = "CONSENT_UPDATE", id = id, amount = "", consentStatus = "SUCCESS",
                    loanType = "PERSONAL_LOAN"
                ),
                context = context,
            )
        } else {
            loanAgreementViewModel.updateConsentHandler(
                updateConsentHandlerBody = UpdateConsentHandlerBody(
                    subType = "CONSENT_UPDATE", id = id, amount = "", consentStatus = "SUCCESS",
                    loanType = "INVOICE_BASED_LOAN"
                ),
               context =  context,
            )
        }
    } else {
        if (fromFlow.equals("Personal Loan", ignoreCase = true)) {
            loanAgreementViewModel.getCustomerLoanList("PERSONAL_LOAN", context)
        } else {
            loanAgreementViewModel.getCustomerLoanList("INVOICE_BASED_LOAN", context)
        }
    }
}

@Composable
fun DashBoardView(
    navController: NavHostController, context: Context, loanList: CustomerLoanList,
    fromFlow: String
) {
    var backPressedTime by remember { mutableLongStateOf(0L) }
    FixedTopBottomScreen(
        navController = navController, showBottom = false, isSelfScrollable = false,
        onBackClick = {
            val currentTime = System.currentTimeMillis()
            if (currentTime - backPressedTime < 2000) {
                navigateApplyByCategoryScreen(navController)
            } else {
                CommonMethods().toastMessage(context, "Press back again to exit")
                backPressedTime = currentTime
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            loanList.data?.forEach { offers ->
                LoanDetailCard(navController, offer = offers, fromFlow = fromFlow)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun LoanDetailCard(navController: NavHostController, offer: OfferResponseItem, fromFlow: String) {

    var found: Boolean

    val loanDetail = json.encodeToString(OfferResponseItem.serializer(), offer)
    FullWidthRoundShapedCard(
        onClick = {
            offer.id?.let { orderId ->
                onCardClick(fromFlow, navController, orderId)
            }
        },
        cardColor = azureBlueColor,
        bottomPadding = 0.dp,
        start = 10.dp, end = 10.dp,
    ) {
        FullWidthRoundShapedCard(
            start = 8.dp, end = 8.dp, top = 5.dp, bottom = 5.dp,
            bottomPadding = 0.dp,
            cardColor = whiteGreenColor,
            onClick = {
                offer.id?.let { orderId ->
                    onCardClick(fromFlow, navController, orderId)
                }
            }
        ) {
            // Use a row to display the bank logo alongside the bank name
            DisplayBankDetail(
                offer = offer, navController = navController,
                loanDetail = loanDetail, fromFlow = fromFlow
            )

            //Get loan amount that user will get
            UserLoanAmount(offer = offer)

            DisplayInterestAmount(offer = offer)
            found = false
            DisplayLoanTenureAmount(offer = offer)
        }
    }
}

@Composable
fun UserLoanAmount(offer: OfferResponseItem) {
    offer.quoteBreakUp?.forEach { quoteBreakUp ->
        quoteBreakUp?.let {
            it.title?.let { title ->
                if (title.lowercase(Locale.ROOT).contains("principal")) {
                    it.value?.let { value ->
                        loanAmt = value
                    }
                }

            }
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
}

fun onCardClick(fromFlow: String, navController: NavHostController, orderId: String) {
    navigateToLoanDetailScreen(
        navController = navController, orderId = orderId, fromFlow = fromFlow
    )
}

@Composable
fun DisplayBankDetail(
    offer: OfferResponseItem,
    navController: NavHostController,
    loanDetail: String,
    fromFlow: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, top = 0.dp, bottom = 0.dp)
    ) {
        offer.providerDescriptor?.name?.let { bankName ->
            offer.providerDescriptor.images?.get(0)?.url?.let { imageUrl ->
                val painter = rememberImagePainter(data = imageUrl,
                    builder = {
                        crossfade(true)
                        placeholder(R.drawable.app_logo)
                    }
                )
                ImageTextButtonRow(
                    imagePainter = painter,
                    textHeader = bankName,
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.robotocondensed_bold)),
                        fontWeight = FontWeight(400)
                    ),
                    buttonText = stringResource(id = R.string.details),
                    textColor = appBlack,
                    buttonColor = greenColour.copy(alpha = 0.9f),
                    buttonTextColor = appWhite,
                    buttonTextStyle = normal12Text400,
                    onButtonClick = {
                        offer.id?.let { orderId ->
                            navigateToLoanDetailScreen(
                                navController = navController,
                                orderId = orderId,
                                fromFlow = fromFlow
                            )
                        }
                    }
                )
            }
        }
    }
    HorizontalDivider(start = 0.dp, end = 0.dp)
}

@Composable
fun DisplayLoanTenureAmount(offer: OfferResponseItem) {
    var found: Boolean
    Row {
        offer.itemTags?.forEach { itemTag ->
            itemTag?.tags?.forEach { tag ->
                if (tag.key.equals("loan_term", ignoreCase = true) ||
                    tag.key.equals("term", ignoreCase = true)
                ) {
                    HeaderNextRowValue(
                        textHeader = stringResource(id = R.string.loan_tenure),
                        textValue = tag.value,
                        textColorHeader = appBlack,
                        textColorValue = appBlack,
                        modifier = Modifier
                            .padding(start = 10.dp, top = 0.dp, end = 10.dp, bottom = 8.dp)
                            .weight(0.5f)
                    )
                    found = true
                    return@forEach // Exit the loop after processing the first matching tag
                }
            }
        }
        found = false
        offer.itemTags?.forEach { itemTag ->
            itemTag?.tags?.forEach { tag ->
                if (tag.key.contains("INSTALLMENT_AMOUNT", ignoreCase = true)) {
                    val installmentAmount = tag.value
                    HeaderNextRowValue(
                        textHeader = stringResource(id = R.string.next_emi_due_amount) + " (INR)",
                        textValue = installmentAmount,
                        textColorHeader = appBlack,
                        textColorValue = appBlack,
                        modifier = Modifier
                            .padding(start = 10.dp, top = 0.dp, end = 10.dp, bottom = 8.dp)
                            .weight(0.5f)
                    )
                    found = true
                    return@forEach
                }
                if (found) return@forEach
            }
        }
    }
}

@Composable
fun DisplayInterestAmount(offer: OfferResponseItem) {

    var found: Boolean
    Row {
        HeaderNextRowValue(
            textHeader = stringResource(id = R.string.loan_amount) + " (INR)",
            textValue = loanAmt,
            textColorHeader = appBlack,
            textColorValue = appBlack,
            modifier = Modifier
                .padding(start = 10.dp, top = 0.dp, end = 10.dp, bottom = 8.dp)
                .weight(0.5f)
        )

        found = false
        offer.itemTags?.forEach { itemTag ->
            itemTag?.tags?.forEach { tag ->
                if (tag.key.equals("interest rate", ignoreCase = true) ||
                    tag.key.equals("interest_rate", ignoreCase = true)
                ) {
                    HeaderNextRowValue(
                        textHeader = stringResource(id = R.string.rate_of_interest),
                        textValue = tag.value,
                        textColorHeader = appBlack,
                        textColorValue = appBlack,
                        modifier = Modifier
                            .padding(start = 10.dp, top = 0.dp, end = 10.dp, bottom = 8.dp)
                            .weight(0.5f)
                    )
                    found = true
                    return@forEach // Exit the loop after processing the first matching tag
                }
                if (found) return@forEach
            }
        }
    }
}





