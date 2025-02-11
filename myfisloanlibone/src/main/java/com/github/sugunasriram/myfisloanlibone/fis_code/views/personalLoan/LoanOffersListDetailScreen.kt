package com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.AgreementAnimation
import com.github.sugunasriram.myfisloanlibone.fis_code.components.ClickHeaderValueInARowWithTextBelowValue
import com.github.sugunasriram.myfisloanlibone.fis_code.components.ClickableHeaderValueInARow
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CurvedPrimaryButtonFull
import com.github.sugunasriram.myfisloanlibone.fis_code.components.HeaderNextRowValue
import com.github.sugunasriram.myfisloanlibone.fis_code.components.HeaderValueInARow
import com.github.sugunasriram.myfisloanlibone.fis_code.components.HeaderValueInARowWithTextBelowValue
import com.github.sugunasriram.myfisloanlibone.fis_code.components.ImageHeaderRow
import com.github.sugunasriram.myfisloanlibone.fis_code.components.SignUpText
import com.github.sugunasriram.myfisloanlibone.fis_code.components.SpaceBetweenTextIcon
import com.github.sugunasriram.myfisloanlibone.fis_code.components.StartImageWithText
import com.github.sugunasriram.myfisloanlibone.fis_code.components.StartingText
import com.github.sugunasriram.myfisloanlibone.fis_code.components.TopBar
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateApplyByCategoryScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateSignInPage
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToEditLoanRequestScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToLoanProcessScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.network.core.ApiPaths
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.gst.GstOfferConfirmResponse
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.personaLoan.OfferResponseItem
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.personaLoan.UpdateLoanAmountBody
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.personaLoan.UpdateResponse
import com.github.sugunasriram.myfisloanlibone.fis_code.network.sse.SSEViewModel
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlack
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appGray
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appRed
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appTheme
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.azureBlueColor
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.hintTextColor
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.lightishGray
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal11Text500
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal12Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal14Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal16Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal16Text700
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal20Text500
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal20Text700
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.personalLoan.EditLoanRequestViewModel
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.personalLoan.EditLoanRequestViewModelFactory
import kotlinx.serialization.json.Json
import java.util.Locale

private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }
var coolOffPeriodDateOffer = ""
var loanAmountValue = ""

@SuppressLint("ResourceType")
@Composable
fun LoanOffersListDetailsScreen(
    navController: NavHostController, responseItem: String, id: String, showButtonId: String,
    fromFlow: String
) {
    var backPressedTime by remember { mutableLongStateOf(0L) }

    val editLoanRequestViewModel: EditLoanRequestViewModel = viewModel(
        factory = EditLoanRequestViewModelFactory("amount", "minAmount", "tenure")
    )
    val context: Context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val backGroundColor: Color = Color.White
    val isEdited by editLoanRequestViewModel.isEdited.collectAsState()
    val isEditProcess by editLoanRequestViewModel.isEditProcess.collectAsState()
    val editLoanResponse by editLoanRequestViewModel.editLoanResponse.collectAsState()
    val gstEditLoanResponse by editLoanRequestViewModel.gstOfferConfirmResponse.collectAsState()
    val navigationToSignIn by editLoanRequestViewModel.navigationToSignIn.collectAsState()

    val showInternetScreen by editLoanRequestViewModel.showInternetScreen.observeAsState(false)
    val showTimeOutScreen by editLoanRequestViewModel.showTimeOutScreen.observeAsState(false)
    val showServerIssueScreen by editLoanRequestViewModel.showServerIssueScreen.observeAsState(false)
    val unexpectedErrorScreen by editLoanRequestViewModel.unexpectedError.observeAsState(false)
    val unAuthorizedUser by editLoanRequestViewModel.unAuthorizedUser.observeAsState(false)
    val middleLoan by editLoanRequestViewModel.middleLoan.observeAsState(false)
    val errorMessage by editLoanRequestViewModel.errorMessage.collectAsState()

    val offer = json.decodeFromString(OfferResponseItem.serializer(), responseItem)

    BackHandler {
        if (showButtonId.equals("0")) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - backPressedTime < 2000) {
                navigateApplyByCategoryScreen(navController)
            } else {
                CommonMethods().toastMessage(
                    context = context, toastMsg = "Press back again to go to the Home page"
                )
                backPressedTime = currentTime
            }
        } else {
            navController.popBackStack()
        }
    }

    when {
        navigationToSignIn -> navigateSignInPage (navController)
        showInternetScreen -> CommonMethods().ShowInternetErrorScreen(navController)
        showTimeOutScreen -> CommonMethods().ShowTimeOutErrorScreen(navController)
        showServerIssueScreen -> CommonMethods().ShowServerIssueErrorScreen(navController)
        unexpectedErrorScreen -> CommonMethods().ShowUnexpectedErrorScreen(navController)
        unAuthorizedUser -> CommonMethods().ShowUnAuthorizedErrorScreen(navController)
        middleLoan -> CommonMethods().ShowMiddleLoanErrorScreen(navController, errorMessage)
        else -> {
            LoanOfferListDetailView(
                isEditProcess = isEditProcess, isEdited = isEdited,
                editLoanResponse = editLoanResponse, navController = navController,
                fromFlow = fromFlow, id = id, gstEditLoanResponse = gstEditLoanResponse,
                backGroundColor = backGroundColor, offer = offer, showButtonId = showButtonId,
                editLoanRequestViewModel = editLoanRequestViewModel,
                context = context, focusManager = focusManager
            )
        }
    }
}

@SuppressLint("ResourceType")
@Composable
fun LoanOfferListDetailView(
    isEditProcess: Boolean, isEdited: Boolean, editLoanResponse: UpdateResponse?,
    navController: NavHostController, fromFlow: String, id: String,
    gstEditLoanResponse: GstOfferConfirmResponse?, backGroundColor: Color,
    offer: OfferResponseItem, editLoanRequestViewModel: EditLoanRequestViewModel,
    showButtonId: String, context: Context, focusManager: FocusManager
) {
    var backPressedTime by remember { mutableLongStateOf(0L) }

    if (isEditProcess) {
        AgreementAnimation(text = "", image = R.raw.processing_please_wait)
    } else {
        if (isEdited) {
            SucessNavigation(
                editLoanResponse = editLoanResponse, navController = navController,
                gstEditLoanResponse = gstEditLoanResponse, fromFlow = fromFlow, id = id
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backGroundColor)
                    .verticalScroll(rememberScrollState())
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { focusManager.clearFocus() })
                    }
            ) {
                TopBar(navController, true, onClick = {
                    if (showButtonId.equals("0")) {
                        val currentTime = System.currentTimeMillis()
                        if (currentTime - backPressedTime < 2000) {
                            navigateApplyByCategoryScreen(navController)
                        } else {
                            CommonMethods().toastMessage(
                                context = context,
                                toastMsg = "Press back again to go to the home page"
                            )
                            backPressedTime = currentTime
                        }
                    } else {
                        navController.popBackStack()
                    }
                })
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {

                    LoanOfferListHeaderSection(offer = offer)

                    /* Loan, Interest, DDuration */
                    if (fromFlow.equals("Personal Loan"))
                        LoanCardInfo(offer = offer)
                    else
                        LoanGSTCardInfo(offer = offer )

                    /* Loan Repayment Card */
                    LoanRePaymentCard()

                    // Loan Details Card
                    LoanDetailsCard(offer = offer, context = context)

                    // Loan Summary Card
                    LoanSummaryCard(offer = offer)

                    // Gro Details Card
                    GroCard(offer = offer, context = context)

                    // Lsp Details Card
                    LspCard(offer = offer)
                }
                LoanOfferListBottomSection(
                    showButtonId = showButtonId, offer = offer, id = id,
                    fromFlow = fromFlow, navController = navController,
                    context = context, editLoanRequestViewModel = editLoanRequestViewModel
                )
            }
        }
    }
}

@Composable
fun SucessNavigation(
    editLoanResponse: UpdateResponse?,
    navController: NavHostController,
    gstEditLoanResponse: GstOfferConfirmResponse?,
    fromFlow: String,
    id: String
) {
    if (fromFlow.equals("Personal Loan", ignoreCase = true)) {
        editLoanResponse?.data?.offerResponse?.formUrl?.let { kycUrl ->
            editLoanResponse.data.offerResponse.txnId?.let {transactionId ->
                editLoanResponse.data.id?.let {
                    navigateToLoanProcessScreen(
                        navController = navController, transactionId = transactionId,
                        statusId = 3,
                        responseItem = kycUrl, offerId = id, fromFlow = fromFlow
                    )
                }
            }
        }
    } else {
        val transactionId = gstEditLoanResponse?.data?.offerResponse?.txnID
        gstEditLoanResponse?.data?.offerResponse?.fromURL?.let { kycUrl ->
            transactionId?.let {
                navigateToLoanProcessScreen(
                    navController = navController, transactionId = it, statusId = 13,
                    responseItem =
                    kycUrl,
                    offerId = id, fromFlow = fromFlow
                )
            }
        }
    }
}

@Composable
fun LoanOfferListHeaderSection(offer: OfferResponseItem) {
    offer.providerDescriptor?.images?.get(0)?.url?.let { imageUrl ->
        val painter = rememberImagePainter(data = imageUrl,
            builder = {
                crossfade(true)
                placeholder(R.drawable.app_logo)
            }
        )
        ImageHeaderRow(
            spaceBeforeImage = 25.dp,
            imagePainter = painter,
            textHeader = stringResource(id = R.string.loan_offer_details),
            textStyle = normal20Text700,
            modifier = Modifier
                .size(55.dp, 40.dp)
                .clip(RectangleShape),
            contentScale = ContentScale.Fit,
            textColor = appBlack
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
    /* Lender */
    offer.providerDescriptor?.name?.let { lender ->
        HeaderValueInARow(
            start = 25.dp,
            textHeader = stringResource(id = R.string.lender),
            textValue = lender,
            textColorHeader = hintTextColor,
            textColorValue = appBlack,
            headerStyle = normal14Text400,
            valueStyle = normal14Text400,
            valueTextAlign = TextAlign.Start
        )
    }

    /* KYC */
    HeaderValueInARow(
        start = 25.dp,
        textHeader = stringResource(id = R.string.kyc),
        textValue = stringResource(id = R.string.to_be_done),
        textColorHeader = hintTextColor,
        textColorValue = appBlack,
        headerStyle = normal14Text400,
        valueStyle = normal14Text400,
        valueTextAlign = TextAlign.Start
    )

    Spacer(modifier = Modifier.height(25.dp))

    //Get Cool Off Period
    offer.itemTags?.forEach { itemTags ->
        itemTags?.let {
            it.tags.let { tags ->
                tags.forEach { tag ->
                    if (tag.key.contains("cool_off", ignoreCase = true)) {
                        coolOffPeriodDateOffer = tag.value
                        return@forEach // Exit the loop after processing the first matching tag
                    }
                }
            }
        }
    }
}

@Composable
fun LoanOfferListBottomSection(
    navController: NavHostController, context: Context, id: String, fromFlow: String,
    editLoanRequestViewModel: EditLoanRequestViewModel, showButtonId: String,
    offer: OfferResponseItem
) {
    if (showButtonId.equals("0")) {
        MoveToNextScreen(fromFlow, navController, offer, id)
    } else {
        /* Edit Loan Request */
        EditLoanRequest(context, offer, navController, id, fromFlow)
        BottomSection(
            navController = navController, fromFlow = fromFlow, id = id,
            context = context, offer = offer,
            editLoanRequestViewModel = editLoanRequestViewModel
        )
    }
}

@Composable
fun MoveToNextScreen(
    fromFlow: String,
    navController: NavHostController,
    offer: OfferResponseItem,
    id: String
) {
    CurvedPrimaryButtonFull(
        text = stringResource(id = R.string.next).uppercase(),
        style = normal16Text400,
        modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp, top = 10.dp)
    ) {
        if (fromFlow.equals("Personal Loan")) {
            val formUrl = offer.formUrl
            offer?.txnId?.let { transactionId ->
                formUrl?.let { kycUrl ->
                    navigateToLoanProcessScreen(
                        navController = navController, statusId = 3, responseItem = kycUrl,
                        offerId = id, transactionId = transactionId, fromFlow = fromFlow
                    )
                }
            }
        } else {
            offer.formUrl?.let { kycUrl ->
                offer.txnId?.let { transactionId ->
                    navigateToLoanProcessScreen(
                        navController = navController, statusId = 13, responseItem = kycUrl,
                        offerId = id, fromFlow = fromFlow, transactionId = transactionId
                    )
                }
            }
        }
    }
}


@Composable
fun EditLoanRequest(
    context: Context,
    offer: OfferResponseItem,
    navController: NavHostController,
    id: String,
    fromFlow: String
) {
    var found: Boolean
    SignUpText(
        text = stringResource(id = R.string.edit_loan_request),
        modifier = Modifier.padding(bottom = 5.dp),
        style = normal20Text500,
        textColor = appRed
    ) {

        //Get loan amount that user will get
        offer.quoteBreakUp?.forEach { quoteBreakUp ->
            quoteBreakUp?.let {
                it.title?.let { title ->
                    if (title.toLowerCase(Locale.ROOT).contains("principal")) {
                        it.value?.let { value ->
                            loanAmountValue = value
                        }
                    }
                }
            }
        }

        found = false
        offer.id?.let { offerId ->
            //Sugu - min Loan Amount is whatever Lender sent as MinLoanAmt
            // if its empty, Principal - 10,000
            val minLoanAmount: String = offer.minLoanAmount
                ?.takeIf { it.isNotEmpty() }
                ?: (loanAmountValue.toIntOrNull()?.minus(10000)?.toString() ?: "2000")



            loanAmountValue.let { loanAmountValue ->
                offer.itemTags?.forEach {
                    it?.tags?.forEach { tag ->
                        if (tag.key.equals("LOAN_TERM", ignoreCase = true) ||
                            tag.key.equals("TERM", ignoreCase = true)
                        ) {
                            val loanAmount = if (fromFlow.equals("Personal Loan")) {
                                loanAmountValue
                            } else {
//                                "400000"
                                loanAmountValue?:"400000"
                            }
                            val loanTenure = if (fromFlow.equals("Personal Loan")) {
                                tag.value
                            } else {
                                tag.value?.lowercase()?.replace(" months", "")?:"5"
                            }

                            //If Loan Amount is equal to Min Loan Amount, then show pop up that
                            // this is the maximum loan amount he can get
                            if (loanAmountValue.toDouble() <= minLoanAmount.toDouble()) {
                                CommonMethods().toastMessage(
                                    context = context,
                                    toastMsg = "Offer amount is already at the minimum " +
                                            "amount by the lender"
                                )
                            } else {

                                //Sugu - TODO
                                navigateToEditLoanRequestScreen(
                                    navController = navController, id = id,
                                    minLoanAmount = minLoanAmount,
                                    loanAmount = loanAmount, loanTenure = loanTenure,
                                    offerId = offerId, fromFlow = fromFlow
                                )
                            }
                            found = true
                        }
                        return@forEach
                    }
                    if (found) return@forEach
                }
            }
        }
    }
}

@Composable
fun BottomSection(
    navController: NavHostController,
    fromFlow: String,
    id: String,
    context: Context,
    offer: OfferResponseItem,
    editLoanRequestViewModel: EditLoanRequestViewModel
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 0.dp, bottom = 0.dp, start = 0.dp, end = 0.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        CurvedPrimaryButtonFull(
            text = stringResource(id = R.string.go_back).uppercase(),
            backgroundColor = appRed,
            style = normal16Text400,
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 10.dp)
                .weight(1f)
        ) { navController.popBackStack() }

        Spacer(modifier = Modifier.height(8.dp))
        CurvedPrimaryButtonFull(
            text = stringResource(id = R.string.accept).uppercase(),
            style = normal16Text400,
            modifier = Modifier
                .padding(
                    start = 10.dp, end = 10.dp, bottom = 10.dp, top = 10.dp
                )
                .weight(1f)
        ) {
            onAcceptClick(
                offer = offer, fromFlow = fromFlow,
                editLoanRequestViewModel = editLoanRequestViewModel,
                id = id, context = context
            )
        }
    }
}

fun onAcceptClick(
    offer: OfferResponseItem,
    fromFlow: String,
    editLoanRequestViewModel: EditLoanRequestViewModel,
    id: String,
    context: Context
) {
    var found = false
    offer.id?.let { offerId ->
        loanAmountValue.let { loanAmountValue ->
            offer.itemTags?.forEach {
                it?.tags?.forEach { tag ->
                    if (tag.key.equals("LOAN_TERM", ignoreCase = true) ||
                        tag.key.equals("TERM", ignoreCase = true)
                    ) {
                        if (fromFlow.equals("Personal Loan", ignoreCase = true)) {
                            val updatedLoanTenure = tag.value.lowercase().replace(" months", "")
                            editLoanRequestViewModel.updateLoanAmount(

                                UpdateLoanAmountBody(
                                    requestAmount = loanAmountValue,
                                    requestTerm = updatedLoanTenure,
                                    id = id,
                                    offerId = offerId,
                                    loanType = "PERSONAL_LOAN"
                                ),
                                context = context
                            )
                        } else {
                            offer.id.let { offerId ->
                                editLoanRequestViewModel.gstInitiateOffer(
                                    id = offerId,
                                    loanType = "INVOICE_BASED_LOAN",
                                    context = context
                                )
                            }
                        }
                        found = true
                    }
                    if (found) return@forEach

                }
            }
        }
    }
}

@Composable
fun LoanSummaryCard(offer: OfferResponseItem) {
    if ((offer.quoteBreakUp?.size ?: 0) > 0) {
        StartingText(
            text = stringResource(id = R.string.loan_summary).uppercase(),
            textColor = appBlack,
            top = 20.dp, start = 25.dp, end = 30.dp, bottom = 10.dp,
            style = normal16Text700,
            backGroundColor = appTheme.copy(alpha = 0.1f),
            textAlign = TextAlign.Start,
        )
    }

    offer.quoteBreakUp?.forEach { quoteBreakUp ->
        quoteBreakUp?.let {
            it.title?.let { title ->
                val newTitle = CommonMethods().displayFormattedText(title)
                it.value?.let { discription ->
                    val currency = it.currency?.let { " ($it)" } ?: ""
                    HeaderValueInARowWithTextBelowValue(
                        textHeader = newTitle + currency,
                        textValue = discription,
                        style = normal11Text500,
                        textColorHeader = appGray,
                        textColorValue = appBlack,
                        textBelowValue = "",
                        textBelowStyle = normal12Text400,
                        textColorBelowValue = appGray,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun GroCard(offer: OfferResponseItem, context: Context) {
    var showGroDetails by remember { mutableStateOf(false) }
    SpaceBetweenTextIcon(
        text = stringResource(id = R.string.gro_details).uppercase(),
        textColor = appBlack,
        imageSize = 15.dp,
        backGroundColor = appTheme.copy(alpha = 0.1f),
        top = 20.dp, bottom = 10.dp, textStart = 25.dp,
        image = if (showGroDetails) R.drawable.up_arrow else R.drawable.down_arrow
    )
    { showGroDetails = !showGroDetails }

    if (showGroDetails) {

        val contactInfo = offer.providerTags?.firstOrNull { it?.name == "Contact Info" }?.tags

        contactInfo?.forEach { (key, value) ->
            val newTitle = CommonMethods().displayFormattedText(key)
            if (newTitle.equals("Customer Support Link", ignoreCase = true)) {
                ClickableHeaderValueInARow(
                    start = 25.dp,
                    textHeader = newTitle,
                    textValue = value,
                    textColorHeader = hintTextColor,
                    textColorValue = azureBlueColor,
                    headerStyle = normal14Text400,
                    valueStyle = normal14Text400,
                    valueTextAlign = TextAlign.Start,
                    onClick = { CommonMethods().openLink(context, value) }
                )
            } else {
                HeaderValueInARow(
                    start = 25.dp,
                    textHeader = newTitle,
                    textValue = value,
                    textColorHeader = hintTextColor,
                    textColorValue = appBlack,
                    headerStyle = normal14Text400,
                    valueStyle = normal14Text400,
                    valueTextAlign = TextAlign.Start
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun LoanDetailsCard(offer: OfferResponseItem, context: Context) {
    /* Loan details */
    StartingText(
        text = stringResource(id = R.string.loan_details).uppercase(),
        textColor = appBlack,
        top = 20.dp, start = 25.dp, end = 30.dp, bottom = 10.dp,
        style = normal16Text700,
        backGroundColor = appTheme.copy(alpha = 0.1f),
        textAlign = TextAlign.Start,
    )

    offer.itemTags?.forEach { itemTags ->
        itemTags?.let {
            // Check if display is true
            if (it.display == true) {
                it.tags.let { tags ->
                    tags.forEach { tag ->
                        val newTitle = CommonMethods().displayFormattedText(tag.key)

                        val displayValue = if (tag.key.contains("cool_off", ignoreCase = true) ||
                            tag.key.contains("cool off", ignoreCase = true)
                        ) {

                            convertUTCToLocalDateTime(tag.value)
                        } else if (tag.key == "PROCESSING_FEE" ||
                            tag.key.contains("AMOUNT")
                        ) {
                            tag.value + " INR"
                        } else {
                            tag.value
                        }
                        if (newTitle.equals("Tnc Link", ignoreCase = true)) {
                            ClickHeaderValueInARowWithTextBelowValue(
                                textHeader = newTitle,
                                textValue = displayValue,
                                style = normal11Text500,
                                textColorHeader = appGray,
                                textColorValue = azureBlueColor,
                                textBelowValue = "",
                                textBelowStyle = normal12Text400,
                                textColorBelowValue = appGray,
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { CommonMethods().openLink(context, displayValue) }
                            )

                        } else if (newTitle.equals("kfs Link", ignoreCase = true)) {
                        ClickHeaderValueInARowWithTextBelowValue(
                            textHeader = newTitle,
                            textValue = displayValue,
                            style = normal11Text500,
                            textColorHeader = appGray,
                            textColorValue = azureBlueColor,
                            textBelowValue = "",
                            textBelowStyle = normal12Text400,
                            textColorBelowValue = appGray,
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { CommonMethods().openLink(context, displayValue) }
                        )

                    }else {
                            HeaderValueInARowWithTextBelowValue(
                                textHeader = newTitle,
                                textValue = displayValue,
                                style = normal11Text500,
                                textColorHeader = appGray,
                                textColorValue = appBlack,
                                textBelowValue = "",
                                textBelowStyle = normal12Text400,
                                textColorBelowValue = appGray,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(30.dp))
}

@Composable
fun LoanRePaymentCard() {
    /* Repayment */
    StartingText(
        text = stringResource(id = R.string.repayment).uppercase(),
        textColor = appBlack,
        top = 20.dp, start = 25.dp, end = 30.dp, bottom = 10.dp,
        style = normal16Text700,
        backGroundColor = appTheme.copy(alpha = 0.1f),
        textAlign = TextAlign.Start,
    )

    StartImageWithText(
        text = stringResource(
            id = R.string
                .you_may_reduce_interest_by_repaying_before_the_due_date
        ),
        start = 25.dp
    )
    Spacer(modifier = Modifier.padding(top = 10.dp))

    StartImageWithText(
        text = stringResource(
            id = R.string
                .late_repayment_will_lead_to_penalty
        ),
        start = 25.dp, bottom = 8.dp, top = 8.dp
    )

}

@Composable
fun LoanCardInfo(offer: OfferResponseItem) {
    //Get loan amount that user will get
    offer.quoteBreakUp?.forEach { quoteBreakUp ->
        quoteBreakUp?.let {
            it.title?.let { title ->
                if (title.lowercase(Locale.ROOT).contains("principal")) {
                    it.value?.let { value ->
                        loanAmountValue = value
                    }
                }
            }
        }
    }
    LoanAmountInterest(offer)
}

@Composable
fun LoanGSTCardInfo(offer: OfferResponseItem) {
    //Get loan amount that user will get
    var found = false
    offer.itemTags?.forEach { itemTags ->
        itemTags?.let {
            itemTags.name?.let { title ->
                found = false
                itemTags?.tags?.forEach { itemTagsItem ->
                    if (itemTagsItem.key.lowercase(Locale.ROOT).contains("principal") && !found) {
                        itemTagsItem.value.let { value ->
                        loanAmountValue = value
                            found = true
                    }
                }
            }
        }
        }
    }
    LoanAmountInterest(offer)
}
@Composable
fun LoanAmountInterest(offer: OfferResponseItem) {
    var found: Boolean
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(lightishGray)
    ) {
        /* Loan */
        offer.itemPrice?.value?.let { loanAmount ->
            val currency = offer.itemPrice.currency?.let { " ($it)" } ?: ""

            HeaderNextRowValue(
                textHeader = stringResource(id = R.string.loan) + currency,
                textValue = loanAmountValue,
                textColorHeader = appBlack,
                textColorValue = appBlack,
                modifier = Modifier
                    .padding(start = 17.dp, top = 0.dp, end = 10.dp, bottom = 8.dp)
                    .weight(0.5f)

            )
        }
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
                            .padding(start = 25.dp, top = 0.dp, end = 10.dp, bottom = 8.dp)
                            .weight(0.5f)
                    )
                    found = true
                    return@forEach
                }
                if (found) return@forEach
            }
        }
        found = false
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
                if (found) return@forEach
            }
        }

    }
}

@Composable
fun LspCard(offer: OfferResponseItem) {
    var showGroDetails by remember { mutableStateOf(false) }
    SpaceBetweenTextIcon(
        text = stringResource(id = R.string.contact_details).uppercase(),
        textColor = appBlack,
        imageSize = 15.dp,
        backGroundColor = appTheme.copy(alpha = 0.1f),
        top = 20.dp, bottom = 10.dp, textStart = 25.dp,
        image = if (showGroDetails) R.drawable.up_arrow else R.drawable.down_arrow
    ) { showGroDetails = !showGroDetails }

    if (showGroDetails) {

        val lspContactInfo = offer.providerTags?.firstOrNull { it?.name == "Lsp Info" }?.tags

        lspContactInfo?.forEach { (key, value) ->
            val newTitle = CommonMethods().displayFormattedText(key)

            HeaderValueInARow(
                start = 25.dp,
                textHeader = newTitle,
                textValue = value,
                textColorHeader = hintTextColor,
                textColorValue = appBlack,
                headerStyle = normal14Text400,
                valueStyle = normal14Text400,
                valueTextAlign = TextAlign.Start
            )
        }
    }
    Spacer(modifier = Modifier.height(30.dp))
}

@Preview
@Composable
fun LoanOffersListDetailsScreenPreview() {
    LoanOffersListDetailsScreen(
        navController = rememberNavController(), id = "1111",
        responseItem = "{\n" +
                "    \"_id\": \"17ae0bcf-b123-5854-914a-7804e8dc68e9\",\n" +
                "    \"form_id\": \"439a7972-6b04-4b81-a486-56b7be3b95df\",\n" +
                "    \"item_descriptor\": {\n" +
                "        \"code\": \"PERSONAL_LOAN\",\n" +
                "        \"name\": \"Personal Loan\"\n" +
                "    },\n" +
                "    \"item_tags\": [\n" +
                "        {\n" +
                "            \"display\": true,\n" +
                "            \"name\": \"Loan Information\",\n" +
                "            \"tags\": {\n" +
                "                \"INTEREST_RATE\": \"17.00%\",\n" +
                "                \"TERM\": \"24 Months\",\n" +
                "                \"INTEREST_RATE_TYPE\": \"FIXED\",\n" +
                "                \"APPLICATION_FEE\": \"0.00 INR\",\n" +
                "                \"FORECLOSURE_FEE\": \"4.00% + GST\",\n" +
                "                \"INTEREST_RATE_CONVERSION_CHARGE\": \"0\",\n" +
                "                \"DELAY_PENALTY_FEE\": \"3.00% + GST\",\n" +
                "                \"OTHER_PENALTY_FEE\": \"0\",\n" +
                "                \"TNC_LINK\": \"https://www.dmifinance.in/pdf/Loan-Application-Undertaking.pdf\",\n" +
                "                \"ANNUAL_PERCENTAGE_RATE\": \"18.22%\",\n" +
                "                \"REPAYMENT_FREQUENCY\": \"MONTHLY\",\n" +
                "                \"NUMBER_OF_INSTALLMENTS_OF_REPAYMENT\": \"24\",\n" +
                "                \"COOL_OFF_PERIOD\": \"2024-08-12T10:16:32.043Z\",\n" +
                "                \"INSTALLMENT_AMOUNT\": \"14832.68 INR\"\n" +
                "            }\n" +
                "        }\n" +
                "    ],\n" +
                "    \"provider_descriptor\": {\n" +
                "        \"images\": [\n" +
                "            {\n" +
                "                \"size_type\": \"sm\",\n" +
                "                \"url\": \"https://refo-static-public.s3.ap-south-1.amazonaws.com/dmi/dmi-sm.png\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"size_type\": \"md\",\n" +
                "                \"url\": \"https://refo-static-public.s3.ap-south-1.amazonaws.com/dmi/dmi-md.png\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"size_type\": \"lg\",\n" +
                "                \"url\": \"https://refo-static-public.s3.ap-south-1.amazonaws.com/dmi/dmi-lg.png\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"name\": \"DMI FINANCE PRIVATE LIMITED\",\n" +
                "        \"short_desc\": \"DMI FINANCE PRIVATE LIMITED\",\n" +
                "        \"long_desc\": \"DMI FINANCE PRIVATE LIMITED\"\n" +
                "    },\n" +
                "    \"bpp_id\": \"dmi-ondcpreprod.refo.dev\",\n" +
                "    \"msg_id\": \"78317181-75d5-5df2-9493-326e3ed1ecbe\",\n" +
                "    \"quote_id\": \"24570402-69d6-400e-b7a3-236667ece756\",\n" +
                "    \"provider_tags\": [\n" +
                "        {\n" +
                "            \"name\": \"Contact Info\",\n" +
                "            \"tags\": {\n" +
                "                \"GRO_NAME\": \"Ashish Sarin\",\n" +
                "                \"GRO_EMAIL\": \"head.services@dmifinance.in/grievance@dmifinance.in\",\n" +
                "                \"GRO_CONTACT_NUMBER\": \"011-41204444\",\n" +
                "                \"GRO_DESIGNATION\": \"Senior Vice President - Customer Success\",\n" +
                "                \"GRO_ADDRESS\": \"Express Building, 3rd Floor, 9-10, Bahadur Shah Zafar Marg, New Delhi-110002\",\n" +
                "                \"CUSTOMER_SUPPORT_LINK\": \"https://portal.dmifinance.in\",\n" +
                "                \"CUSTOMER_SUPPORT_CONTACT_NUMBER\": \"9350657100\",\n" +
                "                \"CUSTOMER_SUPPORT_EMAIL\": \"customercare@dmifinance.in\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Lsp Info\",\n" +
                "            \"tags\": {\n" +
                "                \"LSP_NAME\": \"DMI Finance Pvt. Ltd\",\n" +
                "                \"LSP_EMAIL\": \"customercare@dmifinance.in\",\n" +
                "                \"LSP_CONTACT_NUMBER\": \"9350657100\",\n" +
                "                \"LSP_ADDRESS\": \"Express Building, 3rd Floor, 9-10, Bahadur Shah Zafar Marg New Delhi-110002\"\n" +
                "            }\n" +
                "        }\n" +
                "    ],\n" +
                "    \"quote_price\": {\n" +
                "        \"currency\": \"INR\",\n" +
                "        \"value\": \"359524.32\"\n" +
                "    },\n" +
                "    \"item_id\": \"d9eb81e2-96b5-477f-98dc-8518ad60d72e\",\n" +
                "    \"bpp_uri\": \"https://dmi-ondcpreprod.refo.dev/app/ondc/seller\",\n" +
                "    \"from_url\": \"https://dmi-ondcpreprod.refo.dev/loans/lvform/439a7972-6b04-4b81-a486-56b7be3b95df\",\n" +
                "    \"provider_id\": \"101\",\n" +
                "    \"item_price\": {\n" +
                "        \"currency\": \"INR\",\n" +
                "        \"value\": \"359524.32\"\n" +
                "    },\n" +
                "    \"quote_breakup\": [\n" +
                "        {\n" +
                "            \"currency\": \"INR\",\n" +
                "            \"title\": \"PRINCIPAL\",\n" +
                "            \"value\": \"300000.00\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"currency\": \"INR\",\n" +
                "            \"title\": \"INTEREST\",\n" +
                "            \"value\": \"55984.32\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"currency\": \"INR\",\n" +
                "            \"title\": \"NET_DISBURSED_AMOUNT\",\n" +
                "            \"value\": \"296460.00\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"currency\": \"INR\",\n" +
                "            \"title\": \"OTHER_UPFRONT_CHARGES\",\n" +
                "            \"value\": \"0.00\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"currency\": \"INR\",\n" +
                "            \"title\": \"INSURANCE_CHARGES\",\n" +
                "            \"value\": \"0.00\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"currency\": \"INR\",\n" +
                "            \"title\": \"OTHER_CHARGES\",\n" +
                "            \"value\": \"0.00\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"currency\": \"INR\",\n" +
                "            \"title\": \"PROCESSING_FEE\",\n" +
                "            \"value\": \"3540.00\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"txn_id\": \"02d1272c-39a6-59ae-91c1-51433cba45b7\",\n" +
                "    \"user_id\": \"5a9ba55d-bb12-5cb6-b320-e6d3d0fccedf\",\n" +
                "    \"doc_id\": \"cfc85dcd-9f59-51e4-9f99-5dd688ee20fe\"\n" +
                "}",
        showButtonId = "1",
        fromFlow = "Personal"
    )
}




