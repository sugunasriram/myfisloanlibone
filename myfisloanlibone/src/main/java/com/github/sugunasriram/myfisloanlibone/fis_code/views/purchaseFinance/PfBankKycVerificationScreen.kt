package com.github.sugunasriram.myfisloanlibone.fis_code.views.purchaseFinance

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
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
import com.github.sugunasriram.myfisloanlibone.fis_code.components.FixedTopBottomScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.components.RegisterText
import com.github.sugunasriram.myfisloanlibone.fis_code.components.SucessImage
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateSignInPage
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToLoanProcessScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToPfKycWebViewScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.gst.GstOfferConfirmResponse
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.pf.PfOfferConfirmResponse
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlue
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.disableColor
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.midNightBlue
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal32Text700
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal36Text500
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.gstLoan.GstBankDetailViewModel
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.purchaseFinance.PfBankDetailViewModel


@Composable
fun PfBankKycVerificationScreen(
    navController: NavHostController, transactionId: String, kycUrl: String, offerId: String,
    fromFlow: String
) {
    val context = LocalContext.current

    val pfBankDetailViewModel: PfBankDetailViewModel = viewModel()
    val bankDetailCollecting by pfBankDetailViewModel.bankDetailCollecting.collectAsState()
    val bankDetailCollected by pfBankDetailViewModel.bankDetailCollected.collectAsState()
    val bankDetailResponse by pfBankDetailViewModel.bankDetailResponse.collectAsState()
    val navigationToSignIn by pfBankDetailViewModel.navigationToSignIn.collectAsState()

    val showInternetScreen by pfBankDetailViewModel.showInternetScreen.observeAsState(false)
    val showTimeOutScreen by pfBankDetailViewModel.showTimeOutScreen.observeAsState(false)
    val showServerIssueScreen by pfBankDetailViewModel.showServerIssueScreen.observeAsState(false)
    val unexpectedErrorScreen by pfBankDetailViewModel.unexpectedError.observeAsState(false)
    val unAuthorizedUser by pfBankDetailViewModel.unAuthorizedUser.observeAsState(false)
    when {
        navigationToSignIn -> navigateSignInPage(navController)
        showInternetScreen -> CommonMethods().ShowInternetErrorScreen(navController)
        showTimeOutScreen -> CommonMethods().ShowTimeOutErrorScreen(navController)
        showServerIssueScreen -> CommonMethods().ShowServerIssueErrorScreen(navController)
        unexpectedErrorScreen -> CommonMethods().ShowUnexpectedErrorScreen(navController)
        unAuthorizedUser -> CommonMethods().ShowUnAuthorizedErrorScreen(navController)
        else -> {
            PfBankKycVerificationScreenView(
                navController = navController,
                transactionId = transactionId,
                bankDetailCollecting = bankDetailCollecting,
                bankDetailResponse = bankDetailResponse,
                fromFlow = fromFlow,
                bankDetailCollected = bankDetailCollected,
                offerId = offerId,
                context = context,
                kycUrl = kycUrl,
                pfBankDetailViewModel = pfBankDetailViewModel
            )
        }
    }
}

@Composable
fun PfBankKycVerificationScreenView(
    navController: NavHostController,
    bankDetailCollecting: Boolean,
    bankDetailResponse: PfOfferConfirmResponse?,
    transactionId: String,
    bankDetailCollected: Boolean,
    offerId: String,
    fromFlow: String,
    context: Context,
    kycUrl: String,
    pfBankDetailViewModel: PfBankDetailViewModel
) {
    if (bankDetailCollecting) {
        CenterProgress()
    } else {
        if (bankDetailCollected) {
            bankDetailResponse?.data?.catalog?.fromURL?.let { url ->
                navigateToLoanProcessScreen(
                    navController = navController, transactionId = transactionId, statusId = 5,
                    offerId = offerId,
                    responseItem = url, fromFlow = "Purchase Finance"
                )
            }

        } else {
            if (kycUrl.length > 0 && !kycUrl.equals("No Need KYC URL", true)) {
                kycUrl?.let { entityKycUrl ->
                    navigateToPfKycWebViewScreen(
                        navController = navController, transactionId = transactionId,
                        kycUrl = entityKycUrl,
                        offerId = offerId,
                        fromScreen = "2", fromFlow = fromFlow
                    )
                }
            } else {
                pfBankDetailViewModel.pfLoanApproved(
                    id = offerId, loanType = "PURCHASE_FINANCE", context = context
                )
            }
        }
    }
}


@Preview
@Composable
private fun KycPreviewScreen() {
    PfBankKycVerificationScreen(
        rememberNavController(), "transactionId", "kycUrl", "asdf",
        "Purchase Finance"
    )

}
