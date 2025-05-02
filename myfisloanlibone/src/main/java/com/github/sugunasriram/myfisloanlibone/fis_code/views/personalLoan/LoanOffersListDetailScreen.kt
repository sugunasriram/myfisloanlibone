package com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asComposePaint
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
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
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToLoanOffersListDetailScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToLoanProcessScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.gst.GstOfferConfirmResponse
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.personaLoan.OfferResponseItem
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.personaLoan.UpdateLoanAmountBody
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.personaLoan.UpdateResponse
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.pf.PfOfferConfirmResponse
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.pf.PfOfferResponseItem
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
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlue
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.personalLoan.EditLoanRequestViewModel
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.personalLoan.EditLoanRequestViewModelFactory
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.util.Locale

private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }
var coolOffPeriodDateOffer = ""
var loanAmountValue = ""

@OptIn(ExperimentalMaterialApi::class)
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
    val isPfEdited by editLoanRequestViewModel.isPfEdited.collectAsState()
    val isEditProcess by editLoanRequestViewModel.isEditProcess.collectAsState()
    val editLoanResponse by editLoanRequestViewModel.editLoanResponse.collectAsState()
    val gstEditLoanResponse by editLoanRequestViewModel.gstOfferConfirmResponse.collectAsState()
    val pfOfferConfirmResponse by editLoanRequestViewModel.pfOfferConfirmResponse.collectAsState()
    val navigationToSignIn by editLoanRequestViewModel.navigationToSignIn.collectAsState()

    val showInternetScreen by editLoanRequestViewModel.showInternetScreen.observeAsState(false)
    val showTimeOutScreen by editLoanRequestViewModel.showTimeOutScreen.observeAsState(false)
    val showServerIssueScreen by editLoanRequestViewModel.showServerIssueScreen.observeAsState(false)
    val unexpectedErrorScreen by editLoanRequestViewModel.unexpectedError.observeAsState(false)
    val unAuthorizedUser by editLoanRequestViewModel.unAuthorizedUser.observeAsState(false)
    val middleLoan by editLoanRequestViewModel.middleLoan.observeAsState(false)
    val errorMessage by editLoanRequestViewModel.errorMessage.collectAsState()

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )


    val offer = json.decodeFromString(OfferResponseItem.serializer(), responseItem)
    val pfOffer = json.decodeFromString(PfOfferResponseItem.serializer(), responseItem)

    var maxAmount by remember { mutableFloatStateOf(442300.00f) }
    var minAmount by remember { mutableFloatStateOf(40000.00f) }
    var interest by remember { mutableStateOf("12 %") }
    var loanTenure by remember { mutableStateOf("5") }


    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            BottomSheetContent(
                maxAmount = maxAmount,
                minAmount = minAmount,
                interest = interest,
                onClose = {
                    scope.launch {
                        sheetState.hide()
                    }
                },
                onSubmit = { downPaymentAmount ->

                    scope.launch {
                        sheetState.hide()
                    }

                    checkAndMakeApiCall(
                        maxAmount - downPaymentAmount,
                        context,
                        editLoanRequestViewModel,
                        loanTenure,
                        id,
                    )
                }
            )
        },
        sheetShape = RoundedCornerShape(
            topStart = 24.dp,
            topEnd = 24.dp
        ),
        scrimColor = Color.Black.copy(alpha = 0.32f),
        modifier = Modifier.fillMaxSize()
    ) {

        BackHandler {
            if (showButtonId == "0") {
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
            navigationToSignIn -> navigateSignInPage(navController)
            showInternetScreen -> CommonMethods().ShowInternetErrorScreen(navController)
            showTimeOutScreen -> CommonMethods().ShowTimeOutErrorScreen(navController)
            showServerIssueScreen -> CommonMethods().ShowServerIssueErrorScreen(navController)
            unexpectedErrorScreen -> CommonMethods().ShowUnexpectedErrorScreen(navController)
            unAuthorizedUser -> CommonMethods().ShowUnAuthorizedErrorScreen(navController)
            middleLoan -> CommonMethods().ShowNoResponseFormLendersScreen(navController)
            else -> {
                LoanOfferListDetailView(
                    isEditProcess = isEditProcess,
                    isEdited = isEdited,
                    editLoanResponse = editLoanResponse,
                    navController = navController,
                    fromFlow = fromFlow,
                    id = id,
                    gstEditLoanResponse = gstEditLoanResponse,
                    pfOfferConfirmResponse = pfOfferConfirmResponse,
                    pfOffer = pfOffer,
                    backGroundColor = backGroundColor,
                    offer = offer,
                    showButtonId = showButtonId,
                    editLoanRequestViewModel = editLoanRequestViewModel,
                    context = context,
                    focusManager = focusManager,
                    openSheet = { max, min, int, tenure ->
                        scope.launch {
                            maxAmount = CommonMethods().roundToNearestHundred(max.toFloat())
                            minAmount = min.toFloat()
                            interest = int
                            loanTenure = tenure
                            sheetState.show()
                        }
                    }
                )
            }
        }

        if (isPfEdited) {
            pfOffer.apply {
                formUrl = pfOfferConfirmResponse?.data?.offerResponse?.fromURL
            }
            val pfOfferString = json.encodeToString(
                PfOfferResponseItem.serializer(), pfOffer
            )
            navigateToLoanOffersListDetailScreen(
                navController = navController,
                responseItem = pfOfferString, id = id,
                showButtonId = "0", fromFlow = fromFlow
            )

        }
    }
}


private fun checkAndMakeApiCall(
    downPaymentAmount: Float,
    context: Context,
    editLoanRequestViewModel: EditLoanRequestViewModel,
    loanTenure: String,
    id: String,
) {
    if (downPaymentAmount == 0.0f) {
        Toast.makeText(
            context,
            context.getString(R.string.loan_amount_has_to_be_greater_than_0),
            Toast.LENGTH_LONG
        ).show()
    } else {
        id.let { offerId ->
            editLoanRequestViewModel.updatePfApiFlow(PfFlow.Edited.status)
            editLoanRequestViewModel.pfInitiateOffer(
                id = offerId,
                loanType = "PURCHASE_FINANCE",
                context = context,
                paymentAmount = downPaymentAmount.toString(),
                loanTenure
            )
        }
    }
}

sealed class PfFlow(val status: String) {
    object Normal : PfFlow("Normal")
    object Edited : PfFlow("Edited")
}


@SuppressLint("ResourceType")
@Composable
fun LoanOfferListDetailView(
    isEditProcess: Boolean,
    isEdited: Boolean,
    editLoanResponse: UpdateResponse?,
    navController: NavHostController,
    fromFlow: String,
    id: String,
    gstEditLoanResponse: GstOfferConfirmResponse?,
    pfOfferConfirmResponse: PfOfferConfirmResponse?,
    pfOffer: PfOfferResponseItem,
    backGroundColor: Color,
    offer: OfferResponseItem,
    editLoanRequestViewModel: EditLoanRequestViewModel,
    showButtonId: String,
    context: Context,
    focusManager: FocusManager,
    openSheet: (String, String, String, String) -> Unit
) {
    var backPressedTime by remember { mutableLongStateOf(0L) }

    if (isEditProcess) {
        AgreementAnimation(text = "", image = R.raw.processing_please_wait)
    } else {
        if (isEdited) {
            SucessNavigation(
                editLoanResponse = editLoanResponse,
                navController = navController,
                gstEditLoanResponse = gstEditLoanResponse,
                pfOfferConfirmResponse = pfOfferConfirmResponse,
                fromFlow = fromFlow,
                id = id
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
                    if (showButtonId == "0") {
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
                    when (fromFlow) {
                        "Personal Loan" -> LoanCardInfo(offer = offer)
//                        "Purchase Finance" -> LoanPfCardInfo(offer = pfOffer)
                        else -> LoanGSTCardInfo(offer = offer)
                    }

                    /* Loan Repayment Card */
                    LoanRePaymentCard()

                    // Loan Details Card
                    LoanDetailsCard(offer = offer, context = context, fromFlow = fromFlow)

                    // Loan Summary Card
                    LoanSummaryCard(offer = offer)

                    // Gro Details Card
                    GroCard(offer = offer, context = context)

                    // Lsp Details Card
                    LspCard(offer = offer)
                }
                LoanOfferListBottomSection(
                    showButtonId = showButtonId,
                    offer = offer,
                    id = id,
                    fromFlow = fromFlow,
                    navController = navController,
                    context = context,
                    editLoanRequestViewModel = editLoanRequestViewModel,
                    pfOffer = pfOffer,
                    openSheet = { maxAmount, minAmount, interest, loanTenure ->
                        openSheet(
                            maxAmount,
                            minAmount,
                            interest,
                            loanTenure
                        )
                    }
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
    pfOfferConfirmResponse: PfOfferConfirmResponse?,
    fromFlow: String,
    id: String
) {
    if (fromFlow.equals("Personal Loan", ignoreCase = true)) {
        editLoanResponse?.data?.offerResponse?.formUrl?.let { kycUrl ->
            editLoanResponse.data.offerResponse.txnId?.let { transactionId ->
                editLoanResponse.data.id?.let {
                    navigateToLoanProcessScreen(
                        navController = navController, transactionId = transactionId,
                        statusId = 3,
                        responseItem = kycUrl, offerId = id, fromFlow = fromFlow
                    )
                }
            }
        }
    } else if (fromFlow.equals("Purchase Finance", ignoreCase = true)) {
        val transactionId = pfOfferConfirmResponse?.data?.offerResponse?.txnID
        pfOfferConfirmResponse?.data?.offerResponse?.fromURL?.let { kycUrl ->
            transactionId?.let {
                navigateToLoanProcessScreen(
                    navController = navController, transactionId = it, statusId = 21,
                    responseItem =
                    kycUrl,
                    offerId = id, fromFlow = fromFlow
                )
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
        val painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current).data(data = imageUrl)
                .apply(block = fun ImageRequest.Builder.() {
                    crossfade(true)
                    placeholder(R.drawable.app_logo)
                }).build()
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
    offer.itemTags?.forEach itemTag@{ itemTags ->
        itemTags?.let {
            it.tags.let { tags ->
                tags.forEach { tag ->
                    if (tag.key.contains("cool_off", ignoreCase = true)) {
                        coolOffPeriodDateOffer = tag.value?:""
                        return@itemTag
                    }
                }
            }
        }
    }
}


@Composable
fun LoanOfferListBottomSection(
    navController: NavHostController,
    context: Context,
    id: String,
    fromFlow: String,
    editLoanRequestViewModel: EditLoanRequestViewModel,
    showButtonId: String,
    offer: OfferResponseItem,
    pfOffer: PfOfferResponseItem,
    openSheet: (String, String, String, String) -> Unit
) {
    if (showButtonId == "0") {
        MoveToNextScreen(fromFlow, navController, offer, id)
    } else {
        /* Edit Loan Request */
        if (fromFlow == "Purchase Finance") {
            EditPfLoanRequest(
                pfOffer,
                openSheet = { maxAmount, minAmount, interest, loanTenure ->
                    openSheet(
                        maxAmount,
                        minAmount,
                        interest,
                        loanTenure
                    )
                })
        } else {
            EditLoanRequest(context,offer, navController, id, fromFlow)
        }
        BottomSection(
            navController = navController, fromFlow = fromFlow, id = id,
            context = context, offer = offer,
            editLoanRequestViewModel = editLoanRequestViewModel
        )
    }
}

@Composable
fun EditPfLoanRequest(
    offer: PfOfferResponseItem,
    openSheet: (String, String, String, String) -> Unit
) {
    SignUpText(
        text = stringResource(id = R.string.edit_down_payment),
        modifier = Modifier.padding(bottom = 5.dp),
        style = normal20Text500,
        textColor = appRed
    ) {

        //Get loanAmount
        offer.itemTags?.forEach itemTags@{ itemTag ->
            itemTag?.tags?.forEach { tag ->
                if (tag.key.contains("PRINCIPAL_AMOUNT", ignoreCase = true)) {
                    loanAmountValue = tag.value.replace(" INR", "")
                    return@itemTags
                }
            }
        }

        var minAmount = "0"
        //Get maxAmount
        offer.itemTags?.forEach itemTag@{ itemTag ->
            itemTag?.tags?.forEach { tag ->
                if (tag.key.contains("MINIMUM_DOWNPAYMENT", ignoreCase = true)) {
                    minAmount = tag.value.replace(" INR", "")
                    return@itemTag
                }
            }
        }

        //Get maxAmount
        var maxAmount = loanAmountValue
        offer.itemPrice?.value?.let {
            maxAmount = it
        }

        offer.id?.let {

            var interest = ""
            loanAmountValue.let loanAmount@{
                offer.itemTags?.forEach {
                    it?.tags?.forEach { tag ->
                        if (tag.key.equals("INTEREST_RATE", ignoreCase = true)
                        ) {
                            interest = tag.value
                            return@loanAmount
                        }
                    }
                }
            }

            offer.itemTags?.forEach itemTags@{ itemTag ->
                itemTag?.tags?.forEach { tag ->
                    if (tag.key.contains("NUMBER_OF_INSTALLMENTS", ignoreCase = true)) {
                        openSheet(maxAmount, minAmount, interest, tag.value)
                        return@itemTags
                    }
                }
            }
        }
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
        when (fromFlow) {
            "Personal Loan" -> {
                val formUrl = offer.formUrl
                offer.txnId?.let { transactionId ->
                    formUrl?.let { kycUrl ->
                        navigateToLoanProcessScreen(
                            navController = navController, statusId = 3, responseItem = kycUrl,
                            offerId = id, transactionId = transactionId, fromFlow = fromFlow
                        )
                    }
                }
            }

            "Purchase Finance" -> {
                val formUrl = offer.formUrl
                offer.txnId?.let { transactionId ->
                    formUrl?.let { kycUrl ->
                        navigateToLoanProcessScreen(
                            navController = navController, statusId = 21, responseItem = kycUrl,
                            offerId = id, transactionId = transactionId, fromFlow = fromFlow
                        )
                    }
                }
            }

            else -> {
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
}


@Composable
fun EditLoanRequest(
    context: Context,
    offer: OfferResponseItem,
    navController: NavHostController,
    id: String,
    fromFlow: String
) {

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
                    if (title.lowercase(Locale.ROOT).contains("principal")) {
                        it.value?.let { value ->
                            loanAmountValue = value
                        }
                    }
                }
            }
        }


        offer.id?.let { offerId ->
            // min Loan Amount is whatever Lender sent as MinLoanAmt
            // if its empty, Principal - 10,000
            val minLoanAmount: String = offer.minLoanAmount
                ?.takeIf { it.isNotEmpty() }
                ?: (loanAmountValue.toIntOrNull()?.minus(10000)?.toString() ?: "2000")



            loanAmountValue.let loanAmount@{ loanAmountValue ->
                offer.itemTags?.forEach {
                    it?.tags?.forEach { tag ->
                        if (tag.key.equals("LOAN_TERM", ignoreCase = true) ||
                            tag.key.equals("TERM", ignoreCase = true)
                        ) {
                            val loanAmount = if (fromFlow == "Personal Loan") {
                                loanAmountValue
                            } else {
                                loanAmountValue
                            }
                            val loanTenure = if (fromFlow == "Personal Loan") {
                                tag.value
                            } else {
                                tag.value?.lowercase()?.replace(" months", "")
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
                                    loanAmount = loanAmount, loanTenure = loanTenure?:"",
                                    offerId = offerId, fromFlow = fromFlow
                                )
                            }
                            return@loanAmount
                        }
                    }
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
    offer.id?.let id@{ offerId ->
        loanAmountValue.let { loanAmountValue ->
            offer.itemTags?.forEach {
                it?.tags?.forEach { tag ->
                    if (tag.key.equals("LOAN_TERM", ignoreCase = true) ||
                        tag.key.equals("TERM", ignoreCase = true)
                    ) {
                        if (fromFlow.equals("Personal Loan", ignoreCase = true)) {
                            val updatedLoanTenure = tag.value?.lowercase()?.replace(" months", "")
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
                        return@id
                    }else if (fromFlow == "Purchase Finance") {
                        var loanTenure = "5"
                        offer.itemTags.forEach itemTags@{ itemTag ->
                            itemTag?.tags?.forEach { tag ->
                                if (tag.key.contains(
                                        "NUMBER_OF_INSTALLMENTS",
                                        ignoreCase = true
                                    )
                                ) {
                                    loanTenure = tag.value?:""
                                    return@itemTags
                                }
                            }
                        }
                        editLoanRequestViewModel.updatePfApiFlow(PfFlow.Normal.status)
                        editLoanRequestViewModel.pfInitiateOffer(
                            id = offerId,
                            loanType = "PURCHASE_FINANCE",
                            context = context,
                            paymentAmount = loanAmountValue,
                            loanTenure
                        )
                        return@id
                    }
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
        quoteBreakUp?.let { it ->
            it.title?.let { title ->
                val newTitle = CommonMethods().displayFormattedText(title)
                it.value?.let { description ->
                    val currency = it.currency?.let { " ($it)" } ?: ""
                    HeaderValueInARowWithTextBelowValue(
                        textHeader = newTitle + currency,
                        textValue = description,
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
fun LoanDetailsCard(offer: OfferResponseItem, context: Context, fromFlow: String) {
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
            if (it.display == true || fromFlow == "Purchase Finance") {
                it.tags.let { tags ->
                    tags.forEach { tag ->
                        val newTitle = CommonMethods().displayFormattedText(tag.key)

                        val displayValue = if (tag.key.contains("cool_off", ignoreCase = true) ||
                            tag.key.contains("cool off", ignoreCase = true)
                        ) {

                            convertUTCToLocalDateTime(tag.value?:"")
                        } else if (tag.key == "PROCESSING_FEE" ||
                            tag.key.contains("AMOUNT")
                        ) {
                            tag.value?.appendINRIfMissing()
                        } else {
                            tag.value
                        }
                        if (newTitle.equals("Tnc Link", ignoreCase = true)) {
                            ClickHeaderValueInARowWithTextBelowValue(
                                textHeader = newTitle,
                                textValue = displayValue?:"",
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
                                textValue = displayValue?:"",
                                style = normal11Text500,
                                textColorHeader = appGray,
                                textColorValue = azureBlueColor,
                                textBelowValue = "",
                                textBelowStyle = normal12Text400,
                                textColorBelowValue = appGray,
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { CommonMethods().openLink(context, displayValue) }
                            )

                        } else {
                            HeaderValueInARowWithTextBelowValue(
                                textHeader = newTitle,
                                textValue = displayValue?:"",
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

    offer.itemTags?.forEach itemTag@{ itemTags ->
        itemTags?.let {
            itemTags.name?.let {
                itemTags.tags.forEach { itemTagsItem ->
                    if (itemTagsItem.key.lowercase(Locale.ROOT).contains("principal")) {
                        itemTagsItem.value.let { value ->
                            loanAmountValue = value?:""
                            return@itemTag
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(lightishGray)
    ) {
        /* Loan */
        offer.itemPrice?.value?.let {
            val currency = offer.itemPrice.currency?.let { " ($it)" } ?: ""

            HeaderNextRowValue(
                textHeader = stringResource(id = R.string.loan) + currency,
                textValue = if(loanAmountValue.contains("INR"))"₹ ${CommonMethods().formatWithCommas(loanAmountValue.removeINRifExist().toDouble().toInt())}" else
                    CommonMethods().formatWithCommas(loanAmountValue.removeINRifExist().toDouble().toInt()),
                textColorHeader = appBlack,
                textColorValue = appBlack,
                modifier = Modifier
                    .padding(start = 17.dp, top = 0.dp, end = 10.dp, bottom = 8.dp)
                    .weight(0.5f)

            )
        }

        offer.itemTags?.forEach itemTag@{ itemTag ->
            itemTag?.tags?.forEach { tag ->
                if (tag.key.equals("interest rate", ignoreCase = true) ||
                    tag.key.equals("interest_rate", ignoreCase = true)
                ) {
                    HeaderNextRowValue(
                        textHeader = stringResource(id = R.string.rate_of_interest),
                        textValue = tag.value?:"",
                        textColorHeader = appBlack,
                        textColorValue = appBlack,
                        modifier = Modifier
                            .padding(start = 25.dp, top = 0.dp, end = 10.dp, bottom = 8.dp)
                            .weight(0.5f)
                    )
                    return@itemTag
                }
            }
        }


        offer.itemTags?.forEach itemTags@{ itemTag ->
            itemTag?.tags?.forEach { tag ->
                if (tag.key.equals("loan_term", ignoreCase = true) ||
                    tag.key.equals("term", ignoreCase = true)
                ) {
                    HeaderNextRowValue(
                        textHeader = stringResource(id = R.string.loan_tenure),
                        textValue = tag.value?:"",
                        textColorHeader = appBlack,
                        textColorValue = appBlack,
                        modifier = Modifier
                            .padding(start = 10.dp, top = 0.dp, end = 10.dp, bottom = 8.dp)
                            .weight(0.5f)
                    )
                    return@itemTags
                }
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

@Composable
fun BottomSheetContent(
    maxAmount: Float,
    minAmount: Float,
    interest: String,
    onClose: () -> Unit,
    onSubmit: (Float) -> Unit
) {

    var sliderValue by remember { mutableFloatStateOf(0.00f) }

    val stopPercentage = 0.8f
    val stopValue = minAmount + (maxAmount - minAmount) * stopPercentage // 64_000f

    Spacer(modifier = Modifier.height(30.dp))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Edit Down Payment Amount",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
            textAlign = TextAlign.Center
        )

        // Slider with value display
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column {
                Spacer(Modifier.height(60.dp))
                Box {

                    Slider(
                        value = (sliderValue - minAmount) / (maxAmount - minAmount), // Normalize to 0f..1f
                        onValueChange = { newValue ->
                            sliderValue =
                                CommonMethods().roundToNearestHundred(newValue * (maxAmount - minAmount) + minAmount)
                        },
                        valueRange = 0f..1f,
                        modifier = Modifier.fillMaxWidth(),
                        colors = SliderDefaults.colors(
                            thumbColor = appBlue,
                            activeTrackColor = appBlue,
                            inactiveTrackColor = Color.Gray
                        ),
                    )

                    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
                    val sliderWidth = screenWidth - 32.dp
                    val thumbPosition =
                        ((sliderValue - minAmount) / (maxAmount - minAmount)) * sliderWidth

                    val textWidth = 48.dp // Approximate width of the text

                    // Calculate the position where the text should stop (at 80% of the slider)
                    val stopPosition =
                        ((stopValue - minAmount) / (maxAmount - minAmount)) * sliderWidth

                    // Constrain the thumb position to stop at 80%
                    val constrainedThumbPosition = thumbPosition.coerceAtMost(stopPosition)

                    // Value Bubble (TextView)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .offset(
                                x = (constrainedThumbPosition - (textWidth / 2)), //0 to 270
                                y = (-30).dp
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp))
                                .background(
                                    color = appBlue,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 20.dp, vertical = 10.dp)

                        ) {
                            Text(
                                text = "₹ ${ CommonMethods().formatWithCommas(sliderValue.toInt())}",
                                fontSize = 12.sp,
                                color = Color.White,
                            )
                        }
                    }

                }
            }


            //Other code

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text =  CommonMethods().formatWithCommas(minAmount.toInt()))
                Text(text =  CommonMethods().formatWithCommas(maxAmount.toInt()))
            }

            Spacer(Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 15.dp)
                    .shadowWithBlueBackground(
                        blurAmount = 15.dp,
                        offsetX = 0.dp,
                        offsetY = 5.dp,
                        spread = 0.dp,
                        shadowColor = Color(0xFFADD8E6)
                    )
                    .border(1.dp, Color.Blue, MaterialTheme.shapes.medium)
                    .background(Color.White, MaterialTheme.shapes.medium)
                    .padding(vertical = 10.dp, horizontal = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "₹ ${CommonMethods().formatWithCommas(sliderValue.toInt())}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "Your Down payment Amount should be greater than or equal to min down payment specified by the lender",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 10.dp)
            )
        }

        Spacer(modifier = Modifier.height(80.dp))

        // Interest and Total Due Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            val totalDue = maxAmount - sliderValue
            val interestInt : Int = interest.replace(Regex("[^0-9]"), "").toInt()
            val interestAmount = (totalDue * interestInt/ 100.0).toInt()

            DottedBoxWithDividers(
                interest = "(${interest.replace(" ", "")})",
                totalDue = "₹ ${CommonMethods().formatWithCommas((totalDue).toInt())}",
                interestAmount =  "₹ ${CommonMethods().formatWithCommas(interestAmount)}"
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Buttons Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { onClose() },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White,
                    contentColor = Color.Red
                )
            ) {
                Text(
                    text = "Cancel",
                    fontSize = 15.sp,
                    modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp)
                )
            }
            Button(
                onClick = { onSubmit(sliderValue) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = appBlue,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Submit",
                    fontSize = 15.sp,
                    modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp)
                )
            }
        }
    }
}


fun Modifier.shadowWithBlueBackground(
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    blurAmount: Dp = 0.dp,
    spread: Dp = 0.dp,
    shadowColor: Color = Color.Black
): Modifier {
    return this.drawBehind {
        drawIntoCanvas { canvas ->
            val paint = Paint().asFrameworkPaint().apply {
                color = shadowColor.toArgb()
                maskFilter = android.graphics.BlurMaskFilter(
                    blurAmount.toPx(),
                    android.graphics.BlurMaskFilter.Blur.NORMAL
                )
            }
            val spreadPx = spread.toPx()
            val left = -spreadPx + offsetX.toPx()
            val top = -spreadPx + offsetY.toPx()
            val right = size.width + spreadPx + offsetX.toPx()
            val bottom = size.height + spreadPx + offsetY.toPx()

            canvas.drawRect(left, top, right, bottom, paint.asComposePaint())
        }
    }
}


@Composable
fun DottedBoxWithDividers(
    interestAmount : String,
    interest: String,
    totalDue: String
) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .drawBehind {
                val strokeWidth = 4.dp.toPx()
                val dashPathEffect: PathEffect =
                    PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)

                drawRect(
                    color = Color.LightGray,
                    style = Stroke(
                        width = strokeWidth,
                        pathEffect = dashPathEffect
                    )
                )

                drawLine(
                    color = Color.LightGray,
                    start = Offset(size.width / 2, 0f),
                    end = Offset(size.width / 2, size.height),
                    strokeWidth = strokeWidth
                )
            }
    ) {
        Row(
            modifier = Modifier.wrapContentSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 40.dp, vertical = 25.dp)
            ) {
                Text(
                    text = "INTEREST",
                    fontSize = 14.sp,
                    color = appBlue,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 4.dp),
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(5.dp))
                Row {

                Text(
                    text = interestAmount,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
                    Spacer(Modifier.width(5.dp))

                Text(
                    text = interest,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
                }

            }

            Column(
                modifier = Modifier.padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "TOTAL DUE",
                    fontSize = 14.sp,
                    color = appBlue,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 4.dp),
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    text = totalDue,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDottedBox() {
    DottedBoxWithDividers( interestAmount = "₹ 7,080" ,interest = "(20%)", totalDue = "₹ 59,000")
}

//@Preview
//@Composable
//fun LoanOffersListDetailsScreenPreview() {
//    LoanOffersListDetailsScreen(
//        navController = rememberNavController(), id = "1111",
//        responseItem = "{\n" +
//                "    \"_id\": \"17ae0bcf-b123-5854-914a-7804e8dc68e9\",\n" +
//                "    \"form_id\": \"439a7972-6b04-4b81-a486-56b7be3b95df\",\n" +
//                "    \"item_descriptor\": {\n" +
//                "        \"code\": \"PERSONAL_LOAN\",\n" +
//                "        \"name\": \"Personal Loan\"\n" +
//                "    },\n" +
//                "    \"item_tags\": [\n" +
//                "        {\n" +
//                "            \"display\": true,\n" +
//                "            \"name\": \"Loan Information\",\n" +
//                "            \"tags\": {\n" +
//                "                \"INTEREST_RATE\": \"17.00%\",\n" +
//                "                \"TERM\": \"24 Months\",\n" +
//                "                \"INTEREST_RATE_TYPE\": \"FIXED\",\n" +
//                "                \"APPLICATION_FEE\": \"0.00 INR\",\n" +
//                "                \"FORECLOSURE_FEE\": \"4.00% + GST\",\n" +
//                "                \"INTEREST_RATE_CONVERSION_CHARGE\": \"0\",\n" +
//                "                \"DELAY_PENALTY_FEE\": \"3.00% + GST\",\n" +
//                "                \"OTHER_PENALTY_FEE\": \"0\",\n" +
//                "                \"TNC_LINK\": \"https://www.dmifinance.in/pdf/Loan-Application-Undertaking.pdf\",\n" +
//                "                \"ANNUAL_PERCENTAGE_RATE\": \"18.22%\",\n" +
//                "                \"REPAYMENT_FREQUENCY\": \"MONTHLY\",\n" +
//                "                \"NUMBER_OF_INSTALLMENTS_OF_REPAYMENT\": \"24\",\n" +
//                "                \"COOL_OFF_PERIOD\": \"2024-08-12T10:16:32.043Z\",\n" +
//                "                \"INSTALLMENT_AMOUNT\": \"14832.68 INR\"\n" +
//                "            }\n" +
//                "        }\n" +
//                "    ],\n" +
//                "    \"provider_descriptor\": {\n" +
//                "        \"images\": [\n" +
//                "            {\n" +
//                "                \"size_type\": \"sm\",\n" +
//                "                \"url\": \"https://refo-static-public.s3.ap-south-1.amazonaws.com/dmi/dmi-sm.png\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"size_type\": \"md\",\n" +
//                "                \"url\": \"https://refo-static-public.s3.ap-south-1.amazonaws.com/dmi/dmi-md.png\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"size_type\": \"lg\",\n" +
//                "                \"url\": \"https://refo-static-public.s3.ap-south-1.amazonaws.com/dmi/dmi-lg.png\"\n" +
//                "            }\n" +
//                "        ],\n" +
//                "        \"name\": \"DMI FINANCE PRIVATE LIMITED\",\n" +
//                "        \"short_desc\": \"DMI FINANCE PRIVATE LIMITED\",\n" +
//                "        \"long_desc\": \"DMI FINANCE PRIVATE LIMITED\"\n" +
//                "    },\n" +
//                "    \"bpp_id\": \"dmi-ondcpreprod.refo.dev\",\n" +
//                "    \"msg_id\": \"78317181-75d5-5df2-9493-326e3ed1ecbe\",\n" +
//                "    \"quote_id\": \"24570402-69d6-400e-b7a3-236667ece756\",\n" +
//                "    \"provider_tags\": [\n" +
//                "        {\n" +
//                "            \"name\": \"Contact Info\",\n" +
//                "            \"tags\": {\n" +
//                "                \"GRO_NAME\": \"Ashish Sarin\",\n" +
//                "                \"GRO_EMAIL\": \"head.services@dmifinance.in/grievance@dmifinance.in\",\n" +
//                "                \"GRO_CONTACT_NUMBER\": \"011-41204444\",\n" +
//                "                \"GRO_DESIGNATION\": \"Senior Vice President - Customer Success\",\n" +
//                "                \"GRO_ADDRESS\": \"Express Building, 3rd Floor, 9-10, Bahadur Shah Zafar Marg, New Delhi-110002\",\n" +
//                "                \"CUSTOMER_SUPPORT_LINK\": \"https://portal.dmifinance.in\",\n" +
//                "                \"CUSTOMER_SUPPORT_CONTACT_NUMBER\": \"9350657100\",\n" +
//                "                \"CUSTOMER_SUPPORT_EMAIL\": \"customercare@dmifinance.in\"\n" +
//                "            }\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"name\": \"Lsp Info\",\n" +
//                "            \"tags\": {\n" +
//                "                \"LSP_NAME\": \"DMI Finance Pvt. Ltd\",\n" +
//                "                \"LSP_EMAIL\": \"customercare@dmifinance.in\",\n" +
//                "                \"LSP_CONTACT_NUMBER\": \"9350657100\",\n" +
//                "                \"LSP_ADDRESS\": \"Express Building, 3rd Floor, 9-10, Bahadur Shah Zafar Marg New Delhi-110002\"\n" +
//                "            }\n" +
//                "        }\n" +
//                "    ],\n" +
//                "    \"quote_price\": {\n" +
//                "        \"currency\": \"INR\",\n" +
//                "        \"value\": \"359524.32\"\n" +
//                "    },\n" +
//                "    \"item_id\": \"d9eb81e2-96b5-477f-98dc-8518ad60d72e\",\n" +
//                "    \"bpp_uri\": \"https://dmi-ondcpreprod.refo.dev/app/ondc/seller\",\n" +
//                "    \"from_url\": \"https://dmi-ondcpreprod.refo.dev/loans/lvform/439a7972-6b04-4b81-a486-56b7be3b95df\",\n" +
//                "    \"provider_id\": \"101\",\n" +
//                "    \"item_price\": {\n" +
//                "        \"currency\": \"INR\",\n" +
//                "        \"value\": \"359524.32\"\n" +
//                "    },\n" +
//                "    \"quote_breakup\": [\n" +
//                "        {\n" +
//                "            \"currency\": \"INR\",\n" +
//                "            \"title\": \"PRINCIPAL\",\n" +
//                "            \"value\": \"300000.00\"\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"currency\": \"INR\",\n" +
//                "            \"title\": \"INTEREST\",\n" +
//                "            \"value\": \"55984.32\"\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"currency\": \"INR\",\n" +
//                "            \"title\": \"NET_DISBURSED_AMOUNT\",\n" +
//                "            \"value\": \"296460.00\"\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"currency\": \"INR\",\n" +
//                "            \"title\": \"OTHER_UPFRONT_CHARGES\",\n" +
//                "            \"value\": \"0.00\"\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"currency\": \"INR\",\n" +
//                "            \"title\": \"INSURANCE_CHARGES\",\n" +
//                "            \"value\": \"0.00\"\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"currency\": \"INR\",\n" +
//                "            \"title\": \"OTHER_CHARGES\",\n" +
//                "            \"value\": \"0.00\"\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"currency\": \"INR\",\n" +
//                "            \"title\": \"PROCESSING_FEE\",\n" +
//                "            \"value\": \"3540.00\"\n" +
//                "        }\n" +
//                "    ],\n" +
//                "    \"txn_id\": \"02d1272c-39a6-59ae-91c1-51433cba45b7\",\n" +
//                "    \"user_id\": \"5a9ba55d-bb12-5cb6-b320-e6d3d0fccedf\",\n" +
//                "    \"doc_id\": \"cfc85dcd-9f59-51e4-9f99-5dd688ee20fe\"\n" +
//                "}",
//        showButtonId = "1",
//        fromFlow = "Personal"
//    )
//}



fun String.appendINRIfMissing(): String {
    return if (!this.contains("INR")) {
        "$this INR"
    } else {
        this
    }
}

fun String.removeINRifExist(): String{
    return if(!this.contains("INR")){
        this
    }else{
        this.split(" INR")[0]
    }
}


@Preview
@Composable
fun LoanOffersListDetailsPfScreenPreview() {
    LoanOffersListDetailsScreen(
        navController = rememberNavController(), id = "1111",
        responseItem = "{\n" +
                "  \"_id\": \"e2de3b6c-c969-5830-9f33-5927fe2f7a80\",\n" +
                "  \"item_descriptor\": {\n" +
                "    \"code\": \"LOAN\",\n" +
                "    \"name\": \"Loan\"\n" +
                "  },\n" +
                "  \"item_tags\": [\n" +
                "    {\n" +
                "      \"name\": \"Information\",\n" +
                "      \"tags\": {\n" +
                "        \"INTEREST_RATE\": \"12 %\",\n" +
                "        \"TERM\": \"PT5M\",\n" +
                "        \"INTEREST_RATE_TYPE\": \"FIXED\",\n" +
                "        \"APPLICATION_FEE\": \"1000 INR\",\n" +
                "        \"FORECLOSURE_FEE\": \"0.5 %\",\n" +
                "        \"INTEREST_RATE_CONVERSION_CHARGE\": \"1000 INR\",\n" +
                "        \"DELAY_PENALTY_FEE\": \"5 %\",\n" +
                "        \"OTHER_PENALTY_FEE\": \"1 %\",\n" +
                "        \"ANNUAL_PERCENTAGE_RATE\": \"5 %\",\n" +
                "        \"REPAYMENT_FREQUENCY\": \"PT1M\",\n" +
                "        \"NUMBER_OF_INSTALLMENTS\": \"7\",\n" +
                "        \"TNC_LINK\": \"https://icicibank.com/loan/tnc.html\",\n" +
                "        \"INSTALLMENT_AMOUNT\": \"10000 INR\",\n" +
                "        \"PRINCIPAL_AMOUNT\": \"65000 INR\",\n" +
                "        \"INTEREST_AMOUNT\": \"4000 INR\",\n" +
                "        \"PROCESSING_FEE\": \"500.00\",\n" +
                "        \"OTHER_UPFRONT_CHARGES\": \"0.00\",\n" +
                "        \"INSURANCE_CHARGES\": \"500.00\",\n" +
                "        \"NET_DISBURSED_AMOUNT\": \"64000.00\",\n" +
                "        \"OTHER_CHARGES\": \"0.00\",\n" +
                "        \"OFFER_VALIDITY\": \"PT15D\",\n" +
                "        \"MINIMUM_DOWNPAYMENT\": \"0 INR\",\n" +
                "        \"SUBVENTION_RATE\": \"5 %\"\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"provider_descriptor\": {\n" +
                "    \"images\": [\n" +
                "      {\n" +
                "        \"size_type\": \"sm\",\n" +
                "        \"url\": \"https://ondc.org/assets/theme/images/ondc_registered_logo.svg?v=399788fda7\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"name\": \"ONDC Bank\",\n" +
                "    \"short_desc\": \"Ondc Bank Ltd\",\n" +
                "    \"long_desc\": \"ONDC Bank Ltd, India.\"\n" +
                "  },\n" +
                "  \"bpp_id\": \"pramaan.ondc.org/beta/preprod/mock/seller\",\n" +
                "  \"msg_id\": \"db43ec46-da41-55b9-8923-8a2dc29a3d83\",\n" +
                "  \"provider_tags\": [\n" +
                "    {\n" +
                "      \"name\": \"Contact Info\",\n" +
                "      \"tags\": {\n" +
                "        \"GRO_NAME\": \"ONDC\",\n" +
                "        \"GRO_EMAIL\": \"lifeline@ondc.com\",\n" +
                "        \"GRO_CONTACT_NUMBER\": \"1860 266 7766\",\n" +
                "        \"CUSTOMER_SUPPORT_LINK\": \"Nodal Grievance Redressal Officer\",\n" +
                "        \"CUSTOMER_SUPPORT_CONTACT_NUMBER\": \"1800 1080\",\n" +
                "        \"CUSTOMER_SUPPORT_EMAIL\": \"customer.care@ondc.com\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Lsp Info\",\n" +
                "      \"tags\": {\n" +
                "        \"LSP_NAME\": \"ONDC_BANK_LSP\",\n" +
                "        \"LSP_EMAIL\": \"lsp@ondcbank.com\",\n" +
                "        \"LSP_CONTACT_NUMBER\": \"1860 266 7766\",\n" +
                "        \"LSP_ADDRESS\": \"One Indiabulls centre, Tower 1, 18th Floor Jupiter mill compound 841, Senapati Bapat Marg, Elphinstone Road, Mumbai 400013\"\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"item_id\": \"a51e9957-013e-402d-a8cd-7a6424dbe9e1\",\n" +
                "  \"bpp_uri\": \"https://pramaan.ondc.org/beta/preprod/mock/seller\",\n" +
                "  \"provider_id\": \"c29d8cf3-5eeb-4aac-9eff-f5fadfd4dbc3\",\n" +
                "  \"item_price\": {\n" +
                "    \"currency\": \"INR\",\n" +
                "    \"value\": \"442300.00\"\n" +
                "  },\n" +
                "  \"txn_id\": \"105df1e4-a60a-5ea1-b379-521aaa602a29\",\n" +
                "  \"payments\": [\n" +
                "    {\n" +
                "      \"tags\": [\n" +
                "        {\n" +
                "          \"display\": false,\n" +
                "          \"descriptor\": {\n" +
                "            \"code\": \"BPP_TERMS\",\n" +
                "            \"name\": \"BPP Terms of Engagement\"\n" +
                "          },\n" +
                "          \"list\": [\n" +
                "            {\n" +
                "              \"descriptor\": {\n" +
                "                \"code\": \"BUYER_FINDER_FEES_TYPE\"\n" +
                "              },\n" +
                "              \"value\": \"PERCENT_ANNUALIZED\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"descriptor\": {\n" +
                "                \"code\": \"BUYER_FINDER_FEES_PERCENTAGE\"\n" +
                "              },\n" +
                "              \"value\": \"1\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"descriptor\": {\n" +
                "                \"code\": \"SETTLEMENT_WINDOW\"\n" +
                "              },\n" +
                "              \"value\": \"PT30D\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"descriptor\": {\n" +
                "                \"code\": \"SETTLEMENT_BASIS\"\n" +
                "              },\n" +
                "              \"value\": \"INVOICE_RECEIPT\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"descriptor\": {\n" +
                "                \"code\": \"MANDATORY_ARBITRATION\"\n" +
                "              },\n" +
                "              \"value\": \"TRUE\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"descriptor\": {\n" +
                "                \"code\": \"COURT_JURISDICTION\"\n" +
                "              },\n" +
                "              \"value\": \"New Delhi\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"descriptor\": {\n" +
                "                \"code\": \"STATIC_TERMS\"\n" +
                "              },\n" +
                "              \"value\": \"https://bpp.credit.becknprotocol.org/personal-banking/loans/personal-loan\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"descriptor\": {\n" +
                "                \"code\": \"OFFLINE_CONTRACT\"\n" +
                "              },\n" +
                "              \"value\": \"true\"\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ],\n" +
                "  \"user_id\": \"70f90ace-4451-587d-bcc5-dc3d784a8aee\",\n" +
                "  \"doc_id\": \"a314d3de-3577-5aa8-9a2b-acba5ee4647b\",\n" +
                "  \"category\": [\n" +
                "    {\n" +
                "      \"id\": \"101124\",\n" +
                "      \"descriptor\": {\n" +
                "        \"code\": \"PURCHASE_FINANCE\",\n" +
                "        \"name\": \"Purchase Finance\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}",
        showButtonId = "1",
        fromFlow = "Purchase Finance"
    )
}




@Preview
@Composable
private fun PreviewBottomSheet() {
    Surface {
        BottomSheetContent(442712.00f, 0.0f, "12 %", {}, {})
    }
}

