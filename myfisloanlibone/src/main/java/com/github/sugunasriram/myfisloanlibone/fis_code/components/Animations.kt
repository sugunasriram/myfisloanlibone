package com.github.sugunasriram.myfisloanlibone.fis_code.components

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionResult
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateKycScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToLoanProcessScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.semiBold20Text500
import kotlinx.coroutines.delay

@SuppressLint("ResourceType")
@Composable
fun LoaderAnimation(
    text: String = stringResource(id = R.string.please_wait_processing),
    updatedText: String = stringResource(id = R.string.generating_account_aggregator),
        delayInMillis: Long = 15000, @DrawableRes image: Int = R.raw.invoice_for_offer,
    @DrawableRes updatedImage: Int = R.raw.invoice_for_offer
) {
    var sizeDepends = true
    var currentText by remember { mutableStateOf(text) }
    var currentImage by remember { mutableStateOf(image) }

    LaunchedEffect(Unit) {
        delay(delayInMillis)
        sizeDepends = false
        currentText = updatedText
        currentImage = updatedImage
    }

    val compositionResult: LottieCompositionResult = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(currentImage)
    )
    val progressAnimation by animateLottieCompositionAsState(
        compositionResult.value, isPlaying = true, iterations = LottieConstants.IterateForever,
        speed = 0.5f // Set the animation speed to 0.5
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Transparent), // Set background to transparent
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            composition = compositionResult.value, progress = progressAnimation,
            modifier = Modifier.size(height = 500.dp, width = 300.dp)
        )
        Text(
            text = currentText, style = semiBold20Text500, textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(start = 30.dp, end = 30.dp)
                .fillMaxWidth(),
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 30.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.ondc_icon),
                contentDescription = stringResource(id = R.string.ondc_icon),
                modifier = Modifier.size(height = 50.dp, width = 200.dp)
            )
        }
    }
}

@SuppressLint("ResourceType")
@Composable
fun AnimationLoader(
    delayInMillis: Long = 10000, @DrawableRes image: Int = R.raw.processing_wait, id: String,
    transactionId: String,
    navController: NavHostController, fromFlow: String
) {
    LaunchedEffect(Unit) {
        delay(delayInMillis)
        navigateToLoanProcessScreen(
            navController = navController, transactionId = transactionId, statusId = 4,
            responseItem = "No need ResponseItem",
            offerId = id, fromFlow = fromFlow
        )
    }

    val compotionResult: LottieCompositionResult = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(image)
    )
    val progressAnimation by animateLottieCompositionAsState(
        compotionResult.value, isPlaying = true, iterations = LottieConstants.IterateForever,
        speed = 1.0f
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            composition = compotionResult.value, progress = progressAnimation,
            modifier = Modifier.size(width = 300.dp, height = 500.dp)
        )
        Text(
            text = stringResource(id = R.string.please_wait_processing),
            modifier = Modifier
                .padding(start = 30.dp, end = 30.dp)
                .fillMaxWidth(),
            style = semiBold20Text500, textAlign = TextAlign.Center
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 30.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.ondc_icon),
                contentDescription = stringResource(id = R.string.ondc_icon),
                Modifier.size(height = 50.dp, width = 200.dp)
            )
        }
    }
}

@SuppressLint("ResourceType")
@Composable
fun KycAnimation(
    text: String = stringResource(id = R.string.kyc_verification), delayInMillis: Long = 10000,
    @DrawableRes image: Int = R.raw.kyc_verified, navController: NavHostController,
    transactionId:String, offerId: String,
    responseItem: String
) {

    LaunchedEffect(Unit) {
        delay(delayInMillis)
        navigateKycScreen(
            navController = navController, transactionId =  transactionId, url =  responseItem, id
            =  offerId,
            fromFlow = "Personal Loan"
        )
    }
    val compotionResult: LottieCompositionResult = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(image)
    )
    val progressAnimation by animateLottieCompositionAsState(
        compotionResult.value, isPlaying = true, iterations = LottieConstants.IterateForever,
        speed = 1.0f
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            composition = compotionResult.value, progress = progressAnimation,
            modifier = Modifier.size(500.dp)
        )
        Text(
            text = text,
            modifier = Modifier
                .padding(start = 30.dp, end = 30.dp)
                .fillMaxWidth(),
            style = semiBold20Text500, textAlign = TextAlign.Center
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 30.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.ondc_icon),
                contentDescription = stringResource(id = R.string.ondc_icon),
                Modifier.size(height = 50.dp, width = 200.dp)
            )
        }
    }
}


@SuppressLint("ResourceType")
@Composable
fun AgreementAnimation(
    text: String = stringResource(id = R.string.please_sign_loan_agreement),
    @DrawableRes image: Int = R.raw.sign_loan_agreement
) {
    val compotionResult: LottieCompositionResult = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(image)
    )
    val progressAnimation by animateLottieCompositionAsState(
        compotionResult.value, isPlaying = true, iterations = LottieConstants.IterateForever,
        speed = 1.0f
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            composition = compotionResult.value, progress = progressAnimation,
            modifier = Modifier.size(width = 300.dp, height = 500.dp)
        )
        Text(
            text = text,
            modifier = Modifier
                .padding(start = 30.dp, end = 30.dp)
                .fillMaxWidth(),
            style = semiBold20Text500, textAlign = TextAlign.Center
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 30.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.ondc_icon),
                contentDescription = stringResource(id = R.string.ondc_icon),
                Modifier.size(height = 50.dp, width = 200.dp)
            )
        }
    }
}

@Composable
fun LoanDisburseAnimator() {
    val compotionResult: LottieCompositionResult = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.disburse_gif)
    )
    val progressAnimation by animateLottieCompositionAsState(
        compotionResult.value, isPlaying = true, iterations = LottieConstants.IterateForever,
        speed = 1.0f
    )
    Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.fillMaxWidth()) {
        LottieAnimation(
            composition = compotionResult.value, progress = progressAnimation,
            modifier = Modifier.size(250.dp)
        )
    }
}

@Composable
fun ForeClosureAnimator() {
    val compotionResult: LottieCompositionResult = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.force_closure_gif)
    )
    val progressAnimation by animateLottieCompositionAsState(
        compotionResult.value, isPlaying = true, iterations = LottieConstants.IterateForever,
        speed = 1.0f
    )
    Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.fillMaxWidth()) {
        LottieAnimation(
            composition = compotionResult.value, progress = progressAnimation,
            modifier = Modifier.size(120.dp)
        )
    }
}

