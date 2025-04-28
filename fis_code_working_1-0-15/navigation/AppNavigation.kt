package com.github.sugunasriram.myfisloanlibone.fis_code.navigation

import android.net.Uri
import androidx.navigation.NavHostController

fun shouldCloseCurrent(navController: NavHostController, closeCurrent: Boolean) {
    if (closeCurrent) {
        navController.popBackStack()
    }
}

fun navigateSignInPage(navController: NavHostController, closeCurrent: Boolean = true) {
    navController.navigate(AppScreens.SignInScreen.route) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToForgotPasswordScreen(navController: NavHostController, closeCurrent: Boolean = true) {
    navController.navigate(AppScreens.ForgotPasswordScreen.route) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToResetPasswordScreen(
    navController: NavHostController, mobileNumber: String, closeCurrent: Boolean = true
) {
    navController.navigate("${AppScreens.ResetPasswordScreen.route}/$mobileNumber") {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateHomePage(navController: NavHostController, closeCurrent: Boolean = false) {
    navController.navigate(AppScreens.HomePageScreen.route) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToRegisterScreen(navController: NavHostController, closeCurrent: Boolean = false) {
    navController.navigate(AppScreens.RegisterScreen.route) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToOtpScreen(
    navController: NavHostController, mobileNumber: String, orderId: String, fromScreen: String,
    closeCurrent: Boolean = true
) {
    val otpScreenPath = "${AppScreens.OtpScreen.route}/$mobileNumber/$orderId/$fromScreen"
    navController.navigate(otpScreenPath) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToOtpVerifyScreen(navController: NavHostController, closeCurrent: Boolean = true) {
    navController.navigate(AppScreens.OtpVerifyScreen.route) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToLanguageScreen(navController: NavHostController, closeCurrent: Boolean = false) {
    navController.navigate(AppScreens.LanguageSelectionScreen.route) {
        shouldCloseCurrent(navController, closeCurrent)

    }
}

fun navigateToUpdateProfileScreen(navController: NavHostController, closeCurrent: Boolean = false) {
    navController.navigate(AppScreens.UpdateProfileScreen.route) {
        shouldCloseCurrent(navController, closeCurrent)
    }

}

fun navigateToLoanStatusScreen(navController: NavHostController, closeCurrent: Boolean = false) {
    navController.navigate(AppScreens.LoanStatusScreen.route) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToLoanListScreen(navController: NavHostController, closeCurrent: Boolean = false) {
    shouldCloseCurrent(navController, closeCurrent)
    navController.navigate(AppScreens.LoanListScreen.route)
}

fun navigateLoanStatusDetailScreen(
    navController: NavHostController, closeCurrent: Boolean = false
) {
    shouldCloseCurrent(navController, closeCurrent)
    navController.navigate(AppScreens.LoanStatusDetailScreen.route)
}

fun navigateToLoanOffersListScreen(
    navController: NavHostController, offerItem: String, fromFlow: String,
    closeCurrent: Boolean = false
) {

    // Encode the responseItem as URI component to ensure it is properly formatted
    val encodedResponseItem = Uri.encode(offerItem)

    // Construct the destination URI by appending the encoded responseItem to the route
    val destinationUri = "${AppScreens.LoanOffersListScreen.route}/$encodedResponseItem/$fromFlow"
    navController.navigate(destinationUri) {
        shouldCloseCurrent(navController, closeCurrent)
    }

}

fun navigateToAAConsentApprovalScreen(
    navController: NavHostController, searchId: String, url: String, fromFlow: String,
    closeCurrent: Boolean = false
) {
    val encodedUrl = Uri.encode(url)
    val encodedSearchId = Uri.encode(searchId)
    navController.navigate("${AppScreens.AAConsentApprovalScreen.route}/$encodedSearchId/$encodedUrl/$fromFlow") {
        shouldCloseCurrent(navController, closeCurrent)
    }

}

fun navigateToLoanOffersListDetailScreen(
    navController: NavHostController, responseItem: String, id: String, showButtonId: String,
    fromFlow: String, closeCurrent: Boolean = false
) {
    // Encode the responseItem as URI component to ensure it is properly formatted
    val encodedResponseItem = Uri.encode(responseItem)

    // Construct the destination URI by appending the encoded responseItem to the route
    val destinationUri =
        "${AppScreens.LoanOffersListDetailScreen.route}/$encodedResponseItem/$id/$showButtonId/$fromFlow"

    // Navigate to the destination URI with proper navigation options
    navController.navigate(destinationUri) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToDashboardScreen(
    navController: NavHostController, id: String, consentHandler: String, fromFlow: String,
    closeCurrent: Boolean = false
) {
    val destinationUrl = "${AppScreens.LoanSummary.route}/$id/$consentHandler/$fromFlow"
    navController.navigate(destinationUrl) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToLoanDetailScreen(
    navController: NavHostController, orderId: String, fromFlow: String,
    closeCurrent: Boolean = false
) {
    // Encode the responseItem as URI component to ensure it is properly formatted
    val encodedLoanItem = Uri.encode(orderId)

    // Construct the destination URI by appending the encoded responseItem to the route
    val destinationUri = "${AppScreens.RepaymentScheduleScreen.route}/$encodedLoanItem/$fromFlow"

    navController.navigate(destinationUri) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateUserDetailScreen(navController: NavHostController, closeCurrent: Boolean = false) {
    navController.navigate(AppScreens.UserDetailScreen.route) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateApplyByCategoryScreen(navController: NavHostController, closeCurrent: Boolean = true) {
    navController.navigate(AppScreens.ApplyBycategoryScreen.route) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToPersonaLoanScreen(
    navController: NavHostController, fromFlow: String, closeCurrent: Boolean = false
) {
    val destinationUrl = "${AppScreens.PersonalLoanScreen.route}/$fromFlow"
    navController.navigate(destinationUrl) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToLoanProcessScreen(
    navController: NavHostController, transactionId: String, statusId: Int, responseItem: String,
    offerId: String,
    fromFlow: String, closeCurrent: Boolean = false
) {

    // Encode the responseItem as URI component to ensure it is properly formatted
    val encodedResponseItem = Uri.encode(responseItem)

    // Construct the destination URI by appending the encoded responseItem to the route
    val destinationUri =
        "${AppScreens.LoanProcessScreen
            .route}/$transactionId/$statusId/$encodedResponseItem/$offerId/$fromFlow"

    // Navigate to the destination URI with proper navigation options
    navController.navigate(destinationUri) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToAnnualIncomeScreen(
    navController: NavHostController, fromFlow: String, closeCurrent: Boolean = false
) {
    val destinationUrl = "${AppScreens.AnnualIncomeScreen.route}/$fromFlow"
    navController.navigate(destinationUrl) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToReviewDetailsScreen(
    navController: NavHostController, purpose: String, fromFlow: String,
    closeCurrent: Boolean = false
) {
    navController.navigate("${AppScreens.ReviewDetailsScreen.route}/$purpose/$fromFlow") {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToEditLoanRequestScreen(
    navController: NavHostController, id: String, loanAmount: String, minLoanAmount: String,
    loanTenure: String,
    offerId: String, fromFlow: String, closeCurrent: Boolean = false
) {
    val encodeId = Uri.encode(id)
    val encodedOfferId = Uri.encode(offerId)
    val encodedLoanAmount = Uri.encode(loanAmount)
    val encodedMinLoanAmount = Uri.encode(minLoanAmount)
    val encodedLoanTenure = Uri.encode(loanTenure)
    val path =
        "${AppScreens.EditLoanRequestScreen
            .route}/$encodeId/$encodedLoanAmount/$encodedMinLoanAmount/$encodedLoanTenure" +
                "/$encodedOfferId/$fromFlow"
    navController.navigate(path) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToLoanDisbursementScreen(
    navController: NavHostController, transactionId: String, id: String,
    fromFlow: String, closeCurrent: Boolean = false,
) {
    navController.navigate("${AppScreens.LoanDisbursementScreen.route}/$transactionId/$id/$fromFlow") {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToAccountDetailsScreen(
    navController: NavHostController, id: String, fromFlow: String, closeCurrent: Boolean = false
) {
    navController.navigate("${AppScreens.AccountDetailsScreen.route}/$id/$fromFlow") {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToWebViewFlowOneScreen(
    navController: NavHostController, purpose: String, fromFlow: String,
    closeCurrent: Boolean = false
) {
    navController.navigate("${AppScreens.SearchWebViewScreen.route}/$purpose/$fromFlow") {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToWebViewScreen(
    navController: NavHostController, transactionId: String, urlToOpen: String, searchId: String,
    fromFlow: String,
    closeCurrent: Boolean = false
) {
    val encodedUrl = Uri.encode(urlToOpen)
    val navigatePath = "${AppScreens.SearchWebView
        .route}/$transactionId/$encodedUrl/$searchId/$fromFlow"
    navController.navigate(navigatePath) {
        shouldCloseCurrent(navController, closeCurrent)
    }

}

fun navigateKycScreen(
    navController: NavHostController, transactionId: String, url: String, id: String, fromFlow:
    String,
    closeCurrent: Boolean = false
) {
    // Encode the responseItem as URI component to ensure it is properly formatted
    val encodedUrl = Uri.encode(url)

    // Construct the destination URI by appending the encoded responseItem to the route
    val destinationUri = "${AppScreens.WebKycScreen.route}/$transactionId/$encodedUrl/$id/$fromFlow"

    // Navigate to the destination URI with proper navigation options
    navController.navigate(destinationUri) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}


fun navigateToRepaymentScreen(
    navController: NavHostController, transactionId: String, url: String, id: String, fromFlow:
    String,
    closeCurrent: Boolean = false
) {
    // Encode the responseItem as URI component to ensure it is properly formatted
    val encodedUrl = Uri.encode(url)

    // Construct the destination URI by appending the encoded responseItem to the route
    val destinationUri = "${AppScreens.RepaymentWebScreen.route}/$transactionId/$encodedUrl/$id/$fromFlow"

    // Navigate to the destination URI with proper navigation options
    navController.navigate(destinationUri) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToLoanAgreementScreen(
    navController: NavHostController, transactionId: String, id: String, loanAgreementFormUrl:
    String, fromFlow: String,
    closeCurrent: Boolean = false
) {
    val encodedUrl = Uri.encode(loanAgreementFormUrl)
    // Construct the destination URI by appending the encoded responseItem to the route
    val destinationUri = "${AppScreens.LoanAgreementWebScreen
        .route}/$transactionId/$id/$encodedUrl/$fromFlow"

    // Navigate to the destination URI with proper navigation options
    navController.navigate(destinationUri) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToAnimationLoader(
    navController: NavHostController, transactionId: String, id: String, fromFlow: String,
    closeCurrent: Boolean = false
) {

    // Construct the destination URI by appending the encoded responseItem to the route
    val destinationUri = "${AppScreens.AnimationLoader.route}/$transactionId/$id/$fromFlow"

    // Navigate to the destination URI with proper navigation options
    navController.navigate(destinationUri) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}


fun navigateToKycAnimation(
    navController: NavHostController, transactionId: String, offerId: String, responseItem: String,
    closeCurrent: Boolean = false
) {
    val encodedUrl = Uri.encode(responseItem)

    // Construct the destination URI by appending the encoded responseItem to the route
    val destinationUri = "${AppScreens.KycAnimation.route}/$transactionId/$encodedUrl/$offerId"

    // Navigate to the destination URI with proper navigation options
    navController.navigate(destinationUri) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToSelectBankScreen(
    navController: NavHostController, purpose: String, fromFlow: String,
    closeCurrent: Boolean = false
) {
    navController.navigate("${AppScreens.SelectBankScreen.route}/$purpose/$fromFlow") {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToAccountAgreegatorScreen(
    navController: NavHostController, purpose: String, fromFlow: String,
    closeCurrent: Boolean = false
) {
    navController.navigate("${AppScreens.AccountAgreegatorScreen.route}/$purpose/$fromFlow") {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToSelectAccountAgreegatorScreen(
    navController: NavHostController, purpose: String, fromFlow: String,
    closeCurrent: Boolean = false
) {
    navController.navigate("${AppScreens.SelectAccountAgreegatorScreen.route}/$purpose/$fromFlow") {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToPrePaymentStatusScreen(
    navController: NavHostController, orderId: String, headerText: String, fromFlow: String,
    closeCurrent: Boolean = false
) {
    val encodedOffer = Uri.encode(orderId)
    val encodeHeaderText = Uri.encode(headerText)

    // Construct the destination URI by appending the encoded responseItem to the route
    val destinationUri =
        "${AppScreens.PrePaymentStatusScreen.route}/$encodedOffer/$encodeHeaderText/$fromFlow"

    // Navigate to the destination URI with proper navigation options
    navController.navigate(destinationUri) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToCreateIssueScreen(
    navController: NavHostController, orderId: String, providerId: String, orderState: String,
    fromFlow: String, closeCurrent: Boolean = false,
) {
    val destinationPath =
        "${AppScreens.CreateIssueScreen.route}/$orderId/$providerId/$orderState/$fromFlow"
    navController.navigate(destinationPath) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToPrePaymentWebViewScreen(
    navController: NavHostController, orderId: String, headerText: String, status: String,
    fromFlow: String, closeCurrent: Boolean = false
) {
    val encodedOffer = Uri.encode(orderId)
    val encodeHeaderText = Uri.encode(headerText)
    val encodePaymentStatus = Uri.encode(status)

    // Construct the destination URI by appending the encoded responseItem to the route
    val destinationUri =
        "${AppScreens.PrePaymentWebViewScreen.route}/$encodedOffer/$encodeHeaderText/$encodePaymentStatus/$fromFlow"

    // Navigate to the destination URI with proper navigation options
    navController.navigate(destinationUri) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToBankDetailsScreen(
    navController: NavHostController, id: String, fromFlow: String, closeCurrent: Boolean = false
) {
    navController.navigate("${AppScreens.BankDetailScreen.route}/$id/$fromFlow") {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToIssueListScreen(
    navController: NavHostController, orderId: String, loanState: String, providerId: String,
    fromFlow: String, fromScreen: String, closeCurrent: Boolean = false
) {
    val navigatePath =
        "${AppScreens.IssueListScreen.route}/$orderId/$loanState/$providerId/$fromFlow/$fromScreen"
    navController.navigate(navigatePath) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateTOUnexpectedErrorScreen(
    navController: NavHostController, closeCurrent: Boolean = true
) {
    navController.navigate(AppScreens.UnexpectedErrorScreen.route) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}


// Gst Loan App Navigation

fun navigateToGstInvoiceLoanScreen(
    navController: NavHostController, fromFlow: String, closeCurrent: Boolean = false
) {
    val destinatioUrl = "${AppScreens.GstInvoiceLoanScreen.route}/$fromFlow"
    navController.navigate(destinatioUrl) {
        shouldCloseCurrent(navController, closeCurrent)
    }

}

fun navigateToGstDetailsScreen(
    navController: NavHostController, fromFlow: String, closeCurrent: Boolean = false
) {
    val destinatioUrl = "${AppScreens.GstDetailsScreen.route}/$fromFlow"
    navController.navigate(destinatioUrl) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToGstInformationScreen(
    navController: NavHostController, fromFlow: String, invoiceId: String,
    closeCurrent: Boolean = false
) {
    val destinatioUrl = "${AppScreens.GstInformationScreen.route}/$fromFlow/$invoiceId"
    navController.navigate(destinatioUrl) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToBankKycVerificationScreen(
    navController: NavHostController, transactionId: String, kycUrl: String, offerId: String,
    verificationStatus: String,
    fromFlow: String, closeCurrent: Boolean = false
) {
    val encodedUrl = Uri.encode(kycUrl)
    navController.navigate("${AppScreens.BankKycVerificationScreen
        .route}/$transactionId/$encodedUrl/$offerId/$verificationStatus/$fromFlow") {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToGstNumberVerifyScreen(
    navController: NavHostController, mobileNumber: String, fromFlow: String,
    closeCurrent: Boolean = false
) {
    val destinatioUrl = "${AppScreens.GstNumberVerifyScreen.route}/$mobileNumber/$fromFlow"
    navController.navigate(destinatioUrl)
    shouldCloseCurrent(navController, closeCurrent)
}

fun navigateToGstInvoiceDetailScreen(
    navController: NavHostController, fromFlow: String, invoiceId: String,
    closeCurrent: Boolean = false
) {
    val destinatioUrl = "${AppScreens.GstInvoiceDetailScreen.route}/$fromFlow/$invoiceId"
    navController.navigate(destinatioUrl)
    shouldCloseCurrent(navController, closeCurrent)
}

fun navigateToGstInvoiceLoansScreen(
    navController: NavHostController, fromFlow: String,
    closeCurrent: Boolean = false
) {
    val destinatioUrl = "${AppScreens.GstInvoiceLoansScreen.route}/$fromFlow"
    navController.navigate(destinatioUrl) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToInvoiceDetailScreen(
    navController: NavHostController, fromFlow: String, invoiceId: String,
    closeCurrent: Boolean = false
) {
    val encodeData = Uri.encode(invoiceId)
    val destinatioUrl = "${AppScreens.InvoiceDetailScreen.route}/$fromFlow/$encodeData"
    navController.navigate(destinatioUrl) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToGstLoanOfferListScreen(
    navController: NavHostController, offerResponse: String,
    transactionId: String,
    fromFlow: String,
    closeCurrent: Boolean = false
) {
    val encodedOfferResponse = Uri.encode(offerResponse)
    val navigatePath = "${AppScreens.GstLoanOfferListScreen
        .route}/$transactionId/$encodedOfferResponse/$fromFlow"
    navController.navigate(navigatePath) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}


fun navigateToGstKycWebViewScreen(
    navController: NavHostController, transactionId:String, kycUrl: String, offerId: String,
    fromScreen: String,
    fromFlow: String, closeCurrent: Boolean = true
) {
    val encodedUrl = Uri.encode(kycUrl)
    navController.navigate("${AppScreens.GstKycWebViewScreen
        .route}/$transactionId/$encodedUrl/$offerId/$fromScreen/$fromFlow") {
        shouldCloseCurrent(navController, closeCurrent)
    }

}


fun navigateToGstInvoiceLoanOfferScreen(
    navController: NavHostController, offerResponse: String, fromFlow: String,
    closeCurrent: Boolean = false
) {
    val encodedOfferResponse = Uri.encode(offerResponse)
    val navigatePath =
        "${AppScreens.GstInvoiceLoanOfferScreen.route}/$encodedOfferResponse/$fromFlow"
    navController.navigate(navigatePath) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToIssueDetailScreen(
    navController: NavHostController, issueId: String, fromFlow: String,
    closeCurrent: Boolean = false
) {
    val encodedissueId = Uri.encode(issueId)
    val navigatePath = "${AppScreens.IssueDetailScreen.route}/$encodedissueId/$fromFlow"
    navController.navigate(navigatePath) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}


//Purchase Finance
fun navigateToDownPaymentScreen(
    navController: NavHostController, fromFlow: String,closeCurrent: Boolean = false
) {
    navController.navigate("${AppScreens.DownPaymentScreen.route}/$fromFlow") {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

// Documents
fun navigateToTermsConditionsScreen(
    navController: NavHostController, closeCurrent: Boolean = false
) {
    navController.navigate(AppScreens.TermsConditionsScreen.route) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}

fun navigateToPrivacyPolicyScreen(
    navController: NavHostController, closeCurrent: Boolean = false
) {
    navController.navigate(AppScreens.PrivacyPolicyScreen.route) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}
fun navigateToAboutUsScreen(
    navController: NavHostController, closeCurrent: Boolean = false
) {
    navController.navigate(AppScreens.AboutUsScreen.route) {
        shouldCloseCurrent(navController, closeCurrent)
    }
}


//Negative Scenario
fun navigateToFormRejectedScreen(
    navController: NavHostController, fromFlow: String,
    errorTitle: String?,
    errorMsg: String?, closeCurrent: Boolean = false
) {

    // Construct the destination URI by appending the encoded responseItem to the route
    val destinationUri = "${AppScreens.FormRejectionScreen.route}/$fromFlow/$errorTitle/$errorMsg"

    // Navigate to the destination URI with proper navigation options
    navController.navigate(destinationUri) {
        shouldCloseCurrent(navController, closeCurrent)
    }

}

