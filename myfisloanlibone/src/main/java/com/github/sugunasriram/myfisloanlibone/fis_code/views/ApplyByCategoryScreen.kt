package com.github.sugunasriram.myfisloanlibone.fis_code.views

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CenterProgress
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CenteredMoneyImage
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CurvedPrimaryButtonFull
import com.github.sugunasriram.myfisloanlibone.fis_code.components.InnerScreenWithHamburger
import com.github.sugunasriram.myfisloanlibone.fis_code.components.RegisterText
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToLoanProcessScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToPersonaLoanScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.UserStatus
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appDarkTeal
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.bold36Text700
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.UserStatusViewModel
import com.github.sugunasriram.myfisloanlibone.fis_code.views.invalid.UnexpectedErrorScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.webview.SearchWebView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateSignInPage
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToBankKycVerificationScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToFormRejectedScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToGstInvoiceLoanScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToLoanOffersScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToUpdateProfileScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToWebViewFlowOneScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.ProfileResponse
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.personaLoan.Offer
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.gst.GstSearchData
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.gst.GstSearchResponse
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.personaLoan.SearchModel
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.personaLoan.SearchResponseModel
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.auth.RegisterViewModel
import com.github.sugunasriram.myfisloanlibone.fis_code.views.webview.NavigateToWebView
import kotlinx.serialization.json.Json


@Composable
fun ApplyByCategoryScreen(navController: NavHostController) {

    val userStatusViewModel: UserStatusViewModel = viewModel()
    val registerViewModel: RegisterViewModel = viewModel()

    val showInternetScreen by userStatusViewModel.showInternetScreen.observeAsState(false)
    val showTimeOutScreen by userStatusViewModel.showTimeOutScreen.observeAsState(false)
    val showServerIssueScreen by userStatusViewModel.showServerIssueScreen.observeAsState(false)
    val unexpectedErrorScreen by userStatusViewModel.unexpectedError.observeAsState(false)
    val unAuthorizedUser by userStatusViewModel.unAuthorizedUser.observeAsState(false)
    val middleLoan by userStatusViewModel.middleLoan.observeAsState(false)
    val showLoader by userStatusViewModel.showLoader.observeAsState(false)
    val errorMessage by userStatusViewModel.errorMessage.collectAsState()

    val checkingStatus by userStatusViewModel.checkingStatus.collectAsState()
    val checked by userStatusViewModel.checked.collectAsState()
    val userStatus by userStatusViewModel.userStatus.collectAsState()

    val navigationToSignIn by userStatusViewModel.navigationToSignIn.collectAsState()
    val userDetails by registerViewModel.getUserResponse.collectAsState()
    val userDetailsAPILoading by registerViewModel.inProgress.collectAsState()

    val activity = LocalContext.current as? Activity
    val context = LocalContext.current

    BackHandler { activity?.finish() }
    when {
        navigationToSignIn -> navigateSignInPage(navController)
        showInternetScreen -> CommonMethods().ShowInternetErrorScreen(navController)
        showTimeOutScreen -> CommonMethods().ShowTimeOutErrorScreen(navController)
        showServerIssueScreen -> CommonMethods().ShowServerIssueErrorScreen(navController)
        unexpectedErrorScreen -> CommonMethods().ShowUnexpectedErrorScreen(navController)
        unAuthorizedUser -> CommonMethods().ShowUnAuthorizedErrorScreen(navController)
        middleLoan -> CommonMethods().ShowNoResponseFormLendersScreen(navController)
        else -> {
            SelectingFlow(
                checkingStatus = checkingStatus, navController = navController, context = context,
                userStatus = userStatus, userStatusViewModel = userStatusViewModel,
                checked = checked,showLoader = showLoader,errorMessage = errorMessage,
                userDetails=userDetails , userDetailsAPILoading = userDetailsAPILoading
            )
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SelectingFlow(
    checkingStatus: Boolean, navController: NavHostController, userStatus: UserStatus?,
    userStatusViewModel: UserStatusViewModel, context: Context, checked: Boolean,
    showLoader: Boolean,errorMessage: String, userDetails: ProfileResponse?,userDetailsAPILoading : Boolean
) {
    var setFlow by remember { mutableStateOf("") }
    val  scope = rememberCoroutineScope()

    var triggerApi by remember { mutableStateOf(true) }
    triggerApi = showLoader
    var apiCalled by remember { mutableStateOf(true) }

    if (checkingStatus) {
        CenterProgress()
    } else if (triggerApi){
        if (userStatus?.data?.data != null || userStatus?.data?.url != null){
            when {
                setFlow.equals("Personal Loan", ignoreCase = true) -> {
                    PersonalDecidedFlow(
                        status = userStatus, navController = navController, fromFlow = setFlow
                    )
                }

                setFlow.equals("Invoice Loan", ignoreCase = true) -> {
                    InvoiceDecidedFlow(
                        status = userStatus, navController = navController, fromFlow = setFlow
                    )
                }

                setFlow.equals("Purchase Finance", ignoreCase = true) -> {
                    PurchaseDecidedFlow(
                        status = userStatus, navController = navController, fromFlow = setFlow
                    )
                }
            }
        } else {
            if(apiCalled){
                scope.launch {
                    delay(60000)
                    userStatusViewModel.getUserStatus(loanType = "PERSONAL_LOAN", context = context)
                    apiCalled = false
                }
            }

            UnexpectedErrorScreen(
                navController = navController,
                errorMsgShow = false, errorText = stringResource(id =R.string.request_is_still),
                errorMsg = stringResource(id = R.string.middle_loan_error_message),
                onClick = {
                    userStatusViewModel.getUserStatus(
                        loanType = "PERSONAL_LOAN", context = context
                    )
                }
            )
        }

    } else {
        if (checked) {
            when {
                setFlow.equals("Personal Loan", ignoreCase = true) -> {
                    PersonalDecidedFlow(
                        status = userStatus, navController = navController, fromFlow = setFlow
                    )
                }

                setFlow.equals("Invoice Loan", ignoreCase = true) -> {
                    InvoiceDecidedFlow(
                        status = userStatus, navController = navController, fromFlow = setFlow
                    )
                }

                setFlow.equals("Purchase Finance", ignoreCase = true) -> {
                    PurchaseDecidedFlow(
                        status = userStatus, navController = navController, fromFlow = setFlow
                    )
                }
            }
        } else {
            LoanSelectionScreen(
                navController = navController,
                onLoanSelected = { loanType ->
                    setFlow = loanType
                    when (loanType) {
                        "Invoice Loan" -> {
                            val requiredFields = listOf(
                                userDetails?.data?.firstName,
                                userDetails?.data?.lastName,
                                userDetails?.data?.email,
                                userDetails?.data?.dob,
                                userDetails?.data?.mobileNumber,
                                userDetails?.data?.panNumber,
                                userDetails?.data?.gender,
                                userDetails?.data?.employmentType,
                                userDetails?.data?.companyName,
                                userDetails?.data?.address1,
                                userDetails?.data?.city1,
                                userDetails?.data?.pincode1,
                            )
                            if (requiredFields.any { it.isNullOrEmpty() } && !userDetailsAPILoading) {
                                navigateToUpdateProfileScreen(navController,fromFlow = loanType)
                                CommonMethods().toastMessage(
                                    context,
                                    "Please update your profile details"
                                )
                            } else if (userDetails?.data?.udyamNumber.isNullOrEmpty()) {
                                navigateToUpdateProfileScreen(navController, fromFlow = loanType)
                                CommonMethods().toastMessage(context, "Update UDYAM number")
                            }else{
                                userStatusViewModel.getUserStatus(
                                    loanType = "INVOICE_BASED_LOAN", context = context
                                )
                            }

                        }

//                        "Invoice Loan" ->
//                            navigateToLoanProcessScreen(
//                            responseItem = "No Need", offerId = "1234", fromFlow = setFlow,
//                            navController = navController, statusId = 9, transactionId = "Sugu"
//                        )

                        "Personal Loan" ->{
                            val requiredFields = listOf(
                                userDetails?.data?.firstName,
                                userDetails?.data?.lastName,
                                userDetails?.data?.email,
                                userDetails?.data?.officialEmail,
                                userDetails?.data?.mobileNumber,
                                userDetails?.data?.dob,
                                userDetails?.data?.panNumber,
                                userDetails?.data?.gender,
                                userDetails?.data?.employmentType,
                                userDetails?.data?.companyName,
                                userDetails?.data?.address1,
                                userDetails?.data?.city1,
                                userDetails?.data?.pincode1,
                            )


                            if (requiredFields.any { it.isNullOrEmpty() } && !userDetailsAPILoading) {
                                navigateToUpdateProfileScreen(navController, fromFlow = loanType)
                                CommonMethods().toastMessage(
                                    context,
                                    "Please update your profile details"
                                )
                            } else {
                                userStatusViewModel.getUserStatus(
                                    loanType = "PERSONAL_LOAN", context = context
                                )

                            }
                        }

                        "Purchase Finance" -> {
                            val requiredFields = listOf(
                                userDetails?.data?.firstName,
                                userDetails?.data?.lastName,
                                userDetails?.data?.email,
                                userDetails?.data?.dob,
                                userDetails?.data?.mobileNumber,
                                userDetails?.data?.panNumber,
                                userDetails?.data?.gender,
                                userDetails?.data?.employmentType,
                                userDetails?.data?.companyName,
                                userDetails?.data?.address1,
                                userDetails?.data?.city1,
                                userDetails?.data?.pincode1,
                            )
                            if (requiredFields.any { it.isNullOrEmpty() }  && !userDetailsAPILoading) {
                                navigateToUpdateProfileScreen(navController,fromFlow = loanType)
                                CommonMethods().toastMessage(
                                    context,
                                    "Please update your profile details"
                                )
                            } else {
                                userStatusViewModel.getUserStatus(
                                    loanType = "PURCHASE_FINANCE", context = context
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun LoanSelectionScreen(
    navController: NavHostController, onLoanSelected: (String) -> Unit
) {
    InnerScreenWithHamburger(isSelfScrollable = false, navController = navController) {
        CenteredMoneyImage(
            image = R.drawable.home_image, start = 20.dp, end = 20.dp,
            imageSize = 280.dp, top = 5.dp, contentScale = ContentScale.Fit
        )

        RegisterText(
            text = stringResource(id = R.string.apply_buy_category),
            top = 25.dp, textColor = appDarkTeal, style = bold36Text700
        )

        CurvedPrimaryButtonFull(
            text = stringResource(id = R.string.gst_invoice_loan),
            modifier = Modifier.padding(horizontal = 30.dp, vertical = 20.dp)
        ) {
            onLoanSelected("Invoice Loan")
        }

//        Spacer(modifier = Modifier.height(48.dp))
        CurvedPrimaryButtonFull(
            text = stringResource(id = R.string.personal_loan),
            modifier = Modifier.padding(horizontal = 30.dp, vertical = 20.dp)
        ) {
            onLoanSelected("Personal Loan")
        }

        CurvedPrimaryButtonFull(
            text = stringResource(id = R.string.purchase_finance),
            modifier = Modifier.padding(start = 30.dp, end = 30.dp, top = 20.dp, bottom = 20.dp)
        ) {
            onLoanSelected("Purchase Finance")
        }
    }
}

@Preview
@Composable
fun PersonalDecidedFlowPreview(){
    val fromFlow = "Personal Loan"
    val userStatusResponse = "{\n" +
            "    \"status\": true,\n" +
            "    \"data\": {\n" +
            "           \"txn_id\": \"41129b37-4ee9-5841-a86b-e3cf6ea790ec\" \n" +
            "        \"data\": [\n" +
            "            {\n" +
            "                \"_id\": \"6010b8c4-2d0d-50da-b309-1b9f76a70473\",\n" +
            "                \"step\": \"consent_select\",\n" +
            "                \"data\": [\n" +
            "                    {\n" +
            "                        \"_id\": \"6010b8c4-2d0d-50da-b309-1b9f76a70473\",\n" +
            "                        \"offer\": {\n" +
            "                            \"user_id\": \"fe467dfd-024b-5de1-bdce-19a2fec8f004\",\n" +
            "                            \"doc_id\": \"6010b8c4-2d0d-50da-b309-1b9f76a70473\",\n" +
            "                            \"_id\": \"cc48bcfd-3f41-53c0-95a4-8a1c48cd70e8\",\n" +
            "                            \"provider_id\": \"101\",\n" +
            "                            \"provider_descriptor\": {\n" +
            "                                \"images\": [\n" +
            "                                    {\n" +
            "                                        \"url\": \"https://refo-static-public.s3.ap-south-1.amazonaws.com/dmi/dmi-sm.png\",\n" +
            "                                        \"size_type\": \"sm\"\n" +
            "                                    },\n" +
            "                                    {\n" +
            "                                        \"url\": \"https://refo-static-public.s3.ap-south-1.amazonaws.com/dmi/dmi-md.png\",\n" +
            "                                        \"size_type\": \"md\"\n" +
            "                                    },\n" +
            "                                    {\n" +
            "                                        \"url\": \"https://refo-static-public.s3.ap-south-1.amazonaws.com/dmi/dmi-lg.png\",\n" +
            "                                        \"size_type\": \"lg\"\n" +
            "                                    }\n" +
            "                                ],\n" +
            "                                \"name\": \"DMI FINANCE PRIVATE LIMITED\",\n" +
            "                                \"short_desc\": \"DMI FINANCE PRIVATE LIMITED\",\n" +
            "                                \"long_desc\": \"DMI FINANCE PRIVATE LIMITED\"\n" +
            "                            },\n" +
            "                            \"provider_tags\": [\n" +
            "                                {\n" +
            "                                    \"name\": \"Contact Info\",\n" +
            "                                    \"tags\": {\n" +
            "                                        \"GRO_NAME\": \"Ashish Sarin\",\n" +
            "                                        \"GRO_EMAIL\": \"head.services@dmifinance.in/grievance@dmifinance.in\",\n" +
            "                                        \"GRO_CONTACT_NUMBER\": \"011-41204444\",\n" +
            "                                        \"GRO_DESIGNATION\": \"Senior Vice President - Customer Success\",\n" +
            "                                        \"GRO_ADDRESS\": \"Express Building, 3rd Floor, 9-10, Bahadur Shah Zafar Marg, New Delhi-110002\",\n" +
            "                                        \"CUSTOMER_SUPPORT_LINK\": \"https://portal.dmifinance.in\",\n" +
            "                                        \"CUSTOMER_SUPPORT_CONTACT_NUMBER\": \"9350657100\",\n" +
            "                                        \"CUSTOMER_SUPPORT_EMAIL\": \"customercare@dmifinance.in\"\n" +
            "                                    }\n" +
            "                                },\n" +
            "                                {\n" +
            "                                    \"name\": \"Lsp Info\",\n" +
            "                                    \"tags\": {\n" +
            "                                        \"LSP_NAME\": \"DMI Finance Pvt. Ltd\",\n" +
            "                                        \"LSP_EMAIL\": \"customercare@dmifinance.in\",\n" +
            "                                        \"LSP_CONTACT_NUMBER\": \"9350657100\",\n" +
            "                                        \"LSP_ADDRESS\": \"Express Building, 3rd Floor, 9-10, Bahadur Shah Zafar Marg New Delhi-110002\"\n" +
            "                                    }\n" +
            "                                }\n" +
            "                            ],\n" +
            "                            \"item_id\": \"d9eb81e2-96b5-477f-98dc-8518ad60d72e\",\n" +
            "                            \"item_descriptor\": {\n" +
            "                                \"code\": \"PERSONAL_LOAN\",\n" +
            "                                \"name\": \"Personal Loan\"\n" +
            "                            },\n" +
            "                            \"item_price\": {\n" +
            "                                \"currency\": \"INR\",\n" +
            "                                \"value\": \"647649.13\"\n" +
            "                            },\n" +
            "                            \"item_tags\": [\n" +
            "                                {\n" +
            "                                    \"name\": \"Loan Information\",\n" +
            "                                    \"display\": true,\n" +
            "                                    \"tags\": {\n" +
            "                                        \"INTEREST_RATE\": \"17.00%\",\n" +
            "                                        \"TERM\": \"36 Months\",\n" +
            "                                        \"INTEREST_RATE_TYPE\": \"FIXED\",\n" +
            "                                        \"APPLICATION_FEE\": \"0.00 INR\",\n" +
            "                                        \"FORECLOSURE_FEE\": \"4.00% + GST\",\n" +
            "                                        \"INTEREST_RATE_CONVERSION_CHARGE\": \"0\",\n" +
            "                                        \"DELAY_PENALTY_FEE\": \"3.00% + GST\",\n" +
            "                                        \"OTHER_PENALTY_FEE\": \"0\",\n" +
            "                                        \"TNC_LINK\": \"https://www.dmifinance.in/pdf/Loan-Application-Undertaking.pdf\",\n" +
            "                                        \"ANNUAL_PERCENTAGE_RATE\": \"17.85%\",\n" +
            "                                        \"REPAYMENT_FREQUENCY\": \"MONTHLY\",\n" +
            "                                        \"NUMBER_OF_INSTALLMENTS_OF_REPAYMENT\": \"36\",\n" +
            "                                        \"COOL_OFF_PERIOD\": \"2024-12-15T07:19:14.704Z\",\n" +
            "                                        \"INSTALLMENT_AMOUNT\": \"17826.36 INR\"\n" +
            "                                    }\n" +
            "                                }\n" +
            "                            ],\n" +
            "                            \"form_id\": \"bb1673b1-239c-4310-9eda-e6536aa0839c\",\n" +
            "                            \"from_url\": \"https://dmi-ondcpreprod.refo.dev/loans/lvform/bb1673b1-239c-4310-9eda-e6536aa0839c\",\n" +
            "                            \"quote_id\": \"702971c2-8a3b-48f4-9bf0-3b8ab826fd0e\",\n" +
            "                            \"quote_price\": {\n" +
            "                                \"currency\": \"INR\",\n" +
            "                                \"value\": \"647649.13\"\n" +
            "                            },\n" +
            "                            \"quote_breakup\": [\n" +
            "                                {\n" +
            "                                    \"title\": \"PRINCIPAL\",\n" +
            "                                    \"value\": \"500000.00\",\n" +
            "                                    \"currency\": \"INR\"\n" +
            "                                },\n" +
            "                                {\n" +
            "                                    \"title\": \"INTEREST\",\n" +
            "                                    \"value\": \"141749.13\",\n" +
            "                                    \"currency\": \"INR\"\n" +
            "                                },\n" +
            "                                {\n" +
            "                                    \"title\": \"NET_DISBURSED_AMOUNT\",\n" +
            "                                    \"value\": \"494100.00\",\n" +
            "                                    \"currency\": \"INR\"\n" +
            "                                },\n" +
            "                                {\n" +
            "                                    \"title\": \"OTHER_UPFRONT_CHARGES\",\n" +
            "                                    \"value\": \"0.00\",\n" +
            "                                    \"currency\": \"INR\"\n" +
            "                                },\n" +
            "                                {\n" +
            "                                    \"title\": \"INSURANCE_CHARGES\",\n" +
            "                                    \"value\": \"0.00\",\n" +
            "                                    \"currency\": \"INR\"\n" +
            "                                },\n" +
            "                                {\n" +
            "                                    \"title\": \"OTHER_CHARGES\",\n" +
            "                                    \"value\": \"0.00\",\n" +
            "                                    \"currency\": \"INR\"\n" +
            "                                },\n" +
            "                                {\n" +
            "                                    \"title\": \"PROCESSING_FEE\",\n" +
            "                                    \"value\": \"5900.00\",\n" +
            "                                    \"currency\": \"INR\"\n" +
            "                                }\n" +
            "                            ],\n" +
            "                            \"txn_id\": \"e4b743b8-a433-5021-9845-34ba7e78b827\",\n" +
            "                            \"msg_id\": \"a54c1805-4927-58df-8e4d-89a84ac22231\",\n" +
            "                            \"bpp_id\": \"dmi-ondcpreprod.refo.dev\",\n" +
            "                            \"bpp_uri\": \"https://dmi-ondcpreprod.refo.dev/app/ondc/seller\",\n" +
            "                            \"expires_at\": \"2025-01-09T07:18:14.704Z\",\n" +
            "                            \"settlement_amount\": \"180000.00\"\n" +
            "                        }\n" +
            "                    }\n" +
            "                ]\n" +
            "            }\n" +
            "        ]\n" +

            "    },\n" +
            "    \"statusCode\": 200\n" +
            "}"
    val json = Json { prettyPrint = true; ignoreUnknownKeys = true }

    val status = json.decodeFromString(UserStatus.serializer(), userStatusResponse)

    status.let { status ->
        PersonalDecidedFlow(status, navController = rememberNavController(), fromFlow)
    }
}

@Composable
fun PersonalDecidedFlow(status: UserStatus?, navController: NavHostController, fromFlow: String) {
    if (status?.data == null || status?.data?.data?.any { it == null } == true) {
        Log.d("test status: ", "null")
        navigateToPersonaLoanScreen(navController = navController, fromFlow = "Personal Loan")
    } else if (status?.data?.data?.lastOrNull()?.step.equals(
            "loan_select_form_submission_PENDING",
            true
        ) ||
        status?.data?.data?.lastOrNull()?.step.equals("loan_select_form_submission_REJECTED", true)
    ) {
        navigateToFormRejectedScreen(
            navController = navController,
            errorTitle = stringResource(id = R.string.kyc_failed),
            fromFlow = fromFlow, errorMsg =
            if (status?.data?.data?.lastOrNull()?.step.equals("loan_select_form_submission_PENDING", true)
            ) {
                stringResource(id = R.string.form_submission_pending)
            } else {
                stringResource(id = R.string.form_submission_rejected)
            }
        )
    } else if (status?.data?.data?.lastOrNull()?.step.equals(
            "emandate_form_submission_PENDING",
            true
        ) ||
        status?.data?.data?.lastOrNull()?.step.equals("emandate_form_submission_REJECTED", true)
    ) {
        navigateToFormRejectedScreen(
            navController = navController,
            errorTitle = stringResource(id = R.string.emandate_failed),
            fromFlow = fromFlow,
            errorMsg = if (status?.data?.data?.lastOrNull()?.step.equals(
                    "emandate_form_submission_PENDING",
                    true
                )
            ) {
                stringResource(id = R.string.form_submission_pending)
            } else {
                stringResource(id = R.string.form_submission_rejected)
            }
        )
    } else if (status?.data?.data?.lastOrNull()?.step.equals ("account_information_form_submission_failed", true)){
        navigateToFormRejectedScreen(
            navController = navController,
            errorTitle = stringResource(id = R.string.repayment_failed),
            fromFlow = fromFlow, errorMsg= stringResource(id = R.string.form_submission_rejected_or_pending)
        )
    }else if (status?.data?.data?.lastOrNull()?.step.equals ("loan_agreement_form_submission_failed", true)){
        navigateToFormRejectedScreen(
            navController = navController,
            errorTitle = stringResource(id = R.string.loan_agreement_failed),
            fromFlow = fromFlow, errorMsg= stringResource(id = R.string.form_submission_rejected_or_pending)
        )
    } else if (status?.data?.data?.firstOrNull()?.step.equals("search", true)) {

        val offerList = status?.data?.data?.lastOrNull()?.offerResponse?.lastOrNull()?.data?.map { data ->
            Offer(
                data.offer,
                data.id
            )
        }

        val searchResponse = SearchResponseModel(
            id = status?.data?.data?.lastOrNull()?.id,
            url = status?.data?.data?.lastOrNull()?.url ?: "",
            transactionId = status?.data?.txnId,
            consentResponse = status?.data?.data?.firstOrNull()?.consentResponse,
            offerResponse = offerList
        )

        if (offerList.isNullOrEmpty()) {
            val searchModel = SearchModel(
                data = searchResponse,
                status = true,
                statusCode = 200
            )

            NavigateToWebView(
                searchModel = searchModel, gstSearchResponse = null,
                fromFlow = fromFlow, navController = navController, searchResponse = searchModel
            )
        } else {

            val json = Json { prettyPrint = true; ignoreUnknownKeys = true }
            val searchJson = json.encodeToString(
                SearchModel.serializer(),
                SearchModel(data = searchResponse, status = true, statusCode = 200)
            )
            navigateToLoanOffersScreen(
                navController,
                stringResource(R.string.getUerFlow), fromFlow, searchJson
            )
        }
    }
    else {
        if (status.data.url != null) {
            status.data.url.let { webUrl ->
                val transactionId = status?.data?.data?.get(0)?.data?.get(0)?.txnId
                transactionId.let { transactionId ->
                    transactionId?.let { Log.d("test transactionId: ", it) }

                    status.data.id?.let { searchId ->
                        transactionId?.let {
                            navigateToWebViewFlowOneScreen(
                                navController = navController,
                                purpose = stringResource(R.string.getUerFlow),
                                fromFlow = fromFlow,
                                id = searchId,
                                transactionId = transactionId,
                                url = webUrl
                            )
                        }
                    }

                }
            }
        } else {
            val transactionId = status?.data?.txnId

            transactionId?.let { Log.d("test transactionId: ", it) }


            status.data.data?.forEach { data ->
                data?.step?.let { step ->
                    if (step.equals("consent_select", true)) {
                        transactionId?.let {
                            navigateToLoanProcessScreen(
                                navController, statusId = 2, transactionId = transactionId,
                                responseItem =
                                "No Need Response Item",
                                offerId = "1234", fromFlow = fromFlow
                            )
                        }
                    } else if (step.equals("loan_select", true)) {
                        status.data.data.forEach { item ->
                            item?.data?.forEach { item2 ->
                                item2?.fromUrl?.let { kycUrl ->
                                    item2.id?.let { id ->
                                        transactionId?.let {transactionId->
                                            transactionId?.let { Log.d("test transactionId: ", it) }

                                            navigateToLoanProcessScreen(
                                                navController = navController,
                                                transactionId = transactionId,
                                                statusId = 3,
                                                offerId = id, fromFlow = fromFlow,
                                                responseItem = kycUrl
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else if (step.equals("loan_select_form_submission_APPROVED")) {
                        status.data.data.forEach { userItem ->
                            userItem?.id?.let { id ->
                                transactionId?.let {transactionId->
                                    navigateToLoanProcessScreen(
                                        responseItem = "No need ResponseItem",
                                        transactionId = transactionId,
                                        offerId = id,
                                        navController = navController, statusId = 4,
                                        fromFlow = fromFlow
                                    )
                                }
                            }
                        }
                    } else if (step.equals("emandate_approved", true)) {
                        status.data.data.forEach { userItem ->
                            userItem?.data?.forEach { item ->
                                item?.fromUrl?.let { enachUrl ->
                                    userItem.id?.let { id ->
                                        transactionId?.let {transactionId->
                                            navigateToLoanProcessScreen(
                                                navController = navController,
                                                transactionId = transactionId,
                                                statusId = 5,
                                                responseItem = enachUrl, offerId = id,
                                                fromFlow = fromFlow
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else if (step.equals("loan_agreement_approved", true)) {
                        status.data.data.forEach { userItem ->
                            userItem?.data?.forEach { item ->
                                item?.fromUrl?.let { loanAgreementUrl ->
                                    userItem.id?.let { id ->
                                        transactionId?.let {transactionId->
                                            navigateToLoanProcessScreen(
                                                transactionId = transactionId,
                                                offerId = id, responseItem = loanAgreementUrl,
                                                navController = navController, statusId = 6,
                                                fromFlow = fromFlow
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
    }
}

@Composable
fun InvoiceDecidedFlow(status: UserStatus?, navController: NavHostController, fromFlow: String) {
    if (status?.data == null) {
        navigateToGstInvoiceLoanScreen(navController = navController, fromFlow = "Invoice Loan")
    }  else if (status?.data?.data?.lastOrNull()?.step.equals("individual_ekyc_form_submission_failed", true) ||
        status?.data?.data?.lastOrNull()?.step.equals ("entity_ekyc_form_submission_failed", true)
        ){
        navigateToFormRejectedScreen(
            navController = navController,
            errorTitle = stringResource(id = R.string.kyc_failed),
            fromFlow = fromFlow, errorMsg= stringResource(id = R.string.form_submission_rejected_or_pending)
        )
    }
    else if (status?.data?.data?.lastOrNull()?.step.equals("emandate_form_submission_failed", true)){
        navigateToFormRejectedScreen(
            navController = navController,
            errorTitle = stringResource(id = R.string.emandate_failed),
            fromFlow = fromFlow, errorMsg=stringResource(id = R.string.form_submission_rejected_or_pending)
        )
    }else if (status?.data?.data?.lastOrNull()?.step.equals("account_information_form_submission_failed", true)){
        navigateToFormRejectedScreen(
            navController = navController,
            errorTitle = stringResource(id = R.string.repayment_failed),
            fromFlow = fromFlow, errorMsg=stringResource(id = R.string.form_submission_rejected_or_pending)
        )
    }else if (status?.data?.data?.lastOrNull()?.step.equals("loan_agreement_form_submission_failed", true)){
        navigateToFormRejectedScreen(
            navController = navController,
            errorTitle = stringResource(id = R.string.loan_agreement_failed),
            fromFlow = fromFlow, errorMsg=stringResource(id = R.string.form_submission_rejected_or_pending)
        )
    }else if (status?.data?.data?.lastOrNull()?.step.equals ("search", true)){
        val searchResponseData = GstSearchData(
            id = status?.data?.data?.lastOrNull()?.id,
            url = status?.data?.data?.lastOrNull()?.url?:"",
            transactionId = status?.data?.txnId
        )

        // Create an instance of SearchModel
        val searchResponse = GstSearchResponse(
            data = searchResponseData,
            status = true,
            statusCode = 200
        )
        NavigateToWebView(
            searchResponse = null, gstSearchResponse = searchResponse,
            fromFlow = fromFlow, navController = navController, searchModel = null
        )
    }
    else {
        if (status.data.url != null) {
            status.data.url.let { webUrl ->
                val transactionId = status?.data?.data?.get(0)?.data?.get(0)?.txnId
                transactionId.let { transactionId ->
                    transactionId?.let { Log.d("test transactionId: ", it) }

                    status.data.id?.let { searchId ->
                        transactionId?.let {
                            navigateToWebViewFlowOneScreen(
                                navController = navController,
                                purpose = stringResource(R.string.getUerFlow),
                                fromFlow = fromFlow,
                                id = searchId,
                                transactionId = transactionId,
                                url = webUrl
                            )
//                            SearchWebView(
//                                navController = navController, urlToOpen = webUrl,
//                                searchId = searchId, transactionId = it, fromFlow = fromFlow,
//                                pageContent = {}
//                            )
                        }
                    }

                }
            }
        } else {
            val transactionId = status?.data?.txnId

            transactionId?.let { Log.d("test transactionId: ", it) }


            status.data.data?.forEach { data ->
                data?.step?.let { step ->
                    if (step.equals("consent_select", true)) { //verified
                        transactionId?.let {
                            data?.data?.let { userItem ->
                                userItem?.let {
//                                    val json = Json { prettyPrint = true; ignoreUnknownKeys = true }
//                                    val nonNullUserList = userItem.filterNotNull()
//                                    val offerDetail = json.encodeToString(ListSerializer(UserStatusItem.serializer()), nonNullUserList)
                                    navigateToLoanProcessScreen(
                                        navController = navController,
                                        transactionId = transactionId,
                                        statusId = 20,
                                        responseItem = "Not required",
                                        offerId = "1234",
                                        fromFlow = "Invoice Loan"
                                    )
                                }
                            }
                        }
                    } else if (step.equals("offer_select", true)) {
                        status.data.data.forEach { item ->
                            item?.data?.forEach { item2 ->
                                item2?.fromUrl?.let { kycUrl ->
                                    item2.id?.let { id ->
                                        transactionId?.let {transactionId->
                                            transactionId?.let { Log.d("test transactionId: ", it) }

//                                            navigateToLoanProcessScreen(
//                                                navController = navController,
//                                                transactionId = transactionId,
//                                                statusId = 3,
//                                                offerId = id, fromFlow = fromFlow,
//                                                responseItem = kycUrl
//                                            )
                                            navigateToLoanProcessScreen(
                                                navController = navController, statusId = 13, responseItem = kycUrl,
                                                offerId = id, fromFlow = fromFlow, transactionId = transactionId
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else if (step.equals("offer_confirm", true)) { //verified
                        status.data.data.forEach { userItem ->
                            userItem?.data?.forEach { item ->
                                item?.fromUrl?.let { loanAgreementUrl ->
                                    userItem.id?.let { id ->
                                        transactionId?.let {transactionId->
                                            navigateToLoanProcessScreen(
                                                transactionId = transactionId,
                                                offerId = id, responseItem = loanAgreementUrl,
                                                navController = navController, statusId = 13,
                                                fromFlow = fromFlow
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }else if (step.equals("loan_approved", true)) { //Verified
                        status.data.data.forEach { userItem ->
                            userItem?.data?.forEach { item ->
                                item?.fromUrl?.let { verificationUrl ->
                                    userItem.id?.let { id ->
                                        transactionId?.let {transactionId->
                                            navigateToBankKycVerificationScreen(
                                                navController = navController, kycUrl = verificationUrl,
                                                transactionId = transactionId,
                                                offerId = id, verificationStatus = "2",
                                                fromFlow = fromFlow
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else if (step.equals("entity_ekyc_after_form_submission", true)) {
                        status.data.data.forEach { userItem ->
                            userItem?.data?.forEach { item ->
                                userItem.id?.let { id ->
                                    transactionId?.let {transactionId->
                                        navigateToLoanProcessScreen(
                                            transactionId = transactionId,
                                            offerId = id, responseItem = "No Need",
                                            navController = navController, statusId = 14,
                                            fromFlow = fromFlow
                                        )
                                    }
                                }
                            }
                        }
                    }
                    else if (step.equals("account_information_approved", true)) { //verified
                        status.data.data.forEach { userItem ->
                            userItem?.data?.forEach { item ->
                                item?.fromUrl?.let { enachUrl ->
                                    userItem.id?.let { id ->
                                        transactionId?.let {transactionId->
                                            navigateToLoanProcessScreen(
                                                navController = navController,
                                                transactionId = transactionId,
                                                statusId = 5,
                                                responseItem = enachUrl, offerId = id,
                                                fromFlow = fromFlow
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else if (step.equals("loan_agreement_approved", true)) { //verified
                        status.data.data.forEach { userItem ->
                            userItem?.data?.forEach { item ->
                                item?.fromUrl?.let { loanAgreementUrl ->
                                    userItem.id?.let { id ->
                                        transactionId?.let {transactionId->
                                            navigateToLoanProcessScreen(
                                                transactionId = transactionId,
                                                offerId = id, responseItem = loanAgreementUrl,
                                                navController = navController, statusId = 16,
                                                fromFlow = fromFlow
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
    }
}

@Composable
fun PurchaseDecidedFlow(status: UserStatus?, navController: NavHostController, fromFlow: String) {
    if (status?.data == null) {
        navigateToLoanProcessScreen(
            responseItem = "No Need", offerId = "1234", fromFlow = fromFlow,
            navController = navController, statusId = 18, transactionId = "Sai"
        )
    } else if (status?.data?.data?.lastOrNull()?.step.equals("loan_select_form_submission_failed", true)){
        navigateToFormRejectedScreen(
            navController = navController,
            errorTitle = stringResource(id = R.string.kyc_failed),
            fromFlow = fromFlow, errorMsg= stringResource(id = R.string.form_submission_rejected_or_pending)
        )
    } else if (status?.data?.data?.lastOrNull()?.step.equals ("emandate_form_submission_failed", true)){
        navigateToFormRejectedScreen(
            navController = navController,
            errorTitle = stringResource(id = R.string.emandate_failed),
            fromFlow = fromFlow, errorMsg= stringResource(id = R.string.form_submission_rejected_or_pending)
        )
    }else if (status?.data?.data?.lastOrNull()?.step.equals ("account_information_form_submission_failed", true)){
        navigateToFormRejectedScreen(
            navController = navController,
            errorTitle = stringResource(id = R.string.repayment_failed),
            fromFlow = fromFlow, errorMsg= stringResource(id = R.string.form_submission_rejected_or_pending)
        )
    }else if (status?.data?.data?.lastOrNull()?.step.equals ("loan_agreement_form_submission_failed", true)){
        navigateToFormRejectedScreen(
            navController = navController,
            errorTitle = stringResource(id = R.string.loan_agreement_failed),
            fromFlow = fromFlow, errorMsg= stringResource(id = R.string.form_submission_rejected_or_pending)
        )
    }else if (status?.data?.data?.lastOrNull()?.step.equals ("search", true)){
        val searchResponse = SearchResponseModel(
            id = status?.data?.data?.lastOrNull()?.id,
            url = status?.data?.data?.lastOrNull()?.url?:"",
            transactionId = status?.data?.txnId
        )

        // Create an instance of SearchModel
        val searchModel = SearchModel(
            data = searchResponse,
            status = true,
            statusCode = 200
        )
        NavigateToWebView(
            searchResponse = searchModel, gstSearchResponse = null,
            fromFlow = fromFlow, navController = navController, searchModel = searchModel
        )
    }
    else {
        if (status.data.url != null) {
            status.data.url.let { webUrl ->
                val transactionId = status?.data?.data?.get(0)?.data?.get(0)?.txnId
                transactionId.let { transactionId ->
                    transactionId?.let { Log.d("test transactionId: ", it) }

                    status.data.id?.let { searchId ->
                        transactionId?.let {
                            navigateToWebViewFlowOneScreen(
                                navController = navController,
                                purpose = stringResource(R.string.getUerFlow),
                                fromFlow = fromFlow,
                                id = searchId,
                                transactionId = transactionId,
                                url = webUrl
                            )
                        }
                    }

                }
            }
        } else {
            val transactionId = status?.data?.txnId

            transactionId?.let { Log.d("test transactionId: ", it) }


            status.data.data?.forEach { data ->
                data?.step?.let { step ->
                    if (step.equals("post_search", true)) {
                        transactionId?.let {
                            navigateToLoanProcessScreen(
                                navController, statusId = 20, transactionId = transactionId,
                                responseItem =
                                "No Need Response Item",
                                offerId = "1234", fromFlow = fromFlow
                            )
                        }
                    } else if (step.equals("offer_confirm", true)) {
                        status.data.data.forEach { userItem ->
                            userItem?.data?.forEach { item ->
                                item?.fromUrl?.let { loanAgreementUrl ->
                                    userItem.id?.let { id ->
                                        transactionId?.let {transactionId->
                                            navigateToLoanProcessScreen(
                                                transactionId = transactionId,
                                                offerId = id, responseItem = loanAgreementUrl,
                                                navController = navController, statusId = 13,
                                                fromFlow = fromFlow
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else if (step.equals("loan_approved", true)) {
                        status.data.data.forEach { userItem ->
                            userItem?.data?.forEach { item ->
                                item?.fromUrl?.let { verificationUrl ->
                                    userItem.id?.let { id ->
                                        transactionId?.let { transactionId ->
                                            navigateToLoanProcessScreen(
                                                transactionId = transactionId,
                                                offerId = id, responseItem = verificationUrl,
                                                navController = navController, statusId = 5,
                                                fromFlow = fromFlow
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else if (step.equals("loan_agreement_approved", true)) {
                        status.data.data.forEach { userItem ->
                            userItem?.data?.forEach { item ->
                                item?.fromUrl?.let { loanAgreementUrl ->
                                    userItem.id?.let { id ->
                                        transactionId?.let {transactionId->
                                            navigateToLoanProcessScreen(
                                                transactionId = transactionId,
                                                offerId = id, responseItem = loanAgreementUrl,
                                                navController = navController, statusId = 22,
                                                fromFlow = fromFlow
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
    }
}

@Preview
@Composable
fun ApplyByCategoryScreenPreview() {
    ApplyByCategoryScreen(navController = NavHostController(LocalContext.current))
}