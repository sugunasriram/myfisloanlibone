package com.github.sugunasriram.myfisloanlibone.fis_code.views.invalid

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.ClickableTextWithIcon
import com.github.sugunasriram.myfisloanlibone.fis_code.components.StartingText
import com.github.sugunasriram.myfisloanlibone.fis_code.components.TopBar
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateApplyByCategoryScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlueTitle
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.errorGray
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.errorRed
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.negativeGray
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal14Text500
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal16Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal24Text500
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal30Text700
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal32Text700
import kotlinx.coroutines.delay

@Composable
fun NegativeCommonScreen(
    navController: NavHostController, showRefreshButton: Boolean = true,
    errorText: String = stringResource(id = R.string.page_notfound), onClick: () -> Unit,
    solutionText: String = stringResource(id = R.string.please_try_again_after_sometime),
    errorImage: Painter = painterResource(id = R.drawable.error_404_image),
    buttonText: String = stringResource(id = R.string.retry)
) {
    FixedBottomAndTopBar(showBottom = true, navController = navController) {
        Image(
            painter = errorImage, contentDescription = "", contentScale = ContentScale.Crop
        )

        StartingText(
            text = errorText, alignment = Alignment.TopCenter, style = normal24Text500,
            textColor = Color.Red, top = 10.dp
        )
        StartingText(
            text = solutionText, alignment = Alignment.TopCenter, style = normal16Text400,
            textColor = negativeGray, top = 5.dp
        )
        if (showRefreshButton) {
            ClickableTextWithIcon(text = buttonText, image = R.drawable.refresh_icon) {
                onClick()
            }
        }
    }
}

@Composable
fun FixedBottomAndTopBar(
    navController: NavHostController, showBottom: Boolean = false,
    isSelfScrollable: Boolean = false, pageContent: @Composable () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }
    ) {
        // Top bar
        TopBar(navController = navController)

        // Scrollable content
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (isSelfScrollable) {
                Column(modifier = Modifier.fillMaxSize()) {
                    pageContent()
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    pageContent()
                }
            }
        }
        if (showBottom) {
            Image(
                painter = painterResource(id = R.drawable.ondc_icon),
                contentDescription = stringResource(id = R.string.ondc_icon),
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
                    .size(height = 50.dp, width = 200.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun EmptyLoanStatus(navController: NavHostController) {
    FixedBottomAndTopBar(navController = navController) {
        StartingText(
            text = stringResource(id = R.string.loan_status), textColor = appBlueTitle,
            start = 30.dp, end = 30.dp, top = 30.dp, bottom = 5.dp, style = normal30Text700,
            alignment = Alignment.TopCenter
        )
        Image(
            painter = painterResource(id = R.drawable.loan_not_found),
            contentDescription = "", contentScale = ContentScale.Crop,
            modifier = Modifier.padding(top = 50.dp)
        )
        StartingText(
            text = stringResource(id = R.string.no_existing_loans), textColor = errorGray,
            start = 30.dp, end = 30.dp, top = 10.dp, bottom = 5.dp, style = normal14Text500,
            alignment = Alignment.TopCenter
        )
    }
}

@Composable
fun SessionTimeOutScreen(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.loan_not_found),
                contentDescription = "", contentScale = ContentScale.Crop,
                modifier = Modifier.padding(top = 50.dp)
            )
            StartingText(
                text = stringResource(id = R.string.session_timed_out), textColor = errorGray,
                start = 30.dp, end = 30.dp, top = 10.dp, bottom = 5.dp, style = normal30Text700,
                alignment = Alignment.TopCenter
            )
            StartingText(
                text = stringResource(id = R.string.please_try_after_sometime), start = 30.dp,
                end = 30.dp, textColor = errorGray, top = 10.dp, bottom = 5.dp,
                style = normal14Text500, alignment = Alignment.TopCenter
            )

            ClickableTextWithIcon(
                text = stringResource(id = R.string.retry), image = R.drawable.refresh_icon
            ) { onClick() }
        }
    }
}

@Composable
fun LoanNotApprovedScreen(
    navController: NavHostController,
    text: String = stringResource(id = R.string.loan_not_approved),
) {
    LaunchedEffect(Unit) {
        delay(5000)
        navigateApplyByCategoryScreen(navController = navController)
    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            StartingText(
                text = text, textColor = errorRed, style = normal32Text700,
                alignment = Alignment.TopCenter
            )

            Image(
                painter = painterResource(id = R.drawable.no_loans_approved_image),
                contentDescription = "", contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(top = 50.dp, start = 20.dp, end = 20.dp)
            )
            StartingText(
                text = stringResource(id = R.string.no_offers_available_discription),
                textColor = errorGray, start = 30.dp, end = 30.dp, top = 30.dp, bottom = 5.dp,
                style = normal14Text500, alignment = Alignment.TopCenter
            )
        }
    }
}

@Composable
fun RequestTimeOutScreen(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.request_time_out_image),
                contentDescription = "", contentScale = ContentScale.Crop,
            )

            StartingText(
                text = stringResource(id = R.string.session_timed_out), textColor = errorGray,
                start = 30.dp, end = 30.dp, top = 10.dp, bottom = 5.dp, style = normal30Text700,
                alignment = Alignment.TopCenter
            )
            StartingText(
                text = stringResource(id = R.string.please_try_after_sometime),
                textColor = errorGray, start = 30.dp, end = 30.dp,
                style = normal14Text500, alignment = Alignment.TopCenter
            )

            ClickableTextWithIcon(
                text = stringResource(id = R.string.retry),
                image = R.drawable.refresh_icon
            ) { onClick() }
        }
    }
}


@Composable
fun UnexpectedErrorScreen(
    errorMsgShow: Boolean = true, onClick: () -> Unit,
    errorText: String = stringResource(id = R.string.please_try_after_sometime),
    errorMsg: String = stringResource(id = R.string.something_went_wrong),
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.error_image),
                contentDescription = "", contentScale = ContentScale.Crop,
            )

            StartingText(
                text = stringResource(id = R.string.oops), textColor = errorGray,
                start = 30.dp, end = 30.dp, top = 10.dp, bottom = 5.dp, style = normal30Text700,
                alignment = Alignment.TopCenter
            )
            if (errorMsgShow) {
                StartingText(
                    start = 30.dp, end = 30.dp, top = 30.dp, bottom = 5.dp, style = normal30Text700,
                    text = errorMsg, textColor = errorGray, alignment = Alignment.TopCenter
                )
            }

            StartingText(
                text = errorText, textColor = errorGray, style = normal14Text500, start = 30.dp,
                end = 30.dp, top = 10.dp, bottom = 5.dp, alignment = Alignment.TopCenter
            )

            ClickableTextWithIcon(
                text = stringResource(id = R.string.retry), image = R.drawable.refresh_icon
            ) { onClick() }
        }
    }
}

@Composable
fun UnAuthorizedScreen(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.un_authorized_image),
                contentDescription = "", contentScale = ContentScale.Crop,
            )

            StartingText(
                text = stringResource(id = R.string.un_authorized_access), textColor = errorGray,
                start = 30.dp, end = 30.dp, top = 10.dp, bottom = 5.dp, style = normal30Text700,
                alignment = Alignment.TopCenter
            )
            StartingText(
                text = stringResource(id = R.string.please_try_with_valid_credentials),
                textColor = errorGray, start = 30.dp, end = 30.dp, top = 10.dp, bottom = 5.dp,
                style = normal14Text500, alignment = Alignment.TopCenter
            )

            ClickableTextWithIcon(
                text = stringResource(id = R.string.retry_login), image = R.drawable.refresh_icon,
            ) { onClick() }
        }
    }
}

@Preview
@Composable
fun KYCRejectedScreenPreview() {
    FormRejectionScreen(rememberNavController(),
        fromFlow = "Personal Loan",
        onClick = {},
        errorMsg = stringResource(id = R.string
        .form_submission_rejected_or_pending))
}

@Composable
fun FormRejectionScreen(
    navController: NavHostController,
    fromFlow: String,
    errorMsg: String ?= null,
    onClick: () -> Unit = { navigateApplyByCategoryScreen(navController = navController) },
    errorTitle: String = stringResource(id = R.string.kyc_failed)
) {
//    LaunchedEffect(Unit) {
//        delay(5000)
//        navigateApplyByCategoryScreen(navController = navController)
//    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            StartingText(
                text = errorTitle, textColor = errorRed, style = normal32Text700,
                alignment = Alignment.TopCenter
            )

            Image(
                painter = painterResource(id = R.drawable.no_loans_approved_image),
                contentDescription = "", contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(top = 50.dp, start = 20.dp, end = 20.dp)
            )

            val displayedSubText = errorMsg?.takeIf { it.isNotEmpty() }
                ?: stringResource(id = R.string.form_submission_rejected_or_pending)

            StartingText(
                text = displayedSubText,
                textColor = errorGray, start = 30.dp, end = 30.dp, top = 30.dp, bottom = 5.dp,
                style = normal14Text500, alignment = Alignment.TopCenter
            )

            ClickableTextWithIcon(text = stringResource(id = R.string.retry), image = R.drawable.refresh_icon) {
                onClick()
            }

        }
    }
}