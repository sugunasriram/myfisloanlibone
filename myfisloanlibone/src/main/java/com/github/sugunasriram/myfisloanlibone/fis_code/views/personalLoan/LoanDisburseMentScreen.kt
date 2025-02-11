package com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.AgreementAnimation
import com.github.sugunasriram.myfisloanlibone.fis_code.components.FixedTopBottomScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.components.FullWidthRoundShapedCard
import com.github.sugunasriram.myfisloanlibone.fis_code.components.LoanDisburseAnimator
import com.github.sugunasriram.myfisloanlibone.fis_code.components.OnlyReadAbleText
import com.github.sugunasriram.myfisloanlibone.fis_code.components.RegisterText
import com.github.sugunasriram.myfisloanlibone.fis_code.components.SpaceBetweenTextIcon
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateApplyByCategoryScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToDashboardScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.network.core.ApiPaths
import com.github.sugunasriram.myfisloanlibone.fis_code.network.sse.Catalog
import com.github.sugunasriram.myfisloanlibone.fis_code.network.sse.SSEData
import com.github.sugunasriram.myfisloanlibone.fis_code.network.sse.SSEViewModel
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appGray85
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.bokaraGrayColor
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal11Text500
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal16Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal18Text500
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.slateGrayColor
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.views.webview.ConsentHandlerScreen
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

private val json1 = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
}

@SuppressLint("ResourceType")
@Composable
fun LoanDisbursementScreen(navController: NavHostController, transactionId: String,
                           id: String, fromFlow: String) {

    var backPressedTime by remember { mutableLongStateOf(0L) }
    val context = LocalContext.current

    BackHandler {
        val currentTime = System.currentTimeMillis()
        if (currentTime - backPressedTime < 2000) {
            navigateApplyByCategoryScreen(navController)
        } else {
            CommonMethods().toastMessage(
                context = context, toastMsg = "Press back again to go to the Home page"
            )
            backPressedTime = currentTime
        }
    }

    val sseViewModel: SSEViewModel = viewModel()
    val sseEvents by sseViewModel.events.collectAsState()

    LaunchedEffect(Unit) {
        sseViewModel.startListening(ApiPaths().sse)
    }

    val sseData: SSEData? = try {
        if (sseEvents.isNotEmpty()) {
            json1.decodeFromString(sseEvents)
        } else {
            null
        }
    } catch (e: SerializationException) {
        Log.e("LoanDisbursementScreen", "Failed to parse SSE events: $sseEvents", e)
        null
    }
    val type = sseData?.data?.data?.type
    var sseTransactionId = sseData?.data?.data?.txnId
    //Sugu - to remove
    if (sseTransactionId == null){
        sseTransactionId = sseData?.data?.data?.transactionId
    }

    if (sseData == null || type == "INFO") {
        AgreementAnimation(text = "Processing Please Wait...", image = R.raw.processing_wait)
    } else {
        Log.d("LoanDisbursement:", "transactionId :["+transactionId + "] " +
                "sseTransactionId:["+ sseTransactionId)
        if (transactionId == sseTransactionId && type == "ACTION") {
            sseData.data.data.type.let { actionType ->
                sseData.data.data.consent?.let { consent ->
                    if (transactionId == sseTransactionId && actionType == "ACTION" && consent) {
                        MoveToConsentHandlerScreen(
                            sseData = sseData, navController = navController, fromFlow = fromFlow
                        )
                    } else {

                        MoveToDashBoard(
                            navController = navController, id = id, fromFlow = fromFlow,
                            sseData = sseData, context = context
                        )


                    }
                }
            }
        }
    }
}

@Composable
fun MoveToDashBoard(
    navController: NavHostController, id: String, fromFlow: String, sseData: SSEData,
    context: Context
) {
    var backPressedTime by remember { mutableLongStateOf(0L) }

    FixedTopBottomScreen(
        navController = navController, isSelfScrollable = false,
        buttonText = stringResource(id = R.string.go_to_dash_board),
        onBackClick = {
            val currentTime = System.currentTimeMillis()
            if (currentTime - backPressedTime < 2000) {
                navigateApplyByCategoryScreen(navController)
            } else {
                CommonMethods().toastMessage(
                    context = context, toastMsg = "Press back again to go to the Home page"
                )
                backPressedTime = currentTime
            }
        },
        onClick = {
            navigateToDashboardScreen(
                navController = navController, id = id, consentHandler = "1", fromFlow = fromFlow
            )
        }
    ) {
        LoanDisburseAnimator()
        val totalDisburseAmount = sseData.data?.data?.catalog?.item_price?.value
        RegisterText(
            text = stringResource(id = R.string.loan_amount_disburse)
                    + " ₹" + totalDisburseAmount
                    + stringResource(id = R.string.is_ready_to_disburse),
            style = normal18Text500,
            textColor = bokaraGrayColor,
            start = 30.dp, end = 30.dp, top = 10.dp, bottom = 10.dp
        )
        RegisterText(
            text = stringResource(id = R.string.amount_disburse_time),
            start = 30.dp, end = 30.dp, bottom = 10.dp, top = 10.dp,
            style = normal16Text400, textColor = slateGrayColor
        )
        val loanDetails = sseData.data?.data?.catalog
        if (loanDetails != null) {

            var loanDetailsStr =
                stringResource(id = R.string.loan_details) + "\n\n"

            loanDetails.quote_breakup?.forEach { quoteBreakUp ->
                quoteBreakUp.let {
                    it.title?.let { title ->
                        it.value?.let { description ->
                            loanDetailsStr += title + " : " + description + "\n"
                        }
                    }
                }
            }
            SpaceBetweenTextIcon(
                text = stringResource(id = R.string.loan_details),
                image = R.drawable.share_image, start = 24.dp, end = 24.dp
            ) {
                shareContent(context, loanDetailsStr)
            }
            LoanDisbursementCard(loanDetails)
        }

    }
}

@Composable
fun MoveToConsentHandlerScreen(
    sseData: SSEData, navController: NavHostController, fromFlow: String
) {
    sseData.data?.data?.url?.let { url ->
        sseData.data.data.id?.let { consentId ->
            ConsentHandlerScreen(
                navController = navController, urlToOpen = url, id = consentId, fromFlow = fromFlow
            ) {}
        }
    }
}


fun shareContent(context: Context, content: String) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, content)
        type = "text/plain"
    }
    val chooser = Intent.createChooser(shareIntent, null)
    startActivity(context, chooser, null)
}

@Composable
fun LoanDisbursementCard(loanDetail: Catalog?) {
    FullWidthRoundShapedCard(onClick = {}, cardColor = appGray85) {
        loanDetail?.quote_breakup?.forEach { quoteBreakUp ->
            quoteBreakUp.let {
                it.title?.let { title ->
                    it.value?.let { discription ->
                        OnlyReadAbleText(
                            textHeader = title, textValue = discription, style = normal11Text500,
                            textColorValue = bokaraGrayColor, textColorHeader = slateGrayColor
                        )
                    }
                }
            }
        }
    }

}

@Preview
@Composable
fun PreviewLoanDisbursementScreen() {
    LoanDisbursementScreen(
        navController = NavHostController(LocalContext.current),
        transactionId = "Sugu",
        id = "1",
        "Personal"
    )
}