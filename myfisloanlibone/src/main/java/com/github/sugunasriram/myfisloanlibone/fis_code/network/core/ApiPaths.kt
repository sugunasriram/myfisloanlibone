package com.github.sugunasriram.myfisloanlibone.fis_code.network.core

import io.ktor.http.Url

class ApiPaths {

    // Base URL Flow Api Path
    val baseUrl = "/api/v1"

    // Auth Flow Api Paths
    val authLogIn = "auth/login"

    val authLogOut = "auth/logout"

    val authSignIn = "auth/signUp"

    val authOtp = "auth/authOtp"

    val authGetAccessToken = "auth/getAccessToken"

    val profile = "users/profile"

    val updateProfile = "users/updateUserDetails"

    val updateUserIncome = "users/updateUserIncome"

    val getUserStatus = "users/getUserstatus"

    val status = "lender/status"

    val offerList = "users/offerList"

    // Password related Api Paths
    val forgotPassword = "users/forgotPassword"

    val resetPassword = "users/resetPassword"

    val verifyOtp = "users/verifyOtp"

    val resendOTP = "auth/resendOTP"

    //Bank Related Api Paths
    val getBankAccounts = "Bank/getBankAccounts"

    val addBank = "Bank/addBank"

    val getBanksList = "static/getBanksList"

    // Server side Events Api Paths
    //Prod
    //val sse = "https://ondcfs.jtechnoparks.in/jt-bap/api/v1/sse"
    //Preprod
    val sse = "https://stagingondcfs.jtechnoparks.in/jt-bap/api/v1/sse"
//    val sse = "https://stagingondcfs.jtechnoparks.in/staging-jt-bap/api/v1/sse"

    // ONDC Flow Api paths
    val search = "lender/search"

    val aaConsentApproval = "lender/customer_aa_consent_approval"

    val updateLoanAmount = "lender/update_loan_amount"

    val updateLoanAgreement = "lender/update_loan_agreement"

    val getIssueCategories = "static/getIssueCategories"

    val getIssueWithSubCategories = "static/getIssueWithSubCategories"

    val initialOfferSelect = "lender/offer_select"

    val loanApproved = "lender/loanApproved"

    val addAccountDetails = "lender/add_account_details"

    val getCustomerLoanList = "loans/getCustomerLoanList"

    val completeListOfOrders = "loans/completeListOfOrders"

    val getOrderStatus = "loans/getOrderStatus"

    val getOrderById = "loans/getOrderById"

    // IGm Related Api Paths
    val issueImageUpload = "issues/issueImageUpload"

    val createIssue = "issues/createIssue"

    val closeIssue = "issues/close"

    val issueList = "issues/issueList"

    val issueStatus = "issues/issue_status"

    val issueById = "issues/issueById"

    val checkOrderIssues = "issues/checkOrderIssues"

    val orderIssues = "issues/orderIssues"

    //Cygnet Related Api Paths

    val cygnetGenerateOtp = "cygnet/cygnetGenerateOtp"

    val verifyOtpForGstIn = "cygnet/verifyOtpForGstin"

    val gstInDetails = "cygnet/gstinDetails"

    // Document
    val aboutUs ="static/about-us"

        val termsOfUse = "static/terms-of-use"

    val privacyPolicy = "static/privacy-policy"
}