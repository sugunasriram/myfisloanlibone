package com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CenterProgress
import com.github.sugunasriram.myfisloanlibone.fis_code.components.FixedTopBottomScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.components.FullWidthRoundShapedCard
import com.github.sugunasriram.myfisloanlibone.fis_code.components.HeaderNextRowValue
import com.github.sugunasriram.myfisloanlibone.fis_code.components.HorizontalDivider
import com.github.sugunasriram.myfisloanlibone.fis_code.components.ImageTextButtonRow
import com.github.sugunasriram.myfisloanlibone.fis_code.components.MultipleColorText
import com.github.sugunasriram.myfisloanlibone.fis_code.components.StartingText
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateApplyByCategoryScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateSignInPage
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToLoanOffersListDetailScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.personaLoan.Offer
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.personaLoan.OfferResponseItem
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlack
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlueTitle
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appRed
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appWhite
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.azureBlueColor
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.greenColour
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal12Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal32Text700
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.whiteGreenColor
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.personalLoan.LoanAgreementViewModel
import kotlinx.serialization.json.Json
import java.util.Locale

private val json1 = Json { prettyPrint = true }
private var loanAmountVal = ""

@Composable
fun LoanOffersListScreen(navController: NavHostController, offerItem: String, fromFlow: String) {
    var backPressedTime by remember { mutableLongStateOf(0L) }
    val context = LocalContext.current
    // Handle back button press
    BackHandler {
        val currentTime = System.currentTimeMillis()
        if (currentTime - backPressedTime < 2000) {
            navigateApplyByCategoryScreen(navController)
        } else {
            CommonMethods().toastMessage(
                context = context, toastMsg = "Press back again to go to the home page"
            )
            backPressedTime = currentTime
        }
    }
    val loanAgreementViewModel: LoanAgreementViewModel = viewModel()
    val offerList by loanAgreementViewModel.offerList.collectAsState()
    val offerListLoaded by loanAgreementViewModel.offerListLoaded.collectAsState()
    val offerListLoading by loanAgreementViewModel.offerListLoading.collectAsState()

    val navigationToSignIn by loanAgreementViewModel.navigationToSignIn.collectAsState()


    if (navigationToSignIn) {
        navigateSignInPage (navController)
    }
    else if (offerListLoading) {
        CenterProgress()
    } else {
        if (offerListLoaded) {
            FixedTopBottomScreen(
                navController = navController, showBottom = false, isSelfScrollable = false,
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
                }
            ) {
                StartingText(
                    text = stringResource(id = R.string.loan_offers),
                    textColor = appBlueTitle,
                    start = 30.dp, end = 30.dp, top = 10.dp, bottom = 5.dp,
                    style = normal32Text700
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Map over your list of offers to create a card for each one
                    offerList?.data?.forEach { offer ->
                        LoanOfferCard(navController, offer, fromFlow)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        } else {
            loanAgreementViewModel.offerList(loanType = "PERSONAL_LOAN", context = context)
        }
    }
}

@Composable
fun LoanOfferCard(navController: NavHostController, offerResponseItem: Offer?, fromFlow: String) {
    val context = LocalContext.current
    var found: Boolean
    offerResponseItem?.let { offerResponse ->
        offerResponse.offer?.let {
            val json = Json { prettyPrint = true }
            val responseItem = json.encodeToString(OfferResponseItem.serializer(), it)
            FullWidthRoundShapedCard(
                onClick = {
                    offerResponse.id?.let { id ->
                        navigateToLoanOffersListDetailScreen(
                            navController = navController, responseItem = responseItem,
                            id = id, showButtonId = "1", fromFlow = fromFlow
                        )
                    }
                },
                cardColor = azureBlueColor,
                bottomPadding = 0.dp,
                start = 10.dp, end = 10.dp
            ) {
                LoanCardView(
                    offerResponse = offerResponse, navController = navController,
                    responseItem = responseItem, fromFlow = fromFlow, context = context
                )
            }
        }
    }
}

@Composable
fun LoanCardView(
    offerResponse: Offer, navController: NavHostController, responseItem: String,
    fromFlow: String, context: Context
) {
    FullWidthRoundShapedCard(
        start = 8.dp, end = 8.dp, top = 5.dp, bottom = 30.dp,
        cardColor = whiteGreenColor,
        onClick = {
            offerResponse.id?.let { id ->
                navigateToLoanOffersListDetailScreen(
                    navController, responseItem, id, "1", fromFlow = fromFlow
                )
            }
        }) {
        LoanCard(
            offerResponse = offerResponse, navController = navController,
            responseItem = responseItem, fromFlow = fromFlow
        )
    }
    TermsAndConditions(offerResponse, context)
}

@Composable
fun LoanCard(
    offerResponse: Offer,
    navController: NavHostController,
    responseItem: String,
    fromFlow: String
) {
    // Use a row to display the bank logo alongside the bank name
    CardHeader(offerResponse, navController, responseItem, fromFlow)

    HorizontalDivider(start = 0.dp, end = 0.dp)
    //Get loan amount that user will get
    offerResponse.offer?.quoteBreakUp?.forEach { quoteBreakUp ->
        quoteBreakUp?.let {
            it.title?.let { title ->
                if (title.lowercase(Locale.ROOT).contains("principal")) {
                    it.value?.let { value ->
                        loanAmountVal = value
                    }
                }
            }
        }
    }
    AmountInterest(offerResponse = offerResponse)

    LoanAmountTenure(offerResponse)
}


@Composable
fun TermsAndConditions(offerResponse: Offer, context: Context) {
    var found: Boolean
    MultipleColorText(
        text = stringResource(id = R.string.terms_and_conditions),
        textColor = appWhite,
        resendOtpColor = appRed,
    ) {
        found = false
        offerResponse.offer?.itemTags?.forEach { itemTag ->
            itemTag?.tags?.forEach { tag ->
                if (tag.key.contains("tnc", ignoreCase = true) || (tag.key.contains(
                        "term",
                        ignoreCase = true
                    ) && tag.key.contains("condition", ignoreCase = true))
                ) {
                    tag.value.let { termsConditions ->
                        CommonMethods().openLink(context, termsConditions)
                    }
                    found = true
                }
                if (found) return@forEach
            }
        }
    }
}

@Composable
fun LoanAmountTenure(offerResponse: Offer) {
    var found: Boolean
    Row {
        found = false
        offerResponse.offer?.itemTags?.forEach { itemTag ->
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
        offerResponse.offer?.itemTags?.forEach { itemTag ->
            itemTag?.tags?.forEach { tag ->
                if (tag.key.contains("INSTALLMENT_AMOUNT", ignoreCase = true)) {
                    val installmentAmount = tag.value
                    HeaderNextRowValue(
                        textHeader = stringResource(
                            id = R.string
                                .installment_amount
                        ) + " (INR)",
                        textValue = installmentAmount,
                        textColorHeader = appBlack,
                        textColorValue = appBlack,
                        modifier = Modifier
                            .padding(start = 10.dp, top = 0.dp, end = 10.dp, bottom = 8.dp)
                            .weight(0.5f)
                    )
                    return@forEach // Exit the loop after processing the first matching tag
                }
            }
        }

    }
}

@Composable
fun AmountInterest(offerResponse: Offer) {
    var found: Boolean
    Row {
        HeaderNextRowValue(
            textHeader = stringResource(id = R.string.max_loan_amount),
            textValue = loanAmountVal,
            textColorHeader = appBlack,
            textColorValue = appBlack,
            modifier = Modifier
                .padding(start = 10.dp, top = 0.dp, end = 10.dp, bottom = 8.dp)
                .weight(0.5f)
        )

        found = false
        offerResponse.offer?.itemTags?.forEach { itemTag ->
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
                            .padding(
                                start = 10.dp,
                                top = 0.dp,
                                end = 10.dp,
                                bottom = 8.dp
                            )
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
fun CardHeader(
    offerResponse: Offer,
    navController: NavHostController,
    responseItem: String,
    fromFlow: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        offerResponse.offer?.providerDescriptor?.name?.let {
            offerResponse.offer.providerDescriptor.images?.get(0)?.url?.let { imageUrl ->
                val context = LocalContext.current
                val isSvg = remember(imageUrl) {
                    imageUrl.contains(
                        ".svg",
                        ignoreCase = true
                    )
                }
                val imageRequest = remember(imageUrl) {
                    if (isSvg) {
                        ImageRequest.Builder(context)
                            .data(imageUrl)
                            .decoderFactory(SvgDecoder.Factory())
                            .size(Size.ORIGINAL)
                            .error(R.drawable.app_logo)
                            .build()
                    } else {
                        ImageRequest.Builder(context)
                            .data(imageUrl)
                            .crossfade(true)
                            .placeholder(R.drawable.app_logo)
                            .error(R.drawable.app_logo)
                            .build()
                    }
                }

                val painter = rememberImagePainter(request = imageRequest)
                ImageTextButtonRow(
                    imagePainter = painter,
                    textHeader = it,
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.robotocondensed_bold)),
                        fontWeight = FontWeight(400)
                    ),
                    buttonText = stringResource(id = R.string.check_now),
                    textColor = appBlack,
                    buttonColor = greenColour.copy(alpha = 0.9f),
                    buttonTextColor = appWhite,
                    buttonTextStyle = normal12Text400,
                    onButtonClick = {
                        offerResponse.id?.let { id ->
                            navigateToLoanOffersListDetailScreen(
                                navController, responseItem, id, "1", fromFlow = fromFlow
                            )
                        }
                    }
                )
            }
        }
    }
}
