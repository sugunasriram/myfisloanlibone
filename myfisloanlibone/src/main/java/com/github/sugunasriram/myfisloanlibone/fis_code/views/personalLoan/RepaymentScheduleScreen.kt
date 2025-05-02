package com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CenterProgress
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CenterProgressFixedHeight
import com.github.sugunasriram.myfisloanlibone.fis_code.components.ClickableText
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CurvedPrimaryButton
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CurvedPrimaryButtonFull
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CurvedPrimaryButtonMultipleInRow
import com.github.sugunasriram.myfisloanlibone.fis_code.components.ForeClosureAnimator
import com.github.sugunasriram.myfisloanlibone.fis_code.components.FullWidthRoundShapedCard
import com.github.sugunasriram.myfisloanlibone.fis_code.components.HeaderValueInARowWithTextBelowValue
import com.github.sugunasriram.myfisloanlibone.fis_code.components.OnlyClickAbleText
import com.github.sugunasriram.myfisloanlibone.fis_code.components.OnlyReadAbleText
import com.github.sugunasriram.myfisloanlibone.fis_code.components.StartingText
import com.github.sugunasriram.myfisloanlibone.fis_code.components.TopBar
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateApplyByCategoryScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateSignInPage
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToCreateIssueScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToIssueListScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToLoanStatusScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToPrePaymentWebViewScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.StatusResponse
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.OrderPaymentStatusItem
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.PaymentsItem
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.igm.CheckOrderIssueModel
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.personaLoan.OfferResponseItem
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.personaLoan.OrderByIdResponse
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.personaLoan.UpdateLoanAgreement
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.personaLoan.UpdateLoanBody
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlack
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlue
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appGray
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.azureBlueColor
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.bokaraGrayColor
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.bold16Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.bold18Text100
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.errorGray
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.hintTextColor
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.lightGray
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.lightishGray
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.lightishGrayColor
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal12Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal14Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal14Text500
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal16Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal16Text700
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal18Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.semiBold20Text500
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.slateGrayColor
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.igm.CreateIssueViewModel
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.personalLoan.GetOrderPaymentStatusViewModel
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.personalLoan.LoanAgreementViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

var amount_to_be_paid = ""
var coolOffPeriodDate = ""
var principal = ""

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RepaymentScheduleScreen(navController: NavHostController, orderId: String, fromFlow: String) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val loanAgreementViewModel: LoanAgreementViewModel = viewModel()
    val createIssueViewModel: CreateIssueViewModel = viewModel()
    val getOrderPaymentStatusViewModel: GetOrderPaymentStatusViewModel = viewModel()

    val updateProcessing by loanAgreementViewModel.updateProcessing.collectAsState()
    val updateProcessed by loanAgreementViewModel.updateProcessed.collectAsState()
    val updatedLoanAgreement by loanAgreementViewModel.updatedLoanAgreement.collectAsState()
    val gettingOrderById by loanAgreementViewModel.gettingOrderById.collectAsState()
    val orderByIdLoaded by loanAgreementViewModel.orderByIdLoaded.collectAsState()
    val orderByIdResponse by loanAgreementViewModel.orderByIdResponse.collectAsState()
    val checkedOrderIssues by loanAgreementViewModel.checkedOrderIssues.collectAsState()
    val checkingOrderIssues by loanAgreementViewModel.checkingOrderIssues.collectAsState()
    val checkOrderIssueResponse by loanAgreementViewModel.checkOrderIssueResponse.collectAsState()
    val checkingStatus by loanAgreementViewModel.checkingStatus.collectAsState()
    val checked by loanAgreementViewModel.checked.collectAsState()
    val loanStatus by loanAgreementViewModel.status.collectAsState()

    val showServerIssueScreen by loanAgreementViewModel.showServerIssueScreen.observeAsState(false)
    val unexpectedErrorScreen by loanAgreementViewModel.unexpectedError.observeAsState(false)
    val showInternetScreen by loanAgreementViewModel.showInternetScreen.observeAsState(false)
    val showTimeOutScreen by loanAgreementViewModel.showTimeOutScreen.observeAsState(false)
    val unAuthorizedUser by loanAgreementViewModel.unAuthorizedUser.observeAsState(false)
    val errorHandling by loanAgreementViewModel.errorHandling.observeAsState(false)
    val middleLoan by loanAgreementViewModel.middleLoan.observeAsState(false)
    val errorMessage by loanAgreementViewModel.errorMessage.collectAsState()

    val orderPaymentListLoading by getOrderPaymentStatusViewModel.orderPaymentListLoading.collectAsState()
    val orderPaymentListLoaded by getOrderPaymentStatusViewModel.orderPaymentListLoaded.collectAsState()
    val orderPaymentStatusList by getOrderPaymentStatusViewModel.orderPaymentStatusList.collectAsState()
    val navigationToSignIn by getOrderPaymentStatusViewModel.navigationToSignIn.collectAsState()

    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden, skipHalfExpanded = true
    )

    val bottomSheetStateValue = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden, skipHalfExpanded = true
    )
    val loanType = CommonMethods().setFromFlow(fromFlow)

    // Call getOrderPaymentStatus when the composable first loads
    LaunchedEffect(Unit) {
        // Fetch payment status
        getOrderPaymentStatusViewModel.getOrderPaymentStatus(
            loanType = loanType, loanId = orderId, context = context
        )
    }


    when {
        navigationToSignIn -> navigateSignInPage (navController)
        showInternetScreen -> CommonMethods().ShowInternetErrorScreen(navController)
        showTimeOutScreen -> CommonMethods().ShowTimeOutErrorScreen(navController)
        showServerIssueScreen -> CommonMethods().ShowServerIssueErrorScreen(navController)
        unexpectedErrorScreen -> CommonMethods().ShowUnexpectedErrorScreen(navController)
        unAuthorizedUser -> CommonMethods().ShowUnAuthorizedErrorScreen(navController)
        middleLoan -> CommonMethods().ShowNoResponseFormLendersScreen(navController)
        errorHandling -> {
            PrePaymentStatusScreen(
                navController = navController,
                orderId = orderId,
                fromFlow = fromFlow,
                headerText = stringResource(id = R.string.repayment_un_successful),
                showButton = true,
                image = painterResource(id = R.drawable.unsuccessful_purchase_image),
            ) {}
        }

        else -> {
            RepaymentScheduleScreenHandle(
                checkingOrderIssues = checkingOrderIssues, scope = scope, loanType = loanType,
                checkedOrderIssues = checkedOrderIssues, orderId = orderId,
                bottomSheetStateValue = bottomSheetStateValue, context = context,
                getOrderPaymentStatusViewModel = getOrderPaymentStatusViewModel,
                loanAgreementViewModel = loanAgreementViewModel, navController = navController,
                updateProcessed = updateProcessed, gettingOrderById = gettingOrderById,
                updateProcessing = updateProcessing, fromFlow = fromFlow,
                bottomSheetState = bottomSheetState, orderByIdLoaded = orderByIdLoaded,
                updatedLoanAgreement = updatedLoanAgreement,
                orderPaymentListLoading = orderPaymentListLoading,
                checkOrderIssueResponse = checkOrderIssueResponse,
                orderPaymentStatusList = orderPaymentStatusList,
                orderPaymentListLoaded = orderPaymentListLoaded,
                orderByIdResponse = orderByIdResponse,checked = checked,
                checkingStatus = checkingStatus,loanStatus = loanStatus
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RepaymentScheduleScreenHandle(
    checkingOrderIssues: Boolean, checkedOrderIssues: Boolean, gettingOrderById: Boolean,
    orderId: String, bottomSheetStateValue: ModalBottomSheetState, context: Context,
    getOrderPaymentStatusViewModel: GetOrderPaymentStatusViewModel,
    loanAgreementViewModel: LoanAgreementViewModel, updateProcessed: Boolean,
    updateProcessing: Boolean, fromFlow: String, navController: NavHostController,
    bottomSheetState: ModalBottomSheetState, scope: CoroutineScope,
    updatedLoanAgreement: UpdateLoanAgreement?, orderPaymentListLoading: Boolean,
    checkOrderIssueResponse: CheckOrderIssueModel?, orderByIdLoaded: Boolean,
    orderPaymentStatusList: ArrayList<OrderPaymentStatusItem>?, loanType: String,
    orderPaymentListLoaded: Boolean, orderByIdResponse: OrderByIdResponse?,checked: Boolean,
    checkingStatus: Boolean, loanStatus: StatusResponse?
) {
    var selectedOption by remember { mutableStateOf("") }
    if (checkingOrderIssues || gettingOrderById || checkingStatus || orderPaymentListLoading) {
        CenterProgress()
    } else {
        if (checkedOrderIssues || orderByIdLoaded || checked || orderPaymentListLoaded) {
            if (checked){
                loanStatus?.data?.data?.data?.catalog?.let { loanDetails ->
                    ModalBottomSheetLayout(
                        sheetState = bottomSheetStateValue,
                        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                        sheetContent = {
                            BottomSheetHandle(
                                loanDetails = loanDetails, context = context,
                                loanAgreementViewModel = loanAgreementViewModel,
                                updateProcessed = updateProcessed, updateProcessing = updateProcessing,
                                fromFlow = fromFlow, navController = navController,
                                scope = scope, bottomSheetStateValue = bottomSheetStateValue,
                                updatedLoanAgreement = updatedLoanAgreement, loanType = loanType,
                                orderPaymentListLoading = orderPaymentListLoading,
                                selectedOption = selectedOption, orderId = orderId
                            )
                        }
                    ) {
                        ModalBottomSheetLayout(
                            sheetState = bottomSheetState,
                            sheetContent = {
                                var showMissedEmi = false
                                loanDetails.payments?.forEach { payment ->
                                    if (payment?.type == "POST_FULFILLMENT" && payment.status == "DELAYED") {
                                        showMissedEmi = true
                                    }
                                }
                                PopupWithRadioButtons(
                                    fromFlow = fromFlow, context = context, loanType = loanType,
                                    showMissedEmi = showMissedEmi, navController = navController,
                                    onDismiss = { scope.launch { bottomSheetState.hide() } },
                                    onOptionSelected = { option ->
                                        selectedOption = option
                                        if (option == "MISSED_EMI_PAYMENT" || option == "PRE_PART_PAYMENT" || option == "FORECLOSURE") {
                                            scope.launch {
                                                bottomSheetStateValue.show()
                                            }
                                        }
                                    },
                                    loanDetails = loanDetails, orderId = orderId
                                )
                            },
                            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        ) {
                            LoanSheet(
                                navController = navController, scope = scope, context = context,
                                checkOrderIssueResponse = checkOrderIssueResponse,
                                loanDetails = loanDetails, fromFlow = fromFlow,
                                orderPaymentListLoaded = orderPaymentListLoaded,
                                orderPaymentStatusList = orderPaymentStatusList,
                                bottomSheetState = bottomSheetState, orderId = orderId,
                                getOrderPaymentStatusViewModel = getOrderPaymentStatusViewModel,
                                loanAgreementViewModel = loanAgreementViewModel,
                                loanType = loanType
                            )
                        }
                    }
                }
            } else {
                orderByIdResponse?.data?.let { loanDetails ->

                    ModalBottomSheetLayout(
                        sheetState = bottomSheetStateValue,
                        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                        sheetContent = {
                            BottomSheetHandle(
                                loanDetails = loanDetails, context = context,
                                loanAgreementViewModel = loanAgreementViewModel,
                                updateProcessed = updateProcessed, fromFlow = fromFlow,
                                navController = navController, updateProcessing = updateProcessing,
                                scope = scope, bottomSheetStateValue = bottomSheetStateValue,
                                updatedLoanAgreement = updatedLoanAgreement, loanType = loanType,
                                orderPaymentListLoading = orderPaymentListLoading,
                                selectedOption = selectedOption, orderId = orderId
                            )
                        }
                    ) {
                        ModalBottomSheetLayout(
                            sheetState = bottomSheetState,
                            sheetContent = {
                                var showMissedEmi = false
                                loanDetails.payments?.forEach { payment ->
                                    if (payment?.type == "POST_FULFILLMENT" && payment.status == "DELAYED") {
                                        showMissedEmi = true
                                    }
                                }
                                PopupWithRadioButtons(
                                    fromFlow = fromFlow, context = context, loanType = loanType,
                                    showMissedEmi = showMissedEmi, navController = navController,
                                    onDismiss = { scope.launch { bottomSheetState.hide() } },
                                    onOptionSelected = { option ->
                                        selectedOption = option
                                        if (option == "MISSED_EMI_PAYMENT" || option == "PRE_PART_PAYMENT" || option == "FORECLOSURE") {
                                            scope.launch {
                                                bottomSheetStateValue.show()
                                            }
                                        }
                                    },
                                    loanDetails = loanDetails, orderId = orderId
                                )
                            },
                            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        ) {
                            LoanSheet(
                                navController = navController, scope = scope, context = context,
                                checkOrderIssueResponse = checkOrderIssueResponse,
                                loanDetails = loanDetails, fromFlow = fromFlow,
                                orderPaymentListLoaded = orderPaymentListLoaded,
                                orderPaymentStatusList = orderPaymentStatusList,
                                bottomSheetState = bottomSheetState, orderId = orderId,
                                getOrderPaymentStatusViewModel = getOrderPaymentStatusViewModel,
                                loanAgreementViewModel = loanAgreementViewModel,
                                loanType = loanType
                            )
                        }
                    }
                }
            }
        } else {
            loanAgreementViewModel.getOrderById(
                orderId = orderId, context = context, loanType = loanType
            )
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LoanSheet(
    navController: NavHostController, checkOrderIssueResponse: CheckOrderIssueModel?,
    loanDetails: OfferResponseItem, fromFlow: String, orderPaymentListLoaded: Boolean,
    orderPaymentStatusList: ArrayList<OrderPaymentStatusItem>?, context: Context,
    bottomSheetState: ModalBottomSheetState, scope: CoroutineScope,orderId: String,
    getOrderPaymentStatusViewModel: GetOrderPaymentStatusViewModel,
    loanAgreementViewModel: LoanAgreementViewModel,loanType: String
) {
    val focusManager = LocalFocusManager.current
    val backGroundColor: Color = Color.White

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backGroundColor)
            .verticalScroll(rememberScrollState())
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }
    ) {
        TopBar(
            navController = navController, showBackButton = true,
            onClick = { navigateToLoanStatusScreen(navController) }
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                RepaymentHeaderPart(
                    checkOrderIssueResponse = checkOrderIssueResponse, fromFlow = fromFlow,
                    loanDetails = loanDetails, navController = navController,
                )

                //Get Cool Off Period
                loanDetails.itemTags?.forEach { itemTags ->
                    itemTags?.let {
                        it.tags.let { tags ->
                            tags.forEach { tag ->
                                if (tag.key.contains("cool_off", ignoreCase = true)) {
                                    coolOffPeriodDate = tag.value?:""
                                    return@forEach // Exit the loop after processing the first matching tag
                                }
                            }
                        }
                    }
                }

                //PRINCIPAL
                loanDetails.quoteBreakUp?.forEach { quoteBreakUp ->
                    quoteBreakUp?.let {
                        it.title?.let { title ->
                            it.value?.let { description ->
                                if (title.equals("PRINCIPAL", ignoreCase = true)) {
                                    principal = description
                                    return@forEach // Exit the loop after processing the first matching tag
                                }
                            }
                        }
                    }
                }
                CompleteLoanDetails(
                    loanDetails = loanDetails, context = context,
                    orderPaymentListLoaded = orderPaymentListLoaded,
                    orderPaymentStatusList = orderPaymentStatusList
                )
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        RepaymentBottomPart(
            loanDetails = loanDetails, scope = scope, orderId = orderId, context = context,
            bottomSheetState = bottomSheetState, fromFlow = fromFlow,loanType = loanType,
            navController = navController,getOrderPaymentStatusViewModel =
            getOrderPaymentStatusViewModel, loanAgreementViewModel =
                    loanAgreementViewModel
        )
        Spacer(modifier = Modifier.height(5.dp))
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetHandle(
    loanDetails: OfferResponseItem, context: Context, navController: NavHostController,
    loanAgreementViewModel: LoanAgreementViewModel, scope: CoroutineScope,
    updateProcessed: Boolean, updateProcessing: Boolean, fromFlow: String,
    bottomSheetStateValue: ModalBottomSheetState, updatedLoanAgreement: UpdateLoanAgreement?,
    orderPaymentListLoading: Boolean, selectedOption: String, orderId: String, loanType: String
) {
    when (selectedOption) {
        "MISSED_EMI_PAYMENT" -> {
            MissedEmi(
                updateProcessing = updateProcessing, navController = navController,
                updateProcessed = updateProcessed, loanDetails = loanDetails,
                loanAgreementViewModel = loanAgreementViewModel,
                fromFlow = fromFlow, scope = scope, context = context,
                bottomSheetStateValue = bottomSheetStateValue, loanType = loanType,
                updatedLoanAgreement = updatedLoanAgreement, orderId = orderId
            )
        }

        "PRE_PART_PAYMENT" -> {
            if (updateProcessing || orderPaymentListLoading) {
                CenterProgressFixedHeight()
            }
        }

        "FORECLOSURE" -> {
            ForeClosure(
                loanDetails = loanDetails, updatedLoanAgreement = updatedLoanAgreement,
                updateProcessed = updateProcessed, updateProcessing = updateProcessing,
                context = context, navController = navController,
                loanAgreementViewModel = loanAgreementViewModel, scope = scope,
                bottomSheetStateValue = bottomSheetStateValue,
                fromFlow = fromFlow, orderId = orderId, loanType = loanType
            )
        }
    }
}

@Composable
fun CompleteLoanDetails(
    loanDetails: OfferResponseItem, orderPaymentListLoaded: Boolean, context: Context,
    orderPaymentStatusList: ArrayList<OrderPaymentStatusItem>?
) {
    // Application Details
    ApplicantDetails(loanDetails)

    // Loan Details
    LoanApplicationDetails(loanDetails, context)

    // Loan Summary
    LoanSummary(loanDetails)

    if (orderPaymentListLoaded) {
        Log.d("PaymentHistory", "orderPaymentStatusList : $orderPaymentStatusList")

        val relevantPayments = orderPaymentStatusList?.filter { payment ->
            payment.status == "PAID"
        }
        if (!relevantPayments.isNullOrEmpty()) {
            Log.d("PaymentHistory", "orderPaymentStatusList-Not empty: $orderPaymentStatusList")

            PaymentHistoryCard(relevantPayments)
        }
    }

    // Contact details
    ContactDetailsCard(loanDetails)

    // EMI Details Table
    EmiDetail(loanDetails)

    // GRO details
    GrowDetailsCard(loanDetails, context)

    // Loan Agreement Details
    LoanAgreementDetailsCard(loanDetails, context)

    // Loan Cancellation Terms
    CancellationTermsCard(loanDetails, context)

    Spacer(modifier = Modifier.height(30.dp))
}

@Composable
fun EMIDetailsTable(emiItems: PaymentsItem?, showEmiDetails: Boolean) {
    if (showEmiDetails) {
        Column(modifier = Modifier.fillMaxWidth()) {
            emiItems?.id?.let { emiNum ->
                emiItems.status?.let { status ->
                    emiItems.params?.amount?.let { amount ->
                        emiItems.time?.range?.start?.let { timestamp ->
                            val date = CommonMethods().displayFormattedDate(timestamp)
                            TableRow(
                                emiNumber = emiNum,
                                dueDate = date,
                                amount = amount,
                                status = status
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TableRow(
    emiNumber: String, dueDate: String, amount: String,
    status: String, isTitleRow: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 0.dp, end = 0.dp, bottom = 8.dp)
    ) {
        Text(
            text = emiNumber,
            fontWeight = if (isTitleRow) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.weight(0.8f)
        )
        Text(
            text = dueDate,
            fontWeight = if (isTitleRow) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.weight(2f)
        )
        Text(
            text = amount,
            fontWeight = if (isTitleRow) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.weight(1.5f)
        )
        Text(
            text = status,
            fontWeight = if (isTitleRow) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.weight(1.5f)
        )
    }
}

@Composable
fun GrowDetailsCard(groTags: OfferResponseItem, context: Context) {
    var showGroDetails by remember { mutableStateOf(false) }
    Row(modifier = Modifier.clickable { showGroDetails = !showGroDetails }) {
        StartingText(
            modifier = Modifier.weight(0.7f),
            text = stringResource(id = R.string.gro_details),
            textColor = appBlack,
            top = 20.dp, start = 20.dp, end = 30.dp, bottom = 10.dp,
            style = normal16Text700,
            textAlign = TextAlign.Start,
        )

        IconButton(modifier = Modifier.weight(0.3f),
            onClick = { showGroDetails = !showGroDetails }) {
            Icon(
                imageVector = if (showGroDetails) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        }
    }
    if (showGroDetails) {
        FullWidthRoundShapedCard(onClick = { /*TODO*/ }, cardColor = lightishGray) {
            val groInfo = groTags.providerTags?.firstOrNull { it?.name == "Contact Info" }?.tags
            groInfo?.forEach { (key, value) ->
                val newTitle = CommonMethods().displayFormattedText(key)

                if (newTitle.equals("Customer support link", ignoreCase = true)) {
                    OnlyClickAbleText(
                        textHeader = newTitle,
                        textValue = value,
                        textColorHeader = hintTextColor,
                        textColorValue = azureBlueColor,
                        style = normal14Text400,
                        end = 5.dp, start = 5.dp, top = 20.dp,
                        onClick = { CommonMethods().openLink(context, value) }
                    )
                } else {
                    OnlyReadAbleText(
                        textHeader = newTitle,
                        textValue = value,
                        textColorHeader = hintTextColor,
                        textColorValue = bokaraGrayColor,
                        style = normal14Text400,
                        end = 5.dp, start = 5.dp, top = 20.dp
                    )
                }
            }
        }
    }
}

@Composable
fun ContactDetailsCard(lspTags: OfferResponseItem) {
    var showDetails by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .clickable { showDetails = !showDetails }
    ) {
        StartingText(
            text = stringResource(id = R.string.contact_details),
            modifier = Modifier.weight(0.7f),
            textColor = appBlack,
            top = 20.dp, start = 20.dp, end = 30.dp, bottom = 0.dp,
            style = normal16Text700,
            textAlign = TextAlign.Start,
        )

        IconButton(modifier = Modifier.weight(0.3f),
            onClick = { showDetails = !showDetails }) {
            Icon(
                imageVector = if (showDetails) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        }
    }
    if (showDetails) {
        FullWidthRoundShapedCard(onClick = { /*TODO*/ }, cardColor = lightishGray) {
            val lspInfo = lspTags.providerTags?.firstOrNull { it?.name == "Lsp Info" }?.tags
            lspInfo?.forEach { (key, value) ->
                val newTitle = CommonMethods().displayFormattedText(key)
                OnlyReadAbleText(
                    textHeader = newTitle,
                    textValue = value,
                    textColorHeader = hintTextColor,
                    textColorValue = bokaraGrayColor,
                    style = normal14Text400,
                    end = 5.dp, start = 5.dp, top = 20.dp
                )
            }
        }
    }
}

@Composable
fun PopupWithRadioButtons(
    fromFlow: String, context: Context, showMissedEmi: Boolean, onDismiss: () -> Unit,
    onOptionSelected: (String) -> Unit, loanDetails: OfferResponseItem,
    navController: NavHostController, orderId: String, loanType: String
) {
    var selectedOption by remember { mutableStateOf("") }
    var prePartPaymentAmount by remember { mutableStateOf("") }
    var showPopup by remember { mutableStateOf(true) }

    var showConfirmationPopUp by remember { mutableStateOf(false) }
    var showPaymentOptionPopup by remember { mutableStateOf(true) }

    val loanAgreementViewModel: LoanAgreementViewModel = viewModel()
    val updateProcessed by loanAgreementViewModel.updateProcessed.collectAsState()
    val updateProcessing by loanAgreementViewModel.updateProcessing.collectAsState()
    val updatedLoanAgreement by loanAgreementViewModel.updatedLoanAgreement.collectAsState()

    if (updateProcessed) {
        PrePartPaymentResponseHandle(
            updatedLoanAgreement = updatedLoanAgreement, selectedOption = selectedOption,
            navController = navController, prePartPaymentAmount = prePartPaymentAmount,
            fromFlow = fromFlow, orderId = orderId
        )
    }

    if (showPopup) {
        Column(
            modifier = Modifier
                .background(
                    Color.White,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .padding(16.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (showPaymentOptionPopup) {
                // Header Row with Title and Close Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StartingText(
                        text = stringResource(id = R.string.payment_option),
                        textColor = appBlack,
                        top = 20.dp, start = 0.dp, end = 30.dp, bottom = 0.dp,
                        style = semiBold20Text500,
                        modifier = Modifier.weight(0.5f),
                        textAlign = TextAlign.Start
                    )
                    IconButton(onClick = { onDismiss() }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(26.dp))

                // Radio Button Options
                if (showMissedEmi) {
                    RadioButtonOption(
                        text = stringResource(id = R.string.missed_emi_payment),
                        description = stringResource(id = R.string.when_buyer_missed_to_make_a_scheduled),
                        selectedOption = selectedOption,
                        optionValue = "MISSED_EMI_PAYMENT",
                        onSelectOption = {
                            selectedOption = "MISSED_EMI_PAYMENT"
                            onOptionSelected(selectedOption)
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                }
                RadioButtonOption(
                    text = stringResource(id = R.string.foreclosure),
                    description = stringResource(id = R.string.when_buyer_initiates_the_foreclosure_intent),
                    selectedOption = selectedOption,
                    optionValue = "FORECLOSURE",
                    onSelectOption = {
                        selectedOption = "FORECLOSURE"
                        onOptionSelected(selectedOption)
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                RadioButtonOption(
                    text = stringResource(id = R.string.pre_part_payment),
                    description = stringResource(id = R.string.when_buyer_initiates_the_pre_part_intent),
                    selectedOption = selectedOption,
                    optionValue = "PRE_PART_PAYMENT",
                    onSelectOption = {
                        selectedOption = "PRE_PART_PAYMENT"
                        onOptionSelected(selectedOption)
                    }
                )

                // Conditional Text Field for Pre-Part Payment Amount
                if (selectedOption == "PRE_PART_PAYMENT") {
                    OutlinedTextField(
                        value = prePartPaymentAmount,
                        onValueChange = { prePartPaymentAmount = it },
                        shape = RoundedCornerShape(8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = {
                            Text(
                                text = stringResource(id = R.string.enter_amount),
                                color = hintTextColor,
                                style = normal18Text400,
                                textAlign = TextAlign.Start
                            )
                        },
                        visualTransformation = VisualTransformation.None,
                        textStyle = LocalTextStyle.current,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = appBlue,
                            unfocusedBorderColor = lightGray,
                            cursorColor = appBlue,
                            textColor = appBlack
                        )
                    )
                }

                if (selectedOption == "") {
                    CurvedPrimaryButtonFull(
                        text = stringResource(id = R.string.proceed), style = bold16Text400,
                        backgroundColor = lightishGrayColor,
                        modifier = Modifier.padding(
                            start = 30.dp, end = 30.dp, bottom = 20.dp, top = 50.dp
                        )
                    ) {
                        CommonMethods().toastMessage(context, "Please select an option")
                    }
                } else {
                    // Proceed Button
                    if (selectedOption == "PRE_PART_PAYMENT") {
                        CurvedPrimaryButtonFull(
                            text = stringResource(id = R.string.proceed),
                            style = bold16Text400,
                            modifier = Modifier.padding(
                                start = 30.dp, end = 30.dp, bottom = 20.dp, top = 50.dp
                            )
                        ) {
                            if (prePartPaymentAmount.isEmpty()) {
                                CommonMethods().toastMessage(context, "Please Enter the Amount")
                            } else {
                                onOptionSelected(selectedOption)
                                showConfirmationPopUp = true
                            }
                        }
                    } else {
                        CurvedPrimaryButtonFull(
                            text = stringResource(id = R.string.proceed),
                            style = bold16Text400,
                            modifier = Modifier.padding(
                                start = 30.dp, end = 30.dp, bottom = 20.dp, top = 50.dp
                            )
                        ) {
                            onOptionSelected(selectedOption)
                            showConfirmationPopUp = true
                        }
                    }
                }

            }
        }

        if (showConfirmationPopUp) {
            showPaymentOptionPopup = false
            val msg = "Are you sure you want to pay ₹$prePartPaymentAmount ?"
//                    "\n *this amount may include fee charges"

            ConfirmationDialog(
                headerText = stringResource(id = R.string.pre_part_payment),
                updateProcessing = updateProcessing,
                text = msg,
                showTextDescriptor = false,
                onYesClick = {
                    loanDetails.id?.let { id ->
                        loanAgreementViewModel.updateLoanAgreementApi(
                            context, UpdateLoanBody(
                                subType = "PRE_PART_PAYMENT", id = id,
                                amount = prePartPaymentAmount,
                                loanType = loanType
                            )
                        )
                    }
                },
                onNoClick = {
                    showConfirmationPopUp = false
                    onOptionSelected(selectedOption)
                    showPaymentOptionPopup = true
                    showPopup = true
                },
            )
        }
    }
}

@Composable
fun ConfirmationDialog(
    headerText: String = stringResource(id = R.string.foreclosure),
    text: String = stringResource(id = R.string.are_u_sure_want_to_pay),
    textDescriptor: String = stringResource(id = R.string.may_include_late_charges),
    showTextDescriptor: Boolean = true,
    updateProcessing: Boolean = false, onYesClick: () -> Unit, onNoClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .padding(top = 10.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            ForeClosureAnimator()
            StartingText(
                text = headerText.uppercase(),
                textColor = slateGrayColor,
                start = 30.dp, end = 30.dp, top = 10.dp, bottom = 5.dp,
                style = bold18Text100,
                alignment = Alignment.TopCenter
            )
            StartingText(
                text = text,
                textColor = errorGray,
                start = 30.dp, end = 30.dp, top = 10.dp, bottom = 5.dp,
                style = normal14Text500,
                alignment = Alignment.TopCenter
            )
            if (showTextDescriptor) {
                StartingText(
                    text = textDescriptor,
                    textColor = errorGray,
                    start = 30.dp, end = 30.dp, top = 10.dp, bottom = 5.dp,
                    style = normal14Text500,
                    alignment = Alignment.TopCenter
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 20.dp)
                    .fillMaxWidth()
            ) {
                if (!updateProcessing) {
                    CurvedPrimaryButton(text = stringResource(id = R.string.yes).uppercase())
                    { onYesClick() }
                    CurvedPrimaryButton(text = stringResource(id = R.string.no).uppercase())
                    { onNoClick() }
                }
            }
        }
    }
}

@Composable
fun RadioButtonOption(
    text: String, description: String, selectedOption: String,
    optionValue: String, onSelectOption: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(lightishGray, shape = RoundedCornerShape(8.dp))
            .padding(vertical = 8.dp),

        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selectedOption == optionValue,
            onClick = onSelectOption,
            colors = RadioButtonDefaults.colors(selectedColor = appBlue)
        )
        Column {
            Text(text = text, fontSize = 14.sp, color = appBlack, style = normal14Text400)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = description, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun ApplicantDetails(loanDetails: OfferResponseItem) {
    FullWidthRoundShapedCard(onClick = { }, cardColor = lightishGray) {
        val applicationId = loanDetails.itemId
        loanDetails.fulfillments?.forEach { fulfilment ->
            fulfilment?.customer?.let { customer ->

                applicationId?.let {
                    OnlyReadAbleText(
                        textHeader = stringResource(id = R.string.loan_application_id),
                        textColorHeader = hintTextColor,
                        textValue = applicationId,
                        textColorValue = appBlack,
                        style = normal14Text400,
                        end = 5.dp, start = 5.dp, top = 20.dp
                    )
                }

                loanDetails.itemDescriptor?.let { itemDescriptor ->
                    itemDescriptor.name?.let { loanType ->
                        OnlyReadAbleText(
                            textHeader = stringResource(id = R.string.loan_type),
                            textColorHeader = hintTextColor,
                            textValue = loanType,
                            textColorValue = appBlack,
                            style = normal14Text400,
                            end = 5.dp, start = 5.dp, top = 20.dp
                        )
                    }
                }

                fulfilment.state?.let { state ->
                    state.descriptor?.name?.let { loanStatus ->
                        OnlyReadAbleText(
                            textHeader = stringResource(id = R.string.loan_status),
                            textColorHeader = hintTextColor,
                            textValue = loanStatus,
                            textColorValue = appBlack,
                            style = normal14Text400,
                            end = 5.dp, start = 5.dp, top = 20.dp
                        )
                    }
                }

                customer.person?.name?.let { name ->
                    OnlyReadAbleText(
                        textHeader = stringResource(id = R.string.applicant_name),
                        textColorHeader = hintTextColor,
                        textValue = name,
                        textColorValue = appBlack,
                        style = normal14Text400,
                        end = 5.dp, start = 5.dp, top = 20.dp
                    )
                }

                customer.contact?.email?.let { email ->
                    OnlyReadAbleText(
                        textHeader = stringResource(id = R.string.applicant_email),
                        textColorHeader = hintTextColor,
                        textValue = email,
                        textColorValue = appBlack,
                        style = normal14Text400,
                        end = 5.dp, start = 5.dp, top = 20.dp
                    )
                }

                customer.contact?.phone?.let { mobileNumber ->
                    OnlyReadAbleText(
                        textHeader = stringResource(id = R.string.mobile_number),
                        textColorHeader = hintTextColor,
                        textValue = mobileNumber,
                        textColorValue = appBlack,
                        style = normal14Text400,
                        end = 5.dp, start = 5.dp, top = 20.dp
                    )
                }

                loanDetails.itemPrice?.value?.let {
                    OnlyReadAbleText(
                        textHeader = stringResource(id = R.string.total_payable_amount) + "(INR)",
                        textValue = it,
                        textColorHeader = hintTextColor,
                        textColorValue = appBlack,
                        style = normal14Text400,
                        end = 5.dp, start = 5.dp, top = 20.dp
                    )
                }
                OnlyReadAbleText(
                    textHeader = stringResource(id = R.string.loan_amount) + "(INR)",
                    textColorHeader = hintTextColor,
                    textValue = principal,
                    textColorValue = appBlack,
                    style = normal14Text400,
                    end = 5.dp, start = 5.dp, top = 20.dp
                )
            }
        }
    }
}

@Composable
fun convertUTCToLocalDateTime(utcDateTime: String): String {
    val zonedDateTime =
        ZonedDateTime.parse(utcDateTime).withZoneSameInstant(ZoneId.of("Asia/Kolkata"))
    val formatter = DateTimeFormatter.ofPattern("hh:mm a, dd MMM yyyy", Locale.getDefault())

    val remainingTime =
        CommonMethods().getRemainingTime(utcDateTime) ?: CommonMethods.RemainingTime(
            isFuture = false, days = 0, hours = 0, minutes = 0, seconds = 0
        )
    val remainingTimeStr = CommonMethods().timeBufferString(remainingTime)

    val dateTimeStr = zonedDateTime.format(formatter)
    //Sugu2
//    val formatedStr = "$remainingTimeStr\n($dateTimeStr)"
    val formatedStr = "Till - $dateTimeStr"

    return formatedStr
}

@Composable
fun LoanApplicationDetails(loanDetails: OfferResponseItem,context: Context) {
    FullWidthRoundShapedCard(onClick = { }, cardColor = lightishGray) {
        loanDetails.itemTags?.forEach { itemTags ->
            itemTags?.let {

                // Check if display is true
                if (it.display == true) {
                    it.tags.let { tags ->
                        tags.forEach { tag ->
                            val newTitle = CommonMethods().displayFormattedText(tag.key)

                            val displayValue =
                                if (tag.key.contains("cool_off", ignoreCase = true) ||
                                    tag.key.contains("cool off", ignoreCase = true)
                                ) {
                                    convertUTCToLocalDateTime(tag.value?:"")
                                } else {
                                    tag.value
                                }
                            if (tag.key.contains("LINK", ignoreCase = true)) {
                                tag.value?.let { url ->
                                    OnlyClickAbleText(
                                        textHeader = tag.key,
                                        textValue = url,
                                        textColorHeader = hintTextColor,
                                        style = normal14Text400,
                                        textColorValue = azureBlueColor,
                                        onClick = { CommonMethods().openLink(context, url) }
                                    )
                                }
                            } else {
                                OnlyReadAbleText(
                                    textHeader = newTitle,
                                    textValue = displayValue?:"",
                                    style = normal14Text400,
                                    textColorValue = bokaraGrayColor,
                                    textColorHeader = hintTextColor
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
fun RepaymentBottomCommon(
    headerText: String = stringResource(id = R.string.foreclosure),
    text: String = stringResource(id = R.string.are_u_sure_want_to_pay),
    textDescriptor: String = stringResource(id = R.string.may_include_late_charges),
    showTextDescriptor: Boolean = true,
    updateProcessing: Boolean = false, onYesClick: () -> Unit, onNoClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(top = 10.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            ForeClosureAnimator()
            StartingText(
                text = headerText.uppercase(),
                textColor = slateGrayColor,
                start = 30.dp, end = 30.dp, top = 10.dp, bottom = 5.dp,
                style = bold18Text100,
                alignment = Alignment.TopCenter
            )
            StartingText(
                text = text,
                textColor = errorGray,
                start = 30.dp, end = 30.dp, top = 10.dp, bottom = 5.dp,
                style = normal14Text500,
                alignment = Alignment.TopCenter
            )
            if (showTextDescriptor){
                StartingText(
                    text = textDescriptor,
                    textColor = errorGray,
                    start = 30.dp, end = 30.dp, top = 10.dp, bottom = 5.dp,
                    style = normal14Text500,
                    alignment = Alignment.TopCenter
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 20.dp)
                    .fillMaxWidth()
            )
            {
                if (!updateProcessing) {
                    CurvedPrimaryButton(text = stringResource(id = R.string.yes).uppercase())
                    { onYesClick() }
                    CurvedPrimaryButton(text = stringResource(id = R.string.no).uppercase())
                    { onNoClick() }
                }
            }
        }
    }
}

@Composable
fun PaymentHistoryCard(payment: List<OrderPaymentStatusItem?>?) {
    var showDetails by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .clickable { showDetails = !showDetails }
    ) {
        StartingText(
            text = stringResource(id = R.string.payment_history),
            modifier = Modifier.weight(0.7f),
            textColor = appBlack,
            top = 20.dp, start = 20.dp, end = 30.dp, bottom = 0.dp,
            style = normal16Text700,
            textAlign = TextAlign.Start,
        )

        IconButton(modifier = Modifier.weight(0.3f),
            onClick = { showDetails = !showDetails }) {
            Icon(
                imageVector = if (showDetails) {
                    Icons.Default.ArrowDropUp
                } else {
                    Icons.Default.ArrowDropDown
                },
                contentDescription = null
            )
        }
    }
    if (showDetails) {
        FullWidthRoundShapedCard(onClick = { /*TODO*/ }, cardColor = lightishGray) {
            payment?.forEach { payment ->
                val paymentDate =
                    payment?.updatedAt?.let { CommonMethods().displayFormattedDate(it) }
                payment?.params?.amount?.let { amount ->
                    val newTextHeader =
                        CommonMethods().displayFormattedText(payment.time?.label ?: "")

                    HeaderValueInARowWithTextBelowValue(
                        textHeader = newTextHeader,
                        start = 10.dp, end = 20.dp, top = 8.dp, bottom = 0.dp,
                        textColorHeader = appGray,
                        textValue = "₹$amount",
                        textColorValue = appBlack,
                        textBelowValue = "$paymentDate  PAID",
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
fun LoanAgreementDetailsCard(loanDocument: OfferResponseItem, context: Context) {
    var showGroDetails by remember { mutableStateOf(false) }
    Row(modifier = Modifier.clickable { showGroDetails = !showGroDetails }) {
        StartingText(
            modifier = Modifier.weight(0.7f),
//            text = stringResource(id = R.string.loan_agreement),
            text = "Loan agreement",
            textColor = appBlack,
            top = 20.dp, start = 20.dp, end = 30.dp, bottom = 10.dp,
            style = normal16Text700,
            textAlign = TextAlign.Start,
        )

        IconButton(modifier = Modifier.weight(0.3f),
            onClick = { showGroDetails = !showGroDetails }) {
            val imageVector =
                if (showGroDetails) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown
            Icon(imageVector = imageVector, contentDescription = null)
        }
    }

    if (showGroDetails) {
        FullWidthRoundShapedCard(onClick = { /*TODO*/ }, cardColor = lightishGray) {
            loanDocument.documents?.forEach { document ->
                document?.url?.let { loanPdfUrl ->
                    OnlyClickAbleText(
                        textHeader = stringResource(id = R.string.document),
                        textValue = loanPdfUrl,
                        textColorHeader = hintTextColor,
                        style = normal14Text400,
                        textColorValue = azureBlueColor,
                        onClick = { CommonMethods().openLink(context, loanPdfUrl) }
                    )
                }
            }
        }
    }
}

@Composable
fun CancellationTermsCard(loanDocument: OfferResponseItem, context: Context) {
    var showGroDetails by remember { mutableStateOf(false) }
    Column {
        Row(modifier = Modifier.clickable { showGroDetails = !showGroDetails }) {
            StartingText(
                modifier = Modifier.weight(0.7f),
                text = stringResource(id = R.string.cancellation_terms),
                textColor = appBlack,
                top = 20.dp, start = 20.dp, end = 30.dp, bottom = 10.dp,
                style = normal16Text700,
                textAlign = TextAlign.Start,
            )

            IconButton(modifier = Modifier.weight(0.3f),
                onClick = { showGroDetails = !showGroDetails }) {
                Icon(
                    imageVector = if (showGroDetails) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }
        }
        if (showGroDetails) {
            FullWidthRoundShapedCard(onClick = { /*TODO*/ }, cardColor = lightishGray) {
                loanDocument.cancellationTerms?.firstOrNull()?.let { cancellationTerms ->
                    cancellationTerms.cancellationFeePercentage?.let { cancellationFeePercentage ->
                        OnlyReadAbleText(
                            textHeader = stringResource(id = R.string.cancellation_fee),
                            textValue = cancellationFeePercentage,
                            textColorHeader = hintTextColor,
                            textColorValue = bokaraGrayColor,
                            style = normal14Text400,
                            end = 5.dp, start = 5.dp, top = 20.dp
                        )
                    }
                    cancellationTerms.externalRefUrl?.let { privacy ->
                        OnlyClickAbleText(
                            textHeader = stringResource(id = R.string.privacy),
                            textValue = privacy,
                            textColorHeader = hintTextColor,
                            textColorValue = azureBlueColor,
                            style = normal14Text400,
                            end = 5.dp, start = 5.dp, top = 20.dp,
                            onClick = { CommonMethods().openLink(context, privacy) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoanSummary(offer: OfferResponseItem) {
    var showGroDetails by remember { mutableStateOf(false) }

    Row(modifier = Modifier
        .clickable { showGroDetails = !showGroDetails }) {
        StartingText(
            modifier = Modifier.weight(0.7f), style = normal16Text700, textAlign = TextAlign.Start,
            text = stringResource(id = R.string.loan_summary),
            textColor = appBlack, top = 20.dp, start = 20.dp, end = 30.dp, bottom = 10.dp,
        )

        IconButton(modifier = Modifier.weight(0.3f),
            onClick = { showGroDetails = !showGroDetails }) {
            Icon(
                imageVector = if (showGroDetails) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        }
    }
    if (showGroDetails) {
        FullWidthRoundShapedCard(onClick = { /*TODO*/ }, cardColor = lightishGray) {

            offer.quoteBreakUp?.forEach { quoteBreakUp ->
                quoteBreakUp?.let {
                    it.title?.let { title ->
                        val newTitle = CommonMethods().displayFormattedText(title)
                        it.value?.let { description ->
                            val currency = it.currency?.let { " ($it)" } ?: ""
                            OnlyReadAbleText(
                                textHeader = newTitle + currency, textValue = description,
                                style = normal14Text400, textColorValue = bokaraGrayColor,
                                textColorHeader = hintTextColor, start = 5.dp, end = 5.dp,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RepaymentBottomPart(
    loanDetails: OfferResponseItem, scope: CoroutineScope, bottomSheetState: ModalBottomSheetState,
    fromFlow: String, navController: NavHostController,context: Context,orderId: String,
    getOrderPaymentStatusViewModel: GetOrderPaymentStatusViewModel,
    loanAgreementViewModel: LoanAgreementViewModel, loanType: String
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        loanDetails.fulfillments?.forEach { fulfilment ->
            fulfilment?.state?.let { state ->
                state.descriptor?.name?.let { loanStatus ->
                    if (!loanStatus.contains("Closed", ignoreCase = true) && loanStatus.contains("Disbursed",ignoreCase = true)) {
                        CurvedPrimaryButtonMultipleInRow(
                            text = stringResource(id = R.string.repayment).uppercase(),
                            style = normal16Text400,
                            start = 0.dp, end = 0.dp,
                            modifier = Modifier
                                .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp)
                                .weight(1f)
                                .height(80.dp)
                        ) { scope.launch { bottomSheetState.show() } }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        CurvedPrimaryButtonMultipleInRow(
            text = stringResource(id = R.string.home).uppercase(),
            style = normal16Text400,
            start = 0.dp, end = 0.dp, top = 5.dp, bottom = 5.dp,
            modifier = Modifier
                .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp)
                .weight(1f)
                .height(80.dp)
        ) { navigateApplyByCategoryScreen(navController = navController) }

        Spacer(modifier = Modifier.height(8.dp))

        CurvedPrimaryButtonMultipleInRow(
            text = stringResource(id = R.string.loan_status).uppercase(),
            style = normal16Text400,
            start = 0.dp, end = 0.dp, top = 10.dp, bottom = 10.dp,
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, bottom = 10.dp, top = 10.dp)
                .weight(1f)
                .height(80.dp)
        ) {
            loanAgreementViewModel.status(
                loanType = loanType, context = context, orderId = orderId
            )

            // Fetch payment status
            getOrderPaymentStatusViewModel.getOrderPaymentStatus(
                loanType = loanType, loanId = orderId, context = context
            )
        }
    }
}

@Composable
fun RepaymentHeaderPart(
    checkOrderIssueResponse: CheckOrderIssueModel?, loanDetails: OfferResponseItem,
    navController: NavHostController, fromFlow: String,
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 0.dp)
    ) {
        // Repayment schedule
        StartingText(
            text = stringResource(id = R.string.repayment_schedule), textColor = appBlack,
            modifier = Modifier.weight(0.7f), top = 40.dp, style = normal16Text700,
            textAlign = TextAlign.Start,
        )


        val summary = checkOrderIssueResponse?.data?.data?.firstOrNull()?.summary

        val buttonText = if (summary == null) {
            stringResource(id = R.string.raise_issue)
        } else {
            stringResource(id = R.string.view_issue)
        }

        ClickableText(
            text = buttonText, modifier = Modifier.weight(0.28f), textAlign = TextAlign.Center,
            backgroundColor = Color.White, borderColor = azureBlueColor, color = azureBlueColor
        ) {
            loanDetails.fulfillments?.forEach { fulfilment ->
                fulfilment?.state?.let { state ->
                    state.descriptor?.name?.let { loanState ->
                        loanDetails.id?.let { orderId ->
                            loanDetails.providerId?.let { providerId ->
                                if (summary == null) {
                                    navigateToCreateIssueScreen(
                                        navController = navController, orderId = orderId,
                                        providerId = providerId, orderState = loanState,
                                        fromFlow = fromFlow
                                    )
                                } else {
                                    navigateToIssueListScreen(
                                        navController = navController, orderId = orderId,
                                        fromFlow = fromFlow, providerId = providerId,
                                        loanState = loanState, fromScreen = "Loan Detail"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MissedEmi(
    updateProcessing: Boolean, navController: NavHostController, updateProcessed: Boolean,
    loanDetails: OfferResponseItem, loanAgreementViewModel: LoanAgreementViewModel,
    scope: CoroutineScope, bottomSheetStateValue: ModalBottomSheetState, fromFlow: String,
    updatedLoanAgreement: UpdateLoanAgreement?, context: Context, orderId: String, loanType: String
) {


    if (updateProcessing) {
        CenterProgressFixedHeight()
    }

    if (updateProcessed) {
        MissedEmiPaymentResponseHandling(
            updatedLoanAgreement = updatedLoanAgreement, loanDetails = loanDetails,
            navController = navController, fromFlow = fromFlow, orderId = orderId
        )
    }
    var delayFound = false
    loanDetails.payments?.forEach { payment ->
        if (payment?.type == "POST_FULFILLMENT" && payment.status == "DELAYED") {
            amount_to_be_paid = payment.params?.amount ?: ""
            delayFound = true
            val missingPayment = "Are you sure you want to pay ₹${payment.params?.amount} ? "
//                    "\n *this amount may include late fee charges"
            RepaymentBottomCommon(
                updateProcessing = updateProcessing,
                headerText = stringResource(id = R.string.missed_emi_payment),
                text = missingPayment,

                onYesClick = {
                    loanDetails.id?.let { id ->
                        loanAgreementViewModel.updateLoanAgreementApi(
                            context, UpdateLoanBody(
                                subType = "MISSED_EMI_PAYMENT", id = id,
                                amount = "", loanType = loanType
                            )
                        )
                    }
                },
                onNoClick = { scope.launch { bottomSheetStateValue.hide() } }
            )
        }
    }
    if (!delayFound) {
        CommonMethods().toastMessage(context, "There is No Missed Emi")
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ForeClosure(
    loanDetails: OfferResponseItem, updatedLoanAgreement: UpdateLoanAgreement?, fromFlow: String,
    updateProcessed: Boolean, updateProcessing: Boolean, context: Context, orderId: String,
    navController: NavHostController, loanAgreementViewModel: LoanAgreementViewModel,
    scope: CoroutineScope, bottomSheetStateValue: ModalBottomSheetState, loanType: String
) {
    val fullAmount = loanDetails.itemPrice?.value
    if (updateProcessing) {
        CenterProgressFixedHeight()
    }

    if (updateProcessed) {
        ForeClosureResponseHandling(
            fullAmount = fullAmount, navController = navController, fromFlow = fromFlow,
            updatedLoanAgreement = updatedLoanAgreement, orderId = orderId
        )
    }

    //Sugu
//    fullAmount?.let { amount ->
    principal?.let { amount ->
        amount_to_be_paid = amount
        val remainingTime = CommonMethods().getRemainingTime(coolOffPeriodDate)
        val timeBuffer = remainingTime?.let { CommonMethods().timeBufferString(it) }
        if (remainingTime?.isFuture == true) {
            RepaymentBottomCommon(
                text = "You are within the cool off period.\n" +
                        "No penalty will be charged for Foreclosure.\n" +
                        "Remaining cool off period: $timeBuffer \n" +
                        "Proceed with Foreclosure with no penalty ? ",
                showTextDescriptor = false,
                updateProcessing = updateProcessing,
                onYesClick = {
                    loanDetails.id?.let { id ->
                        loanAgreementViewModel.updateLoanAgreementApi(
                            context = context, updateLoanBody = UpdateLoanBody(
                                subType = "FORECLOSURE", id = id, amount = "", loanType = loanType
                            )
                        )
                    }

                },
                onNoClick = { scope.launch { bottomSheetStateValue.hide() } }
            )
        } else {
            RepaymentBottomCommon(
                text = "You are past the cool off period.\n" +
                        "Penalty will be charged for Foreclosure.\n" +
                        "Please check loan details.\n" +
                        "Proceed with Foreclosure with penalty ? ",
                updateProcessing = updateProcessing,
                onYesClick = {
                    loanDetails.id?.let { id ->
                        loanAgreementViewModel.updateLoanAgreementApi(
                            context, UpdateLoanBody(
                                subType = "FORECLOSURE", id = id, amount = "", loanType = loanType
                            )
                        )
                    }
                },
                onNoClick = { scope.launch { bottomSheetStateValue.hide() } }
            )
        }
    }
}

@Composable
fun MissedEmiPaymentResponseHandling(
    updatedLoanAgreement: UpdateLoanAgreement?, loanDetails: OfferResponseItem,
    navController: NavHostController, fromFlow: String, orderId: String
) {
    loanDetails.payments?.forEach {
        if (it?.type == "POST_FULFILLMENT" && it.status == "DELAYED") {
            val paymentStatusText =
                "Missed EMI of ₹$amount_to_be_paid successfully processed"
            updatedLoanAgreement?.data?.let { data ->
                data.updatedObject?.let { updatedLoan ->
                    updatedLoan.payments?.get(0)?.let { payment ->
                        payment.url?.let { paymentUrl ->
                            navigateToPrePaymentWebViewScreen(
                                navController = navController, orderId = orderId,
                                headerText = paymentUrl, status = paymentStatusText,
                                fromFlow = fromFlow
                            )
                        }
                    }
                }
            }
            return@forEach
        }
    }
}

@Composable
fun ForeClosureResponseHandling(
    fullAmount: String?, navController: NavHostController,
    updatedLoanAgreement: UpdateLoanAgreement?, fromFlow: String, orderId: String
) {
    fullAmount?.let {
        val paymentStatusText = "ForeClosure of ₹$amount_to_be_paid successfully processed"
        updatedLoanAgreement?.data?.let {
            it.updatedObject?.let { updatedLoan ->
                updatedLoan.payments?.get(0)?.let { payment ->
                    payment.url?.let { paymentUrl ->
                        navigateToPrePaymentWebViewScreen(
                            navController = navController, orderId = orderId, fromFlow = fromFlow,
                            headerText = paymentUrl, status = paymentStatusText,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PrePartPaymentResponseHandle(
    updatedLoanAgreement: UpdateLoanAgreement?, selectedOption: String, fromFlow: String,
    navController: NavHostController, prePartPaymentAmount: String, orderId: String
) {
    var paymentStatusText = ""
    if (selectedOption == "PRE_PART_PAYMENT")
        paymentStatusText = "Repayment of ₹$prePartPaymentAmount successfully processed"
    else if (selectedOption == "FORECLOSURE" || selectedOption == "MISSED_EMI_PAYMENT")
        paymentStatusText = "Repayment of $amount_to_be_paid successfully processed"
    updatedLoanAgreement?.data?.let agreement@{
        it.updatedObject?.let { updatedLoan ->
            updatedLoan.payments?.forEach {
                it?.let { payment ->
                payment.url?.let { paymentUrl ->
                    navigateToPrePaymentWebViewScreen(
                        navController = navController, orderId = orderId, headerText = paymentUrl,
                        status = paymentStatusText, fromFlow = fromFlow
                    )
                    return@agreement
                }
            }
            }
        }
    }
}

@Composable
fun EmiDetail(loanDetails: OfferResponseItem) {
    var showEmiDetails by remember { mutableStateOf(false) }
    Row(modifier = Modifier.clickable { showEmiDetails = !showEmiDetails }) {
        StartingText(
            modifier = Modifier.weight(0.7f), text = stringResource(id = R.string.emi_schedule),
            textColor = appBlack, top = 20.dp, start = 20.dp, end = 30.dp, bottom = 10.dp,
            style = normal16Text700, textAlign = TextAlign.Start,
        )

        IconButton(modifier = Modifier.weight(0.3f),
            onClick = { showEmiDetails = !showEmiDetails }) {
            Icon(
                imageVector = if (showEmiDetails) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        }
    }
    if (showEmiDetails) {
        Row(modifier = Modifier.fillMaxWidth()) {
            FullWidthRoundShapedCard(
                onClick = { },
                start = 0.dp, end = 15.dp, top = 0.dp,
                bottom = 0.dp, cardColor = lightishGray
            ) {
                Column(
                    modifier = Modifier
                        .padding(0.dp)
                        .fillMaxWidth()
                ) {
                    TableRow(
                        emiNumber = "No", dueDate = "Due Date", amount = "Amount(INR)",
                        status = "Status", isTitleRow = true
                    )
                }
                loanDetails.payments?.forEach { payment ->
                    if (payment?.type == "POST_FULFILLMENT") {
                        EMIDetailsTable(emiItems = payment, showEmiDetails = showEmiDetails)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun LoanDetailScreenPreview() {

    val responseData: String = "{\n" +
            "      \"_id\": \"e99d8c34-673d-4c40-9f4b-f779f984b56e\",\n" +
            "      \"provider_id\": \"6a58b3e7-6c4b-4987-b7b5-62fbc4dec263\",\n" +
            "      \"provider_descriptor\": {\n" +
            "        \"images\": [\n" +
            "          {\n" +
            "            \"url\": \"https://ondcuatkbl.ignosis.ai:8880/kbl_logo.png\",\n" +
            "            \"size_type\": \"sm\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"name\": \"Karnataka Bank\",\n" +
            "        \"short_desc\": \"Your Family Bank Across India\",\n" +
            "        \"long_desc\": \"Karnataka Bank Limited is a leading Indian private sector bank based in Mangalore. It is an 'A' Class Scheduled Commercial Bank with a network of 913 branches, 1188 ATMs & Cash recyclers and 587 e-lobbies/mini e-lobbies across 22 states and 2 union territories with 8,652 employees and over 11 million customers throughout the country.Being a leading bank, we have created a wide selection of tailored goods and services appropriate for any market, industry, or perceived need, whether it be personal or business-related. These consist of deposit options, borrowing capabilities, offering the best returns on excess money, and assistance with international transactions. In order to provide goods and services for your advantage and to establish a productive long-term partnership, we have made use of the most recent information technology. Above all, though, technology meets and exceeds your service expectations both now and in the future.\"\n" +
            "      },\n" +
            "      \"provider_tags\": [\n" +
            "        {\n" +
            "          \"name\": \"Contact Info\",\n" +
            "          \"tags\": {\n" +
            "            \"Gro name\": \"Shri Suhas M, Chief Manager\",\n" +
            "            \"Gro email\": \"info@ktkbank.com\",\n" +
            "            \"Gro contact number\": \"18004251444\",\n" +
            "            \"Gro Designation\": \"Chief Manager & Nodal Grievance Redressal Officer, Technology & Digital Hub, Digital Centre of Excellence [DCoE]\",\n" +
            "            \"Gro Address\": \"Artisane, 32, 1st Main Road, 3rd Phase, J P Nagar, Bengaluru 560 078, Karnataka.\",\n" +
            "            \"Customer support link\": \"https://karnatakabank.com/etms/complaint.php\",\n" +
            "            \"Customer support contact\": \"1800 425 1444\",\n" +
            "            \"Customer support email\": \"info@ktkbank.com\"\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": \"Lsp Info\",\n" +
            "          \"tags\": {\n" +
            "            \"Lsp name\": \"Karnataka Bank\",\n" +
            "            \"Lsp email\": \"info@ktkbank.com\",\n" +
            "            \"Lsp contact number\": \"18004251444\",\n" +
            "            \"Lsp Address\": \"No.109/1, Old Madras Road, Opp.adarsha Theatre, Old Madras Rd, Halasuru, Murphy Town, Karnataka 560008\"\n" +
            "          }\n" +
            "        }\n" +
            "      ],\n" +
            "      \"item_id\": \"6b58b3e7-6c4b-4987-b7b5-62fbc4dec263\",\n" +
            "      \"item_descriptor\": {\n" +
            "        \"name\": \"Personal Loan\",\n" +
            "        \"code\": \"PERSONAL_LOAN\"\n" +
            "      },\n" +
            "      \"item_price\": {\n" +
            "        \"currency\": \"INR\",\n" +
            "        \"value\": \"63766.00\"\n" +
            "      },\n" +
            "      \"item_tags\": [\n" +
            "        {\n" +
            "          \"name\": \"Loan Information\",\n" +
            "          \"display\": true,\n" +
            "          \"tags\": {\n" +
            "            \"Foreclosure Penalty\": \"4% of POS if loan closed within 12 months and 3% of POS if loan is closed at or after 12 months\",\n" +
            "            \"Interest rate conversion charges\": \"Not Applicable\",\n" +
            "            \"Delayed payments penalty\": \"2% (p.m.)\",\n" +
            "            \"Other penalty charges\": \"0\",\n" +
//            "            \"cool off period\": \"P3D Days\",\n" +
            "            \"cool_off_period\": \"2024-07-15T08:00:46.301Z\",\n" +
            "            \"Terms and Conditions\": \"https://karnatakabank.com/privacy\",\n" +
            "            \"Interest Rate\": \"11.0%\",\n" +
            "            \"Loan Term\": \"12 Months\",\n" +
            "            \"Interest rate type\": \"FIXED\",\n" +
            "            \"Application Fees\": \"118.00 INR\",\n" +
            "            \"Annual Percentage Rate\": \"11.41%\",\n" +
            "            \"Repayment Frequency\": \"MONTHLY\",\n" +
            "            \"Number of installments of repayment\": \"12\",\n" +
            "            \"Installment amount\": \"5304\"\n" +
            "          }\n" +
            "        }\n" +
            "      ],\n" +
            "      \"quote_id\": \"65ece19d70b54f84a4fc7da5130338e6\",\n" +
            "      \"quote_price\": {\n" +
            "        \"currency\": \"INR\",\n" +
            "        \"value\": \"63766.00\"\n" +
            "      },\n" +
            "      \"quote_breakup\": [\n" +
            "        {\n" +
            "          \"title\": \"PRINCIPAL\",\n" +
            "          \"value\": \"60000\",\n" +
            "          \"currency\": \"INR\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"title\": \"INTEREST\",\n" +
            "          \"value\": \"3648\",\n" +
            "          \"currency\": \"INR\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"title\": \"PROCESSING_FEE\",\n" +
            "          \"value\": \"118.00\",\n" +
            "          \"currency\": \"INR\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"title\": \"INSURANCE_CHARGES\",\n" +
            "          \"value\": \"0\",\n" +
            "          \"currency\": \"INR\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"title\": \"OTHER_CHARGES\",\n" +
            "          \"value\": \"0\",\n" +
            "          \"currency\": \"INR\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"title\": \"NET_DISBURSED_AMOUNT\",\n" +
            "          \"value\": \"59882.00\",\n" +
            "          \"currency\": \"INR\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"title\": \"OTHER_UPFRONT_CHARGES\",\n" +
            "          \"value\": \"0\",\n" +
            "          \"currency\": \"INR\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"payments\": [\n" +
            "        {\n" +
            "          \"id\": \"0\",\n" +
            "          \"type\": \"ON_ORDER\",\n" +
            "          \"status\": \"NOT-PAID\",\n" +
            "          \"collected_by\": \"BPP\",\n" +
            "          \"tags\": [\n" +
            "            {\n" +
            "              \"list\": [\n" +
            "                {\n" +
            "                  \"descriptor\": {\n" +
            "                    \"code\": \"BUYER_FINDER_FEES_TYPE\"\n" +
            "                  },\n" +
            "                  \"value\": \"percent-annualized\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"descriptor\": {\n" +
            "                    \"code\": \"BUYER_FINDER_FEES_PERCENTAGE\"\n" +
            "                  },\n" +
            "                  \"value\": \"1\"\n" +
            "                }\n" +
            "              ],\n" +
            "              \"descriptor\": {\n" +
            "                \"code\": \"BUYER_FINDER_FEES\"\n" +
            "              },\n" +
            "              \"display\": false\n" +
            "            },\n" +
            "            {\n" +
            "              \"list\": [\n" +
            "                {\n" +
            "                  \"descriptor\": {\n" +
            "                    \"code\": \"SETTLEMENT_WINDOW\"\n" +
            "                  },\n" +
            "                  \"value\": \"P30D\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"descriptor\": {\n" +
            "                    \"code\": \"SETTLEMENT_BASIS\"\n" +
            "                  },\n" +
            "                  \"value\": \"INVOICE_RECEIPT\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"descriptor\": {\n" +
            "                    \"code\": \"MANDATORY_ARBITRATION\"\n" +
            "                  },\n" +
            "                  \"value\": \"TRUE\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"descriptor\": {\n" +
            "                    \"code\": \"COURT_JURISDICTION\"\n" +
            "                  },\n" +
            "                  \"value\": \"New Delhi\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"descriptor\": {\n" +
            "                    \"code\": \"STATIC_TERMS\"\n" +
            "                  },\n" +
            "                  \"value\": \"https://karnatakabank.com/privacy\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"descriptor\": {\n" +
            "                    \"code\": \"OFFLINE_CONTRACT\"\n" +
            "                  },\n" +
            "                  \"value\": \"false\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"descriptor\": {\n" +
            "                    \"code\": \"SETTLEMENT_AMOUNT\"\n" +
            "                  },\n" +
            "                  \"value\": \"1159\"\n" +
            "                }\n" +
            "              ],\n" +
            "              \"descriptor\": {\n" +
            "                \"code\": \"SETTLEMENT_TERMS\"\n" +
            "              },\n" +
            "              \"display\": false\n" +
            "            }\n" +
            "          ]\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": \"1\",\n" +
            "          \"type\": \"POST_FULFILLMENT\",\n" +
            "          \"status\": \"NOT-PAID\",\n" +
            "          \"params\": {\n" +
            "            \"amount\": \"5304.00\",\n" +
            "            \"currency\": \"INR\"\n" +
            "          },\n" +
            "          \"time\": {\n" +
            "            \"label\": \"INSTALLMENT\",\n" +
            "            \"range\": {\n" +
            "              \"start\": \"2024-06-19T00:00:00.000Z\",\n" +
            "              \"end\": \"2024-07-19T00:00:00.000Z\"\n" +
            "            }\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": \"2\",\n" +
            "          \"type\": \"POST_FULFILLMENT\",\n" +
            "          \"status\": \"DELAYED\",\n" +
            "          \"params\": {\n" +
            "            \"amount\": \"5304.00\",\n" +
            "            \"currency\": \"INR\"\n" +
            "          },\n" +
            "          \"time\": {\n" +
            "            \"label\": \"INSTALLMENT\",\n" +
            "            \"range\": {\n" +
            "              \"start\": \"2024-07-19T00:00:00.000Z\",\n" +
            "              \"end\": \"2024-08-19T00:00:00.000Z\"\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      ],\n" +
            "      \"cancellation_terms\": [\n" +
            "        {\n" +
            "          \"fulfillment_state\": \"SANCTIONED\",\n" +
            "          \"cancellation_fee_percentage\": \"4% of POS if loan is cancelled within 12 months and 3% of POS if loan is cancelled at or after 12 months\",\n" +
            "          \"external_ref_url\": \"https://karnatakabank.com/privacy\",\n" +
            "          \"external_ref_url_mimetype\": \"text/html\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"fulfillment_state\": \"DISBURSED\",\n" +
            "          \"cancellation_fee_percentage\": \"4% of POS if loan is cancelled within 12 months and 3% of POS if loan is cancelled at or after 12 months\",\n" +
            "          \"external_ref_url\": \"https://karnatakabank.com/privacy\",\n" +
            "          \"external_ref_url_mimetype\": \"text/html\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"fulfillments\": [\n" +
            "        {\n" +
            "          \"customer\": {\n" +
            "            \"person\": {\n" +
            "              \"name\": \"Sandhya vis\"\n" +
            "            },\n" +
            "            \"contact\": {\n" +
            "              \"phone\": \"9611041286\",\n" +
            "              \"email\": \"vsandhya5950@gmail.com\"\n" +
            "            }\n" +
            "          },\n" +
            "          \"state\": {\n" +
            "            \"descriptor\": {\n" +
            "              \"name\": \"SANCTIONED\",\n" +
            "              \"code\": \"SANCTIONED\"\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      ],\n" +
            "      \"documents\": [\n" +
            "        {\n" +
            "          \"descriptor\": {\n" +
            "            \"name\": \"Loan Agreement Document\",\n" +
            "            \"code\": \"LOAN_AGREEMENT\",\n" +
            "            \"short_desc\": \"Download Loan Agreement document here\",\n" +
            "            \"long_desc\": \"This is just a loan agreement link for reference.\"\n" +
            "          },\n" +
            "          \"url\": \"https://ondcuatkbl.ignosis.ai/lender-bridge/ui/api/public/kbl-uat/esign/signed/tm1718793197656r14de/loan-agreement.pdf\",\n" +
            "          \"mime_type\": \"application/pdf\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"txn_id\": \"acd12e39-3902-58cc-b5ef-49ef2141361e\",\n" +
            "      \"msg_id\": \"59ac5cc3-cd35-5281-9db4-2cd1bbdd166d\",\n" +
            "      \"bpp_id\": \"ondcuatkbl.ignosis.ai\",\n" +
            "      \"bpp_uri\": \"https://ondcuatkbl.ignosis.ai/lender-bridge/ondc/kbl-uat\"\n" +
            "    }"

    RepaymentScheduleScreen(
        navController = NavHostController(LocalContext.current),
        orderId = "e99d8c34-673d-4c40-9f4b-f779f984b56e", fromFlow = "Personal"
    )
}
