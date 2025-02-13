package com.github.sugunasriram.myfisloanlibone.fis_code.navigation

import android.net.Uri
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.github.sugunasriram.myfisloanlibone.LoanLib
import com.github.sugunasriram.myfisloanlibone.fis_code.components.AnimationLoader
import com.github.sugunasriram.myfisloanlibone.fis_code.components.KycAnimation
import com.github.sugunasriram.myfisloanlibone.fis_code.views.ApplyByCategoryScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.LoanProcessScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.auth.ForgotPasswordScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.auth.OtpScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.auth.OtpVerifiedScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.auth.RegisterScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.auth.ResetPasswordScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.auth.SignInScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.auth.SpalashScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.auth.UpdateProfileScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.auth.UserDetailScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.documents.AboutUsScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.documents.PrivacyPolicyScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.documents.TermsConditionsScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.gstLoan.GstBankKycVerificationScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.gstLoan.GstDetailsScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.gstLoan.GstInformationScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.gstLoan.GstInvoiceDetailScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.gstLoan.GstInvoiceLoanOfferScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.gstLoan.GstInvoiceLoanScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.gstLoan.GstInvoiceLoansScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.gstLoan.GstLoanOfferListScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.gstLoan.GstNumberVerifyScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.gstLoan.InvoiceDetailScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.igm.CreateIssueScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.igm.IssueDetailScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.igm.IssueListScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.invalid.FormRejectionScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.invalid.UnexpectedErrorScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan.AccountAgreegatorScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan.AddBankDetailScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan.AnnualIncomeScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan.BankDetailScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan.DownPaymentScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan.EditLoanRequestScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan.HomePageScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan.LanguageSelectionScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan.LoanDisbursementScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan.LoanOffersListDetailsScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan.LoanOffersListScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan.LoanSummaryScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan.PersonaLoanScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan.PrePaymentStatusScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan.RepaymentScheduleScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan.ReviewDetailsScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan.ReviewDetailsScreenForPF
import com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan.SelectAccountAgreegatorScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan.SelectBankScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.sidemenu.LoanListScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.sidemenu.LoanStatusDetailScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.sidemenu.LoanStatusScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.webview.AAConsentApprovalScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.webview.LoanAgreementWebScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.webview.RepaymentWebScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.webview.SearchWebView
import com.github.sugunasriram.myfisloanlibone.fis_code.views.webview.SearchWebViewScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.webview.gst.GstKycWebViewScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.webview.personalLoan.PrePartPaymentWebView
import com.github.sugunasriram.myfisloanlibone.fis_code.views.webview.personalLoan.WebKycScreen

fun NavGraphBuilder.mobileNavigation(
    navController: NavHostController, startDestination: String
) {
    navigation(route = AppNavGraph.GRAPH_LAUNCH, startDestination = startDestination) {

        // Authentication Screen
        composable(AppScreens.SplashScreen.route) {
            SpalashScreen(navController = navController)
        }

        composable(AppScreens.LanguageSelectionScreen.route) {
            LanguageSelectionScreen(navController = navController)
        }

        composable(AppScreens.RegisterScreen.route) {
            RegisterScreen(navController = navController)
        }

        composable("${AppScreens.OtpScreen.route}/{mobileNumber}/{orderId}/{fromScreen}") { backStackEntry ->
            val mobileNumber = backStackEntry.arguments?.getString("mobileNumber")
            val orderId = backStackEntry.arguments?.getString("orderId")
            val fromScreen = backStackEntry.arguments?.getString("fromScreen")
            if (orderId != null && mobileNumber != null && fromScreen != null) {
                OtpScreen(
                    navController = navController, number = mobileNumber, orderId = orderId,
                    fromScreen = fromScreen
                )
            }
        }

        composable(AppScreens.OtpVerifyScreen.route) {
            OtpVerifiedScreen(navController = navController)
        }

        composable(AppScreens.SignInScreen.route) {
            SignInScreen(navController = navController)

        }
        composable(AppScreens.ForgotPasswordScreen.route) {
            ForgotPasswordScreen(navController = navController)

        }
        composable("${AppScreens.ResetPasswordScreen.route}/{mobileNumber}") { backStack ->
            val mobileNumber = backStack.arguments?.getString("mobileNumber")
            if (mobileNumber != null) {
                ResetPasswordScreen(navController = navController, mobileNumber = mobileNumber)
            }
        }

        composable(AppScreens.UpdateProfileScreen.route) {
            UpdateProfileScreen(navController = navController)
        }

        composable(AppScreens.UserProfileScreen.route) {
            UserDetailScreen(navController = navController)
        }

        composable("${AppScreens.HomePageScreen.route}/{fromFlow}") { backStack ->
            val fromFlow = backStack.arguments?.getString("fromFlow")
            if (fromFlow != null) {
                HomePageScreen(navController = navController, fromFlow = fromFlow)
            }
        }

        composable(AppScreens.ApplyBycategoryScreen.route) {
            ApplyByCategoryScreen(navController = navController)
        }


        // Personal Loan Screens


        composable("${AppScreens.PersonalLoanScreen.route}/{fromFlow}") { backStack ->
            val fromFlow = backStack.arguments?.getString("fromFlow")
            if (fromFlow != null) {
                PersonaLoanScreen(navController = navController, fromFlow = fromFlow)
            }
        }

        composable("${AppScreens.LoanProcessScreen
            .route}/{transactionId}/{statusId}/{responseItem}/{offerId}/{fromFlow}") { backStack ->
            val transactionId = backStack.arguments?.getString("transactionId")
            val statusId = backStack.arguments?.getString("statusId")
            val responseItem = backStack.arguments?.getString("responseItem")
            val offerId = backStack.arguments?.getString("offerId")
            val fromFlow = backStack.arguments?.getString("fromFlow")
            if (transactionId != null && statusId != null && responseItem != null && offerId !=
            null &&
                    fromFlow != null) {
                LoanProcessScreen(
                    navController = navController, transactionId = transactionId, statusId =
                    statusId,
                    responseItem = responseItem, offerId = offerId, fromFlow = fromFlow
                )
            }
        }

        composable("${AppScreens.AnnualIncomeScreen.route}/{fromFlow}") { backStack ->
            val fromFlow = backStack.arguments?.getString("fromFlow")
            if (fromFlow != null) {
                AnnualIncomeScreen(navController = navController, fromFlow = fromFlow)
            }
        }

        composable("${AppScreens.ReviewDetailsScreen.route}/{purpose}/{fromFlow}") { backStack ->
            val purpose = backStack.arguments?.getString("purpose")
            val fromFlow = backStack.arguments?.getString("fromFlow")
            if (purpose != null && fromFlow != null) {
                ReviewDetailsScreen(
                    navController = navController, purpose = purpose, fromFlow = fromFlow
                )
            }
        }

        //Sugu
        composable("${AppScreens.ReviewDetailsScreenForPF.route}/{personalDetails}/{productDetails}") {
            backStack ->
                ReviewDetailsScreenForPF(
                    navController = navController,
                    purpose = "travel", fromFlow = "Purchase Loan"
                )
            }

        composable("${AppScreens.SearchWebViewScreen.route}/{purpose}/{fromFlow}") { backStack ->
            val purpose = backStack.arguments?.getString("purpose")
            val fromFlow = backStack.arguments?.getString("fromFlow")
            if (purpose != null && fromFlow != null) {
                SearchWebViewScreen(
                    navController = navController, purpose = purpose, fromFlow = fromFlow
                )
            }
        }

        composable("${AppScreens.SearchWebView.route}/{transactionId}/{urlToOpen}/{searchId}/{fromFlow}") {
            backStack ->
            val transactionId = backStack.arguments?.getString("transactionId")
            val urlToOpen = backStack.arguments?.getString("urlToOpen")
            val searchId = backStack.arguments?.getString("searchId")
            val fromFlow = backStack.arguments?.getString("fromFlow")

            if (transactionId != null && urlToOpen != null && searchId != null && fromFlow != null){
                SearchWebView(
                    navController = navController, urlToOpen = urlToOpen, searchId = searchId,
                    fromFlow = fromFlow, transactionId=transactionId
                ) { }
            }
        }

        composable("${AppScreens.AAConsentApprovalScreen.route}/{searchId}/{url}/{fromFlow}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("searchId")
            val url = Uri.decode(backStackEntry.arguments?.getString("url") ?: "")
            val fromFlow = backStackEntry.arguments?.getString("fromFlow")
            if (id != null && url != null && fromFlow != null) {
                AAConsentApprovalScreen(
                    navController = navController, id = id, url = url, fromFlow = fromFlow
                )
            }
        }

        composable("${AppScreens.LoanOffersListScreen.route}/{offerResponse}/{fromFlow}") { backStackEntry ->
            val offerResponse = backStackEntry.arguments?.getString("offerResponse")
            val fromFlow = backStackEntry.arguments?.getString("fromFlow")
            if (offerResponse != null && fromFlow != null) {
                LoanOffersListScreen(
                    navController = navController, offerItem = offerResponse, fromFlow = fromFlow
                )
            }
        }

        composable("${AppScreens.LoanOffersListDetailScreen.route}/{responseItem}/{id}/{showButtonId}/{fromFlow}") {
            val responseItem = it.arguments?.getString("responseItem")
            val id = it.arguments?.getString("id")
            val showButton = it.arguments?.getString("showButtonId")
            val fromFlow = it.arguments?.getString("fromFlow")
            if (responseItem != null && id != null && showButton != null && fromFlow != null) {
                LoanOffersListDetailsScreen(
                    navController = navController, responseItem = responseItem, id = id,
                    showButtonId = showButton, fromFlow = fromFlow
                )
            }
        }

        composable("${AppScreens.EditLoanRequestScreen
            .route}/{id}/{loanAmount}/{minLoanAmount}/{loanTenure}/{offerId}/{fromFlow}") {
            backStackEntry ->
            val offerId = backStackEntry.arguments?.getString("offerId")
            val id = backStackEntry.arguments?.getString("id")
            val loanAmount = backStackEntry.arguments?.getString("loanAmount")
            val minLoanAmount = backStackEntry.arguments?.getString("minLoanAmount")
            val fromFlow = backStackEntry.arguments?.getString("fromFlow")
            val loanTenure =
                backStackEntry.arguments?.getString("loanTenure")?.replace(" Months", "")
                    ?.replace(" months", "")
            if (id != null && loanAmount != null && loanTenure != null && minLoanAmount != null
                && offerId != null && fromFlow != null) {
                EditLoanRequestScreen(
                    navController = navController, id = id, amount = loanAmount, minAmount = minLoanAmount,
                    tenure = loanTenure, offerId = offerId, fromFlow = fromFlow
                )
            }
        }

        composable("${AppScreens.WebKycScreen.route}/{transactionId}/{url}/{id}/{fromFlow}") {
            backStack ->
            val transactionId = backStack.arguments?.getString("transactionId")
            val url = backStack.arguments?.getString("url")
            val id = backStack.arguments?.getString("id")
            val fromFlow = backStack.arguments?.getString("fromFlow")
            if (transactionId != null && url != null && id != null && fromFlow != null) {
                WebKycScreen(
                    navController = navController, transactionId = transactionId,
                    url = url, id = id,
                    fromFlow = fromFlow
                ) {}
            }
        }

        composable("${AppScreens.KycAnimation.route}/{transactionId}/{responseItem}/{offerID}") {
            backStack ->
            val transactionId = backStack.arguments?.getString("transactionId")
            val offerID = backStack.arguments?.getString("offerID")
            val responseItem = backStack.arguments?.getString("responseItem")
            if (transactionId != null && offerID != null && responseItem != null) {
                KycAnimation(
                    navController = navController, transactionId = transactionId, offerId = offerID, responseItem = responseItem
                )
            }
        }

        composable("${AppScreens.AccountDetailsScreen.route}/{id}/{fromFlow}") { backStack ->
            val id = backStack.arguments?.getString("id")
            val fromFlow = backStack.arguments?.getString("fromFlow")
            if (id != null && fromFlow != null) {
                AddBankDetailScreen(navController = navController, id = id, fromFlow = fromFlow)
            }
        }

        composable("${AppScreens.RepaymentWebScreen.route}/{transactionId}/{url}/{id}/{fromFlow}") { backStack ->
            val transactionId = backStack.arguments?.getString("transactionId")
            val url = backStack.arguments?.getString("url")
            val id = backStack.arguments?.getString("id")
            val fromFlow = backStack.arguments?.getString("fromFlow")
            if (transactionId != null && url != null && id != null && fromFlow != null) {
                RepaymentWebScreen(
                    navController = navController, transactionId=transactionId,
                    url = url, id = id, fromFlow = fromFlow
                ) {}
            }
        }

        composable("${AppScreens.LoanAgreementWebScreen
            .route}/{transactionId}/{id}/{loanAgreementFormUrl}/{fromFlow}") { backStack ->
            val transactionId = backStack.arguments?.getString("transactionId")
            val id = backStack.arguments?.getString("id")
            val fromFlow = backStack.arguments?.getString("fromFlow")
            val loanAgreementFormUrl = backStack.arguments?.getString("loanAgreementFormUrl")
            if (transactionId != null && id != null && fromFlow != null && loanAgreementFormUrl !=
                null) {
                LoanAgreementWebScreen(
                    navController = navController, id = id, transactionId = transactionId,
                    loanAgreementFormUrl = loanAgreementFormUrl, fromFlow = fromFlow
                )
            }
        }

        composable("${AppScreens.LoanDisbursementScreen.route}/{transactionId}/{id}/{fromFlow}") {
            backStack ->
            val transactionId = backStack.arguments?.getString("transactionId")
            val id = backStack.arguments?.getString("id")
            val fromFlow = backStack.arguments?.getString("fromFlow")
            if (transactionId != null && id != null && fromFlow != null) {
                LoanDisbursementScreen(navController = navController, transactionId =
                transactionId, id = id, fromFlow = fromFlow)
            }
        }

        composable("${AppScreens.LoanSummary.route}/{id}/{consentHandler}/{fromFlow}") { backStack ->
            val id = backStack.arguments?.getString("id")
            val consentHandler = backStack.arguments?.getString("consentHandler")
            val fromFlow = backStack.arguments?.getString("fromFlow")
            if (id != null && consentHandler != null && fromFlow != null) {
                LoanSummaryScreen(
                    navController = navController, id = id, consentHandler = consentHandler,
                    fromFlow = fromFlow
                )
            }
        }

        composable("${AppScreens.RepaymentScheduleScreen.route}/{orderId}/{fromFlow}") { backStack ->
            val orderId = backStack.arguments?.getString("orderId")
            val fromFlow = backStack.arguments?.getString("fromFlow")
            if (orderId != null && fromFlow != null) {
                RepaymentScheduleScreen(
                    navController = navController, orderId = orderId, fromFlow = fromFlow
                )
            }
        }

        composable("${AppScreens.AnimationLoader.route}/{transactionId}/{id}/{fromFlow}") {
            backStack ->
            val transactionId = backStack.arguments?.getString("transactionId")
            val id = backStack.arguments?.getString("id")
            val fromFlow = backStack.arguments?.getString("fromFlow")
            if (transactionId != null && id != null && fromFlow != null) {
                AnimationLoader(navController = navController, transactionId = transactionId, id =
                id, fromFlow = fromFlow)
            }
        }

        composable("${AppScreens.SelectBankScreen.route}/{purpose}/{fromFlow}") { backStack ->
            val purpose = backStack.arguments?.getString("purpose")
            val fromFlow = backStack.arguments?.getString("fromFlow")
            if (purpose != null && fromFlow != null) {
                SelectBankScreen(
                    navController = navController, purpose = purpose, fromFlow = fromFlow
                )
            }
        }
        composable("${AppScreens.AccountAgreegatorScreen.route}/{purpose}/{fromFlow}") { backStack ->
            val purpose = backStack.arguments?.getString("purpose")
            val fromFlow = backStack.arguments?.getString("fromFlow")
            if (purpose != null && fromFlow != null) {
                AccountAgreegatorScreen(
                    navController = navController, loanPurpose = purpose, fromFlow = fromFlow
                )
            }
        }

        composable("${AppScreens.SelectAccountAgreegatorScreen.route}/{purpose}/{fromFlow}") { backStack ->
            val purpose = backStack.arguments?.getString("purpose")
            val fromFlow = backStack.arguments?.getString("fromFlow")
            if (purpose != null && fromFlow != null) {
                SelectAccountAgreegatorScreen(
                    navController = navController, purpose = purpose, fromFlow = fromFlow
                )
            }
        }

        composable("${AppScreens.PrePaymentStatusScreen.route}/{offer}/{headerText}/{fromFlow}") { backStack ->
            val offer = backStack.arguments?.getString("offer")
            val headerText = backStack.arguments?.getString("headerText")
            val fromFlow = backStack.arguments?.getString("fromFlow")
            if (offer != null && headerText != null && fromFlow != null) {
                PrePaymentStatusScreen(
                    navController = navController, orderId = offer, headerText = headerText,
                    fromFlow = fromFlow
                ) {}
            }
        }
        composable("${AppScreens.PrePaymentWebViewScreen.route}/{orderId}/{headerText}/{status}/{fromFlow}") { backStack ->
            val orderId = backStack.arguments?.getString("orderId")
            val headerText = backStack.arguments?.getString("headerText")
            val status = backStack.arguments?.getString("status")
            val fromFlow = backStack.arguments?.getString("fromFlow")
            if (orderId != null && headerText != null && status != null && fromFlow != null) {
                PrePartPaymentWebView(
                    navController = navController, url = headerText, orderId = orderId,
                    status = status, fromFlow = fromFlow
                )
            }
        }

        composable("${AppScreens.CreateIssueScreen.route}/{orderId}/{providerId}/{orderState}/{fromFlow}") {
            val orderId = it.arguments?.getString("orderId")
            val providerId = it.arguments?.getString("providerId")
            val orderState = it.arguments?.getString("orderState")
            val fromFlow = it.arguments?.getString("fromFlow")
            if (orderId != null && providerId != null && orderState != null && fromFlow != null) {
                CreateIssueScreen(
                    navController = navController, orderId = orderId, providerId = providerId,
                    orderState = orderState, fromFlow = fromFlow
                )
            }
        }
        composable("${AppScreens.BankDetailScreen.route}/{id}/{fromFlow}") { backStack ->
            val id = backStack.arguments?.getString("id")
            val fromFlow = backStack.arguments?.getString("fromFlow")
            if (fromFlow != null && id != null) {
                BankDetailScreen(navController = navController, id = id, fromFlow = fromFlow)
            }
        }

        composable("${AppScreens.IssueListScreen.route}/{orderId}/{loanState}/{providerId}/{fromFlow}/{fromScreen}") { backStack ->
            val orderId = backStack.arguments?.getString("orderId")
            val fromScreen = backStack.arguments?.getString("fromScreen")
            val fromFlow = backStack.arguments?.getString("fromFlow")
            val providerId = backStack.arguments?.getString("providerId")
            val loanState = backStack.arguments?.getString("loanState")
            if (orderId != null && fromScreen != null && fromFlow != null && providerId != null && loanState != null) {
                IssueListScreen(
                    navController = navController, orderId = orderId, loanState = loanState,
                    providerId = providerId, fromFlow = fromFlow, fromScreen = fromScreen
                )
            }
        }

        composable(AppScreens.UnexpectedErrorScreen.route) {
            UnexpectedErrorScreen(onClick = {})
        }

        composable("${AppScreens.BankKycVerificationScreen
            .route}/{transactionId}/{kycUrl}/{offerId}/{verificationStatus}/{fromFlow}") {
            backStack ->
            val transactionId = backStack.arguments?.getString("transactionId")
            val kycUrl = backStack.arguments?.getString("kycUrl")
            val offerId = backStack.arguments?.getString("offerId")
            val verificationStatus = backStack.arguments?.getString("verificationStatus")
            val fromFlow = backStack.arguments?.getString("fromFlow")
            if (transactionId != null && kycUrl != null && offerId != null && verificationStatus !=
                    null && fromFlow != null) {
                GstBankKycVerificationScreen(
                    navController = navController, transactionId = transactionId, kycUrl = kycUrl,
                    offerId = offerId,
                    verificationStatus = verificationStatus, fromFlow = fromFlow
                )
            }
        }
        composable(AppScreens.LoanStatusScreen.route) {
            LoanStatusScreen(navController = navController)
        }

        composable(AppScreens.LoanListScreen.route) {
            LoanListScreen(navController = navController)
        }

        composable(AppScreens.LoanStatusDetailScreen.route) {
            LoanStatusDetailScreen(navController = navController)
        }

        composable(AppScreens.UserDetailScreen.route) {
            UserDetailScreen(navController = navController)
        }

        // Gst Loan Flow Mobile Navigation

        composable("${AppScreens.GstInvoiceLoanScreen.route}/{fromFlow}") { backStack ->
            val fromFlow = backStack.arguments?.getString("fromFlow")
            if (fromFlow != null) {
                GstInvoiceLoanScreen(navController = navController, fromFlow = fromFlow)
            }
        }

        composable("${AppScreens.GstDetailsScreen.route}/{fromFlow}") { backStack ->
            val fromFlow = backStack.arguments?.getString("fromFlow")
            if (fromFlow != null) {
                GstDetailsScreen(navController = navController, fromFlow = fromFlow)
            }
        }

        composable("${AppScreens.GstInformationScreen.route}/{fromFlow}/{invoiceId}") { backStack ->
            val fromFlow = backStack.arguments?.getString("fromFlow")
            val invoiceId = backStack.arguments?.getString("invoiceId")
            if (fromFlow != null && invoiceId != null) {
                GstInformationScreen(
                    navController = navController, fromFlow = fromFlow, invoiceId = invoiceId
                )
            }
        }

        composable("${AppScreens.GstNumberVerifyScreen.route}/{mobileNumber}/{fromFlow}") { backStack ->
            val mobileNumber = backStack.arguments?.getString("mobileNumber")
            val fromFlow = backStack.arguments?.getString("fromFlow")
            if (fromFlow != null && mobileNumber != null) {
                GstNumberVerifyScreen(
                    navController = navController, gstMobileNumber = mobileNumber,
                    fromFlow = fromFlow
                )
            }
        }

        composable("${AppScreens.GstInvoiceDetailScreen.route}/{fromFlow}/{invoiceId}") { backStack ->
            val fromFlow = backStack.arguments?.getString("fromFlow")
            val invoiceId = backStack.arguments?.getString("invoiceId")
            if (fromFlow != null && invoiceId != null) {
                GstInvoiceDetailScreen(
                    navController = navController, fromFlow = fromFlow,invoiceId = invoiceId
                )
            }
        }

        composable("${AppScreens.GstInvoiceLoansScreen.route}/{fromFlow}") { backStack ->
            val fromFlow = backStack.arguments?.getString("fromFlow")
            if (fromFlow != null) {
                GstInvoiceLoansScreen(navController = navController, fromFlow = fromFlow)
            }
        }

        composable("${AppScreens.InvoiceDetailScreen.route}/{fromFlow}/{invoiceId}") { backStack ->
            val fromFlow = backStack.arguments?.getString("fromFlow")
            val invoiceId = backStack.arguments?.getString("invoiceId")
            if (fromFlow != null && invoiceId != null) {
                InvoiceDetailScreen(
                    navController = navController, fromFlow = fromFlow,invoiceId = invoiceId
                )
            }
        }
        composable("${AppScreens.GstLoanOfferListScreen.route}/{transactionId}/{offerResponse}/{fromFlow}") {
            backStackEntry ->
            val transactionId = backStackEntry.arguments?.getString("transactionId")
            val offerResponse = backStackEntry.arguments?.getString("offerResponse")
            val fromFlow = backStackEntry.arguments?.getString("fromFlow")
            if (transactionId != null && offerResponse != null && fromFlow != null) {
                GstLoanOfferListScreen(
                    navController = navController, transactionId=transactionId, fromFlow = fromFlow,
                    offerResponse = offerResponse,
                )
            }
        }

        composable("${AppScreens.GstKycWebViewScreen
            .route}/{transactionId}/{kycUrl}/{offerId}/{fromScreen}/{fromFlow}") { backStack ->
            val transactionId = backStack.arguments?.getString("transactionId")
            val kycUrl = backStack.arguments?.getString("kycUrl")
            val offerId = backStack.arguments?.getString("offerId")
            val fromScreen = backStack.arguments?.getString("fromScreen")
            val fromFlow = backStack.arguments?.getString("fromFlow")
            if (transactionId != null && kycUrl != null && offerId != null && fromScreen != null &&
                fromFlow != null) {
                GstKycWebViewScreen(
                    navController = navController, transactionId= transactionId, url = kycUrl, id
                    = offerId,
                    fromScreen = fromScreen, fromFlow = fromFlow, pageContent = {}
                )
            }
        }

        composable("${AppScreens.GstInvoiceLoanOfferScreen.route}/{offerResponse}/{fromFlow}") { backStackEntry ->
            val offerResponse = backStackEntry.arguments?.getString("offerResponse")
            val fromFlow = backStackEntry.arguments?.getString("fromFlow")
            if (offerResponse != null && fromFlow != null) {
                GstInvoiceLoanOfferScreen(
                    navController = navController, offerResponse = offerResponse,
                    fromFlow = fromFlow
                )
            }
        }

        composable("${AppScreens.IssueDetailScreen.route}/{issueId}/{fromFlow}") { backStackEntry ->
            val issueId = backStackEntry.arguments?.getString("issueId")
            val fromFlow = backStackEntry.arguments?.getString("fromFlow")
            if (issueId != null && fromFlow != null) {
                IssueDetailScreen(
                    navController = navController, issueId = issueId, fromFlow = fromFlow
                )
            }
        }

        //Purchase Finance

        composable("${AppScreens.DownPaymentScreen.route}/{fromFlow}") {
            val fromFlow = it.arguments?.getString("fromFlow")
            if (fromFlow != null) {
                DownPaymentScreen(navController = navController, fromFlow = fromFlow)
            }
        }

        //Documents
        composable(AppScreens.TermsConditionsScreen.route) {
            TermsConditionsScreen(navController = navController)
        }

        composable(AppScreens.PrivacyPolicyScreen.route) {
            PrivacyPolicyScreen(navController = navController)
        }
        composable(AppScreens.AboutUsScreen.route) {
            AboutUsScreen(navController = navController)
        }

        //Negative Scenario
        composable("${AppScreens.FormRejectionScreen.route}/{fromFlow}/{errorTitle}/{errorMsg}") {
            backStackEntry ->
            val fromFlow = backStackEntry.arguments?.getString("fromFlow")
            val errorTitle = backStackEntry.arguments?.getString("errorTitle")
            val errorMsg = backStackEntry.arguments?.getString("errorMsg")

            if (errorTitle != null && errorMsg != null && fromFlow != null) {
                FormRejectionScreen(
                    navController = navController, errorTitle = errorTitle, errorMsg = errorMsg,
                    fromFlow = fromFlow
                )
            }
        }

        composable("${AppScreens.FormRejectionScreen.route}/{fromFlow}/{errorMsg}") {
                backStackEntry ->
            val fromFlow = backStackEntry.arguments?.getString("fromFlow")
            val errorTitle = backStackEntry.arguments?.getString("errorTitle")
            val errorMsg = backStackEntry.arguments?.getString("errorMsg")

            if (errorTitle != null && errorMsg != null && fromFlow != null) {
                FormRejectionScreen(
                    navController = navController, errorTitle = errorTitle, errorMsg = errorMsg,
                    fromFlow = fromFlow
                )
            }
        }
    }
}