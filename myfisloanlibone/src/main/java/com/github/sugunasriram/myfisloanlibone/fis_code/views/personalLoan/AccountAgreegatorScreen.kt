package com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.FixedTopBottomScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.components.StartImageWithText
import com.github.sugunasriram.myfisloanlibone.fis_code.components.StartingText
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateApplyByCategoryScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToLoanOffersScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToLoanProcessScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToSelectBankScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlueTitle
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal14Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal32Text700

@Composable
fun AccountAgreegatorScreen(
    navController: NavHostController, loanPurpose: String, fromFlow: String
) {
    BackHandler {
        onGoBackClick(
            navController = navController, fromFlow = fromFlow, loanPurpose = loanPurpose
        )
    }
    FixedTopBottomScreen(
        navController = navController, showHyperText = true,
        buttonText = stringResource(id = R.string.next),
        onBackClick = {
            onGoBackClick(
                navController = navController, fromFlow = fromFlow, loanPurpose = loanPurpose
            )
        },
        onClick = {
            navigateToSelectBankScreen(
                navController = navController, purpose = loanPurpose, fromFlow = fromFlow
            )
        }
    ) {
        StartingText(
            text = stringResource(id = R.string.account_agreegator), textColor = appBlueTitle,
            start = 30.dp, end = 30.dp, top = 10.dp, bottom = 5.dp,
            style = normal32Text700, alignment = Alignment.TopCenter
        )
        StartingText(
            text = stringResource(id = R.string.share_bank_statements),
            start = 30.dp, end = 30.dp, bottom = 5.dp, style = normal14Text400,
            textAlign = TextAlign.Start, alignment = Alignment.TopCenter
        )
        Image(
            painter = painterResource(id = R.drawable.account_aggregator_image),
            contentDescription = stringResource(id = R.string.home_page_image),
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 15.dp, end = 15.dp, top = 10.dp),
            contentScale = ContentScale.Fit,
        )
        StartImageWithText()
        StartImageWithText(text = stringResource(id = R.string.no_branch_visits))
        StartImageWithText(text = stringResource(id = R.string.rbi_licensed_entities))
        StartImageWithText(
            text = stringResource(id = R.string.revoke_consent_at_any_time),
            bottom = 15.dp
        )
    }
}

fun onGoBackClick(navController: NavHostController, fromFlow: String, loanPurpose: String) {
    if (fromFlow.equals("Invoice Loan", ignoreCase = true)) {
        navigateToLoanProcessScreen(
            navController = navController, transactionId="Sugu", statusId = 11,
            responseItem = "No Need",
            offerId = "1234", fromFlow = fromFlow
        )
    } else if (fromFlow.equals("Purchase Finance", ignoreCase = true)) {
        navigateToLoanProcessScreen(
            navController = navController, transactionId="Sugu",
            statusId = 19, responseItem = "no need",
            offerId = "1234", fromFlow = fromFlow
        )
    } else if (fromFlow.equals("Personal Loan", ignoreCase = true)) {
        navController.popBackStack()
//        navigateToLoanProcessScreen(
//            navController = navController, transactionId="Sugu",
//            statusId = 8, responseItem = loanPurpose,
//            offerId = "1234", fromFlow = fromFlow
//        )
    }

}

//@Preview(widthDp = 352, heightDp = 737)
@Preview
@Composable
fun AccountAgreegatorScreenPreview() {
    AccountAgreegatorScreen(rememberNavController(), "loanPurpose", "fromFlow")
}

